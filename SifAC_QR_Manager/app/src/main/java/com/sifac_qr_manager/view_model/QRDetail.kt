package com.sifac_qr_manager.view_model

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.sifac_qr_manager.model.QRCodeDataRecord
import com.sifac_qr_manager.model.QRCodeRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QRDetail(
    context: Context,
    private var qr: QRCodeDataRecord
) : ViewModel() {
    private val dao = QRCodeRoomDatabase.GetDatabase(context).QRCodeDao()

    var qrData: String = ""
    var group: String? = null
    var character: String? = null
    var assistSkillName: String? = null
    var assistSkillLevel: Int? = null
    var cameraSkillName: String? = null
    var cameraSkillLevel: Int? = null
    var stageSkillName: String? = null
    var stageSkillLevel: Int? = null
    var costume: String? = null
    var music: String? = null
    var difficulty: String? = null
    var scoreRank: String? = null
    var perfection: String? = null
    var name: String? = null
    var imagePath: String? = null
    var isUsed: Boolean = false
    var userName: String? = null

    init {
        qrData = qr.Data
        group = qr.Group
        character = qr.Character
        assistSkillName = qr.AssistSkillName
        assistSkillLevel = qr.AssistSkillLevel
        cameraSkillName = qr.CameraSkillName
        cameraSkillLevel = qr.CameraSkillLevel
        stageSkillName = qr.StageSkillName
        stageSkillLevel = qr.StageSkillLevel
        costume = qr.Costume
        music = qr.Music
        difficulty = qr.Difficulty
        scoreRank = qr.ScoreRank
        perfection = qr.Perfection
        name = qr.Name
        imagePath = qr.ImagePath
        isUsed = qr.IsUsed
        userName = qr.UserName
    }

    private fun toClass(): QRCodeDataRecord {
        return QRCodeDataRecord(
            qr.ID,
            qrData,
            group,
            character,
            assistSkillName,
            assistSkillLevel,
            cameraSkillName,
            cameraSkillLevel,
            stageSkillName,
            stageSkillLevel,
            costume,
            music,
            difficulty,
            scoreRank,
            perfection,
            name,
            imagePath,
            isUsed,
            userName
        )
    }

    fun isChanged(): Boolean {
        return _isChanged(qr, toClass())
    }
    private fun _isChanged(before: QRCodeDataRecord, after: QRCodeDataRecord): Boolean {
        return before != after
    }

    fun save() {
        val editedQr = toClass()

        if (!_isChanged(qr, editedQr)) {
            return
        }
        qr = editedQr

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.updateQR(editedQr)
            }
        }
    }
}