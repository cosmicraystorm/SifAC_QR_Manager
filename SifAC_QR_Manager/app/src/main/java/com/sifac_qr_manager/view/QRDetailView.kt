package com.sifac_qr_manager.view

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sifac_qr_manager.R
import com.sifac_qr_manager.databinding.QrDetailViewFragmentBinding
import com.sifac_qr_manager.model.QRCodeDataRecord
import com.sifac_qr_manager.view_model.QRDetail

class QRDetailView : Fragment(), OnBackPressedListener {

    companion object {
        const val QR_ARG = "qr"

        fun newInstance(qr: QRCodeDataRecord): QRDetailView {
            val fragment = QRDetailView()

            val args = Bundle()
            args.putSerializable(QR_ARG, qr)
            fragment.arguments = args

            return fragment
        }
    }

    class QRDetailViewModelFactory(
        private val context: Context,
        val qr: QRCodeDataRecord) :
        ViewModelProvider.AndroidViewModelFactory(context.applicationContext as Application) {

        private var vm: QRDetail? = null
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return QRDetail(context, qr) as T
        }
    }


    private lateinit var qr: QRCodeDataRecord
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = savedInstanceState ?: this.arguments
        if (args != null) {
            this.qr = args.getSerializable(QR_ARG) as QRCodeDataRecord
        }
    }

    private fun buildSpinner(context: Context, spinner: Spinner, items: Array<String>, initialValue: String?, onSelect: (
        parent: AdapterView<*>?,
        view: View?,
        items: Array<String>,
        position: Int,
        id: Long
    ) -> Unit) {
        val adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            items
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onSelect(parent, view, items, position, id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }

        if (initialValue != null) {
            val index = items.indexOf(initialValue)
            spinner.setSelection(index)
        }
    }

    private lateinit var binding: QrDetailViewFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val con = context
        if (con != null) {
            if (con is MainActivity) {
                con.fabHidden = true
            }

            val viewModel = ViewModelProvider(this.viewModelStore, QRDetailViewModelFactory(con, qr)).get(QRDetail::class.java)

            val binding: QrDetailViewFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.qr_detail_view_fragment, container, false
            )
            binding.lifecycleOwner = viewLifecycleOwner

            binding.viewModel = viewModel

            this.binding = binding


            val view = binding.root

            val showButton = view.findViewById<Button>(R.id.show_qr_button)
            showButton.setOnClickListener {
                val manager = parentFragmentManager
                val fragment = ShowQrFragment.newInstance(qr.Data)

                val transaction = manager.beginTransaction()
                transaction.addToBackStack(null)
                transaction.replace(R.id.qr_list, fragment)
                transaction.commit()
            }

            val saveButton = view.findViewById<Button>(R.id.qr_update_button)
            saveButton.setOnClickListener {
                viewModel.save()
//                val listView = parentFragmentManager.fragments.find { it is QRListView }
//                if (listView is QRListView) {
//                    listView.updateItem(qr, pos)
//                }
            }

            val groupSpinner = view.findViewById<Spinner>(R.id.group_spinner)
            buildSpinner(con, groupSpinner, resources.getStringArray(R.array.group_array), viewModel.group) { spinnerParent, spinnerView, items, position, id ->
                val characterItems = when (position) {
                    1 -> {
                        viewModel.group = "μ's"
                        resources.getStringArray(R.array.muses_character_array)
                    }
                    2 -> {
                        viewModel.group = "Aqours"
                        resources.getStringArray(R.array.Aquours_character_array)
                    }
                    3 -> {
                        viewModel.group = "Saint Snow"
                        resources.getStringArray(R.array.saint_snow_character_array)
                    }
                    else -> {
                        viewModel.group = null
                        emptyArray<String>()
                    }
                }

                buildSpinner(con, view.findViewById<Spinner>(R.id.character_spinner), characterItems, viewModel.character) {spinnerParent, spinnerView, items, position, id ->
                    viewModel.character = items[position]
                }
            }


            buildSpinner(con, view.findViewById<Spinner>(R.id.assist_level_spinner), resources.getStringArray(R.array.level_array), viewModel.assistSkillLevel?.toString()) { spinnerParent, spinnerView, items, position, id ->
                val level = items[position].toIntOrNull()
                if (level != null) {
                    viewModel.assistSkillLevel = level
                }
                else {
                    viewModel.assistSkillLevel = null
                }
            }

            buildSpinner(con, view.findViewById(R.id.stage_level_spinner), resources.getStringArray(R.array.level_array), viewModel.stageSkillLevel?.toString()) { spinnerParent, spinnerView, items, position, id ->
                val level = items[position].toIntOrNull()
                if (level != null) {
                    viewModel.stageSkillLevel = level
                }
                else {
                    viewModel.stageSkillLevel = null
                }
            }

            buildSpinner(con, view.findViewById(R.id.camera_level_spinner), resources.getStringArray(R.array.level_array), viewModel.cameraSkillLevel?.toString()) { spinnerParent, spinnerView, items, position, id ->
                val level = items[position].toIntOrNull()
                if (level != null) {
                    viewModel.cameraSkillLevel = level
                }
                else {
                    viewModel.assistSkillLevel = null
                }
            }

            buildSpinner(con, view.findViewById(R.id.difficulty_spinner), resources.getStringArray(R.array.difficulty_array), viewModel.difficulty) {spinnerParent, spinnerView, items, position, id ->
                viewModel.difficulty = items[position]
            }

            buildSpinner(con, view.findViewById(R.id.score_rank_spinner), resources.getStringArray(R.array.score_rank_array), viewModel.scoreRank) {spinnerParent, spinnerView, items, position, id ->
                viewModel.scoreRank = items[position]
            }

            buildSpinner(con, view.findViewById(R.id.perfection_spinner), resources.getStringArray(R.array.perfection_array), viewModel.perfection) {spinnerParent, spinnerView, items, position, id ->
                viewModel.perfection = items[position]
            }

            val usedCheckbox = view.findViewById<CheckBox>(R.id.used_checkbox)
            usedCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.isUsed = isChecked
            }

//            val linerLayout = binding.root.findViewById<LinearLayout>(R.id.qr_info_layout)
//            val imageView = SquareImageView(con)
//
//            val imagePath = viewModel.imagePath
//            if (imagePath != null) {
//                val file = File(imagePath)
//                if (file.exists()) {
//                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
//                    imageView.setImageBitmap(bitmap)
//                }
//                else {
//                    imageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_warning_24))
//                }
//            }
//            else {
//                imageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_menu_camera))
//            }
//
//            linerLayout.addView(imageView, 2)

            return binding.root
        }
        return inflater.inflate(R.layout.qr_detail_view_fragment, container, false)
    }

    override fun onBackPressed() {
        val con = context
        if (con != null && con is MainActivity) {
            con.fabHidden = false
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

//        val con = context
//        if (con != null && con is MainActivity) {
//            con.fabHidden = false
//        }
    }
}