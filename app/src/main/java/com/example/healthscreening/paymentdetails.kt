package com.example.healthscreening

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.classes.DateFormatter
import java.time.LocalDate

class paymentdetails : AppCompatActivity() {
    lateinit var txtdate: TextView;
    lateinit var txttime: TextView;
    lateinit var txtamount: TextView;
    lateinit var txtremarks: TextView;
    lateinit var txtprescription: TextView;
    lateinit var id: String;
    lateinit var btnback: Button;
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymentdetails)
        id = intent.getStringExtra("appid").toString();
        txtdate = findViewById(R.id.tvdate)
        txttime = findViewById(R.id.tvtime)
        txtamount = findViewById(R.id.tvamount)
        txtremarks = findViewById(R.id.tvremarks)
        txtprescription=findViewById(R.id.tvprescription)
        btnback=findViewById(R.id.btnback)
        jsonfetch()

        btnback.setOnClickListener {
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun jsonfetch() {
        var ip = DomainAddress()
        var url = ip.address + "clinic/controls/payment.php?op=paymentdetails2&appid=" + id
        var sr = JsonObjectRequest(Request.Method.GET, url, null, { response ->


            var obj1 = response.getJSONArray("results")
            if (obj1.length() == 0) {
                Toast.makeText(this, "No appointments", Toast.LENGTH_LONG).show()
            } else {

                for (i in 0 until obj1.length()) {
                    txttime.text = obj1.getJSONObject(i).getString("timefinished")
                    txtremarks.text = obj1.getJSONObject(i).getString("dremarks")
                    txtamount.text = obj1.getJSONObject(i).getString("amount")
                    txtprescription.text = obj1.getJSONObject(i).getString("prescription")
                    var dt = DateFormatter(obj1.getJSONObject(i).getString("pdate"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        txtdate.text = dt.convert_date()
                    } else {
                        txtdate.text = obj1.getJSONObject(i).getString("pdate")
                    }


                }


            }


        }, { error ->
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(this).addToRequestQueue(sr)

    }
}
