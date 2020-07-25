package com.sifac_qr_manager.view

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.zxing.integration.android.IntentIntegrator
import com.sifac_qr_manager.R
import com.sifac_qr_manager.model.QRCodeDataRecord
import com.sifac_qr_manager.model.QRCodeRoomDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val FLEXIBLE_UPDATE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(findViewById(R.id.toolbar))

        val link = findViewById<TextView>(R.id.privacy_policy_link)
        link.linksClickable = true
        link.setText(HtmlCompat.fromHtml("<a href=\"https://cosmicraystorm.github.io/SifAC_QR_Manager/\">プライバシーポリシー</a>", HtmlCompat.FROM_HTML_MODE_COMPACT))
        link.movementMethod = LinkMovementMethod.getInstance()

        QRCodeRoomDatabase.GetDatabase(this)
    }

    override fun onResume() {
        super.onResume()

        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    it.isImmediateUpdateAllowed) {
                val options = AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE)
                appUpdateManager.startUpdateFlow(it, this, options)
            }

            // ToDo: Flexibleアップデートの実装
//            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
//                    it.isFlexibleUpdateAllowed) {
//                appUpdateManager.startUpdateFlowForResult(
//                    it, AppUpdateType.FLEXIBLE, this, FLEXIBLE_UPDATE_REQUEST_CODE
//                )
//            }
        }



        val dialog = AgreementDialog(this)
        if (!dialog.isAgreed) {
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setFabAction(listener: (View?, MainActivity) -> Unit) {
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { listener(it, this) }
    }
    var fabHidden = false
        set(value) {
            val fab = findViewById<FloatingActionButton>(R.id.fab)
            if (value) {
                fab.hide()
            }
            else {
                fab.show()
            }
        }

    private lateinit var onQRCodeCapture: (QRCodeDataRecord) -> Unit
    fun setOnQRCodeCapture(listener: (QRCodeDataRecord) -> Unit) {
        this.onQRCodeCapture = listener
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val contents = data?.getByteArrayExtra("SCAN_RESULT_BYTE_SEGMENTS_0");
        if (contents != null) {
            val base64 = Base64.encodeToString(contents, Base64.DEFAULT)
            val newQRCode = QRCodeDataRecord(
                0,
                base64,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Date()),
                null,
                false
            )

            onQRCodeCapture(newQRCode)
        }
    }

    override fun onBackPressed() {
        fab.show()
        super.onBackPressed()
    }
}