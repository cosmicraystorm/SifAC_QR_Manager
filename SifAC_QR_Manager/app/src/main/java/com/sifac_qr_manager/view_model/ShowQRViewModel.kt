package com.sifac_qr_manager.view_model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder

class ShowQRViewModel(private val content: String, resources: Resources) : ViewModel() {
    val qrImgaeLiveData: MutableLiveData<Drawable?> = MutableLiveData(null)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val raw = Base64.decode(content, Base64.DEFAULT)
                val contentStringJava = java.lang.String(raw, "windows-1252")
                val contentString = contentStringJava.toString()

                val encoder = BarcodeEncoder()

                val hints = mapOf<EncodeHintType, Any>(
                    EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
                    EncodeHintType.CHARACTER_SET to "CP1252",
                    EncodeHintType.MARGIN to 2
                )
                val bitmap = encoder.encodeBitmap(contentString, BarcodeFormat.QR_CODE, 200, 200, hints)

                val drawable = BitmapDrawable(resources, bitmap)
                qrImgaeLiveData.postValue(drawable)
            }
        }
    }
}