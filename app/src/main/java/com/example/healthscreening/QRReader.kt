package com.example.healthscreening

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.budiyev.android.codescanner.*;
import com.example.healthscreening.classes.Doctors
import com.example.healthscreening.viewadapters.DoctorAdapter

private const val CAMERA_REQUEST_CODE=101
class QRReader : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    lateinit var appid:String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_reader)

        appid=intent.getStringExtra("appid").toString();
        setupPermissions()
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //var intent= Intent(this,AppointmentDetails::class.java);
               //intent.putExtra("appid",it.text)
                //startActivity(intent)
                if(it.text==appid){
                    var ad=AlertDialog.Builder(this)
                    ad.setTitle("Appointment");
                    ad.setMessage("Check-in?")
                    ad.setPositiveButton("Yes"){dialog,which->
                        check_in(appid);

                    }

                    ad.show()

                }
                else {
                    Toast.makeText(
                        this, "Appointment not found",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    fun check_in(id:String) {
        try {
            var ip = DomainAddress()
            var url = ip.address + "clinic/controls/appointments.php?op=clientcheckin&id=$id"
            var sr = JsonObjectRequest(Request.Method.GET, url, null, { response ->


                var obj1 = response.getJSONArray("results")
                if (obj1.length() == 0) {
                    Toast.makeText(this, "No results", Toast.LENGTH_LONG).show()
                } else {
                    finish();
                }


            }, { error ->
                Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
            })
            MySingleton.getInstance(this).addToRequestQueue(sr)

        }
        catch(ex:Exception){
            Toast.makeText(this,ex.message.toString(), Toast.LENGTH_LONG).show()
        }


    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }
    fun setupPermissions(){
        val permission=ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        if(permission!=PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }
    fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

     if(grantResults.isEmpty()||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
        Toast.makeText(this,"You need to grant camera permission",Toast.LENGTH_LONG).show()
     }
        else{
            //success
        }
    }
}
