package com.sifac_qr_manager.model

import androidx.room.*
import android.content.Context
import androidx.lifecycle.LiveData
import java.io.Serializable

@Entity(tableName = "DataList")
data class QRCodeDataRecord(
    @PrimaryKey(autoGenerate = true)
    val ID: Int,
    val Data: String,
    val Group: String? = null,
    val Character: String? = null,
    val AssistSkillName: String? = null,
    val AssistSkillLevel: Int? = null,
    val CameraSkillName: String? = null,
    val CameraSkillLevel: Int? = null,
    val StageSkillName: String? = null,
    val StageSkillLevel: Int? = null,
    val Costume: String? = null,
    val Music: String? = null,
    val Difficulty: String? = null,
    val ScoreRank: String? = null,
    val Perfection: String? = null,
    val Name: String? = null,
    val ImagePath: String? = null,
    val IsUsed: Boolean = false,
    val UserName: String? = null
) : Serializable

@Entity(tableName = "Costumes")
data class CostumeNameRecords constructor(
    @PrimaryKey(autoGenerate = true)
    val ID: Int,
    val Name: String,
    val Group: String
)

@Entity(tableName = "Musics")
data class MusicNameRecords constructor(
    @PrimaryKey(autoGenerate = true)
    val ID: Int,
    val Name: String,
    val Group: String
)

@Entity(tableName = "AssistSkillNames")
data class AssistSkillNameRecord constructor(
    @PrimaryKey(autoGenerate = true)
    val ID: Int,
    val Name: String
)

@Entity(tableName = "CameraSkillNames")
data class CameraSkillNameRecord constructor(
    @PrimaryKey(autoGenerate = true)
    val ID: Int,
    val Name: String
)

@Entity(tableName = "StageSkillNames")
data class StageSkillNameRecords constructor(
    @PrimaryKey(autoGenerate = true)
    val ID: Int,
    val Name: String
)


@Dao
interface IQRCodeDao {
    @Insert
    fun addQRData(qr: QRCodeDataRecord)

    @Insert
    fun addCostume(cos: CostumeNameRecords)

    @Insert
    fun addMusic(music: MusicNameRecords)

    @Insert
    fun addAssistSkill(skill: AssistSkillNameRecord)

    @Insert
    fun addCameraSkill(skill: CameraSkillNameRecord)

    @Insert
    fun addStageSkill(skill: StageSkillNameRecords)


    @Delete
    fun deleteQRData(qr: QRCodeDataRecord)

    @Delete
    fun deleteCostume(cos: CostumeNameRecords)

    @Delete
    fun deleteMusic(music: MusicNameRecords)

    @Delete
    fun deleteAssistSkill(assistSkillNameRecord: AssistSkillNameRecord)

    @Delete
    fun deleteCameraSkill(cameraSkillNameRecord: CameraSkillNameRecord)

    @Delete
    fun deleteStageSkill(stageSkillNameRecords: StageSkillNameRecords)


    @Query("SELECT * FROM DataList")
    fun getAllQR(): LiveData<List<QRCodeDataRecord>>

    @Query("SELECT * FROM DataList WHERE ID=:id")
    fun getQR(id: Int): LiveData<QRCodeDataRecord>
    @Update
    fun updateQR(qr: QRCodeDataRecord)

    @Query("SELECT * FROM Costumes")
    fun getAllRegisteredCostumes(): LiveData<List<CostumeNameRecords>>

    @Query("SELECT * FROM Musics")
    fun getAllRegisteredMusics(): LiveData<List<MusicNameRecords>>

    @Query("SELECT * FROM AssistSkillNames")
    fun getAllRegisteredAssistSkillNames(): LiveData<List<AssistSkillNameRecord>>

    @Query("SELECT * FROM CameraSkillNames")
    fun getAllRegisteredCameraSkillNames(): LiveData<List<CameraSkillNameRecord>>

    @Query("SELECT * FROM StageSkillNames")
    fun getAllRegisteredStageSkillNames(): LiveData<List<StageSkillNameRecords>>
}

@Database(entities = arrayOf(
    QRCodeDataRecord::class,
    CostumeNameRecords::class,
    MusicNameRecords::class,
    AssistSkillNameRecord::class,
    CameraSkillNameRecord::class,
    StageSkillNameRecords::class
), version = 1)
abstract class QRCodeRoomDatabase: RoomDatabase() {
    abstract fun QRCodeDao(): IQRCodeDao
    companion object {
        @Volatile
        private var instance: QRCodeRoomDatabase? = null

        fun GetDatabase(context: Context): QRCodeRoomDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(context) {
                val buildedInstance = Room.databaseBuilder(
                    context.applicationContext,
                    QRCodeRoomDatabase::class.java,
                    "SifAC_QRCodeDatabase"
                ).build()
                instance = buildedInstance
                return buildedInstance
            }
        }
    }

}