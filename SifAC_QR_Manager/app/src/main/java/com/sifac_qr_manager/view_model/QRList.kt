package com.sifac_qr_manager.view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sifac_qr_manager.model.QRCodeDataRecord
import com.sifac_qr_manager.model.QRCodeRoomDatabase
import com.sifac_qr_manager.view.QRListView
import com.sifac_qr_manager.view.QRListViewRecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QRList(context: Context) : ViewModel() {
    private val dao = QRCodeRoomDatabase.GetDatabase(context).QRCodeDao()

    val allListLiveData: LiveData<List<QRCodeDataRecord>> = dao.getAllQR()

    fun addQR(qr: QRCodeDataRecord) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.addQRData(qr)
            }
        }
    }

    fun deleteQR(qr: QRCodeDataRecord) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteQRData(qr)
            }
        }
    }
}