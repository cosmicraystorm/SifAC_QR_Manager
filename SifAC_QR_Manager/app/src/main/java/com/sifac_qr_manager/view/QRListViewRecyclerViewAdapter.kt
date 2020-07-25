package com.sifac_qr_manager.view

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.sifac_qr_manager.R
import com.sifac_qr_manager.databinding.QrListItemBinding
import com.sifac_qr_manager.model.QRCodeDataRecord


import com.sifac_qr_manager.view.QRListView.OnListFragmentInteractionListener

class QRListViewRecyclerViewAdapter(
    private var mValues: List<QRCodeDataRecord>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<QRListViewRecyclerViewAdapter.ViewHolder>() {

    private lateinit var mOnClickListener: View.OnClickListener
    private lateinit var mOnLongClickListener: View.OnLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: QrListItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.qr_list_item, parent, false)
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.qr_list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.binding.viewModel = item

//        if (item.ImagePath != null) {
////            holder.SummaryImageView.setImageBitmap(item.Attribute.Image.Load())
//        }
//        else {
//            holder.SummaryImageView.setImageBitmap(BitmapFactory.decodeResource(holder.SummaryImageView.resources, R.drawable.ic_menu_camera))
//        }

        holder.SummaryNameTextView.text = item.Name
        holder.SummaryCharacterTextView.text = item.Character
        holder.SummaryMusicTextView.text = item.Music
        holder.SummaryCostumeTextView.text = item.Costume
        holder.SummarySmileLevelTextView.text = item.AssistSkillLevel?.toString() ?: "-"
        holder.SummaryCoolLevelTextView.text = item.CameraSkillLevel?.toString() ?: "-"
        holder.SummaryPureLevelTextView.text = item.StageSkillLevel?.toString() ?: "-"

        this.mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as QRCodeDataRecord
            mListener?.onItemClick(item)
        }
        this.mOnLongClickListener = View.OnLongClickListener { v ->
            val item = v.tag as QRCodeDataRecord
            mListener?.onItemLongClick(item) ?: true
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
            setOnLongClickListener(mOnLongClickListener)
        }
    }

    var listData: List<QRCodeDataRecord> = mValues
        set(value) {
            mValues = value
        }




    override fun getItemCount(): Int = mValues.size



    inner class ViewHolder(val binding: QrListItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        val SummaryImageView: ImageView
        val SummaryNameTextView: TextView
        val SummaryCharacterTextView: TextView
        val SummaryCostumeTextView: TextView
        val SummaryMusicTextView: TextView
        val SummarySmileLevelTextView: TextView
        val SummaryCoolLevelTextView: TextView
        val SummaryPureLevelTextView: TextView

        init {
            val view = binding.root
//            SummaryImageView = view.findViewById(R.id.qrcode_summary_image)
            SummaryNameTextView = view.findViewById(R.id.qrcode_summary_name)
            SummaryCharacterTextView = view.findViewById(R.id.qrcode_summary_character_name)
            SummaryCostumeTextView = view.findViewById(R.id.qrcode_summary_costume)
            SummaryMusicTextView = view.findViewById(R.id.qrcode_summary_music)
            SummarySmileLevelTextView = view.findViewById(R.id.qrcode_summary_smile_level)
            SummaryCoolLevelTextView = view.findViewById(R.id.qrcode_summary_cool_level)
            SummaryPureLevelTextView = view.findViewById(R.id.qrcode_summary_pure_level)
        }
    }
}
