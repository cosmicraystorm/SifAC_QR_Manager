package com.sifac_qr_manager.view

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sifac_qr_manager.R
import com.sifac_qr_manager.databinding.FragmentShowQrBinding
import com.sifac_qr_manager.view_model.ShowQRViewModel

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ShowQrFragment : Fragment() {

    class QRShowViewModelFactory(
        context: Context,
        private val content: String,
        private val resources: Resources
    ) : ViewModelProvider.AndroidViewModelFactory(context.applicationContext as Application) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ShowQRViewModel(content, resources) as T
        }
    }

    lateinit var viewModel: ShowQRViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = savedInstanceState ?: this.arguments
        if (args != null) {
            val content = args.getString(QR_CONTENT) ?: ""

            val con = context
            if (con != null) {
                this.viewModel = ViewModelProvider(this.viewModelStore, QRShowViewModelFactory(con, content, resources)).get(ShowQRViewModel::class.java)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentShowQrBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_show_qr, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = this.viewModel

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val baseActivity = activity as MainActivity
        if (baseActivity != null) {
            val editAttr = baseActivity.window.attributes
            editAttr.screenBrightness = 1f

            baseActivity.window.attributes = editAttr
        }
    }

    override fun onStop() {
        super.onStop()

        val baseActivity = activity as MainActivity
        if (baseActivity != null) {
            val editAttr = baseActivity.window.attributes
            editAttr.screenBrightness = -1f    // -1にするとシステム設定の輝度に戻る

            baseActivity.window.attributes = editAttr
        }
    }

    companion object {
        private const val QR_CONTENT = "qr content string"

        fun newInstance(content: String): ShowQrFragment {
            val fragment = ShowQrFragment()

            val args = Bundle()
            args.putString(QR_CONTENT, content)
            fragment.arguments = args

            return fragment
        }
    }
}