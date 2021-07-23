package com.example.healthscreening

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.healthscreening.classes.QRGenerator

class QrCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        var code:QRGenerator=QRGenerator()
        val bitmap = code.generateQRCode("Sample Text")
        var img:ImageView=findViewById(R.id.ivqrcode)
        img.setImageBitmap(bitmap)
    }
}