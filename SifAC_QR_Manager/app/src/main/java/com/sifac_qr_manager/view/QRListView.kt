package com.sifac_qr_manager.view

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.google.zxing.integration.android.IntentIntegrator
import com.sifac_qr_manager.R
import com.sifac_qr_manager.databinding.QrListViewBinding
import com.sifac_qr_manager.model.QRCodeDataRecord
import com.sifac_qr_manager.view_model.QRList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

//import com.sifac_qr_manager.DomainModel.QRCode

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [QRListView.OnListFragmentInteractionListener] interface.
 */
class QRListView : Fragment() {

    private lateinit var viewModel: QRList

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listener = ListFragmentInteraction()

        val con = context
        if (con != null) {
            viewModel = QRList(con)
            if (con is MainActivity) {
                con.setFabAction { mainView, mainActivity ->  wakeCameraActivity(mainView, mainActivity) }
                con.setOnQRCodeCapture { captureQRCode(it) }
            }
        }
    }

    private suspend fun update(newList: List<QRCodeDataRecord>, callback: Callback, adapter: QRListViewRecyclerViewAdapter): DiffUtil.DiffResult {
        var result: DiffUtil.DiffResult
        withContext(Dispatchers.Default) {
            result = DiffUtil.calculateDiff(callback)
            if (!coroutineContext.isActive) {
                return@withContext
            }
        }

        return result
    }

    private var currentList = emptyList<QRCodeDataRecord>()

    private lateinit var binding: QrListViewBinding
    private fun bindToAdapter(list: List<QRCodeDataRecord>) {
        val adapter = binding.qrcodeList.adapter
        if (adapter == null) {
            currentList = list
            binding.qrcodeList.adapter = QRListViewRecyclerViewAdapter(currentList, listener)
            return
        }

        val viewAdapter = adapter as QRListViewRecyclerViewAdapter
        var result: DiffUtil.DiffResult
        runBlocking {
            result = update(list, Callback(currentList, list), viewAdapter)
        }
        if (currentList.size < list.size) {
            val manager = parentFragmentManager
            val fragment = QRDetailView.newInstance(list.last())

            val transaction = manager.beginTransaction()
            transaction.addToBackStack(null)
            transaction.replace(R.id.qr_list, fragment)
            transaction.commit()
        }
        currentList = list
        viewAdapter.listData = currentList
        result.dispatchUpdatesTo(viewAdapter)
    }



    class Callback(private val old: List<QRCodeDataRecord>, private val new: List<QRCodeDataRecord>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = new.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].ID == new[newItemPosition].ID
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding: QrListViewBinding = DataBindingUtil.inflate(
            inflater, R.layout.qr_list_view, container, false
        )
        this.binding = binding

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = this.viewModel
        binding.viewModel!!.allListLiveData.observe(viewLifecycleOwner, Observer { bindToAdapter(it) })



        return binding.root
    }

    val REQUEST_CODE_PERMISSION = 2
    private fun wakeCameraActivity(view: View?, activity: MainActivity): Unit {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(activity).setMessage("カメラの権限がありません。設定から許可してください").show()
                return
            }
            else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSION)
            }
        }

        val intentIntegrator = IntentIntegrator(activity).apply {
            setPrompt("Scan a QR code")
            setBeepEnabled(false)
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            captureActivity = ActivityQRCodeCapture::class.java
        }
        intentIntegrator.initiateScan()
    }

    private fun captureQRCode(qr: QRCodeDataRecord) {
        viewModel.addQR(qr)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onItemClick(item: QRCodeDataRecord)
        fun onItemLongClick(item: QRCodeDataRecord): Boolean
    }

    inner class ListFragmentInteraction : OnListFragmentInteractionListener {
        override fun onItemClick(item: QRCodeDataRecord) {
            val manager = parentFragmentManager
            val fragment = QRDetailView.newInstance(item)

            val transaction = manager.beginTransaction()
            transaction.addToBackStack(null)
            transaction.replace(R.id.qr_list, fragment)
            transaction.commit()
        }

        override fun onItemLongClick(item: QRCodeDataRecord): Boolean {
            val act = activity
            if (act != null) {
                val alert = AlertDialog.Builder(act)
                alert.setTitle("${item.Name ?: "nameless"}を削除しますか?")

                alert.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    viewModel.deleteQR(item)
                })

                alert.setNegativeButton("Cancel", null)

                alert.show()
            }
            return true
        }
    }

    companion object {
        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            QRListView().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
