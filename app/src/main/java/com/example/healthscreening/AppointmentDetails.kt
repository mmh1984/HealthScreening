package com.example.healthscreening

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.classes.Appointments
import com.example.healthscreening.classes.DateFormatter
import com.example.healthscreening.viewadapters.AppoinmentAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime

class AppointmentDetails : AppCompatActivity() {
    lateinit var cname:TextView
    lateinit var apptype:TextView
    lateinit var doctor:TextView
    lateinit var appdate:TextView
    lateinit var apptime:TextView
    lateinit var appdetails:TextView
    lateinit var bookingdate:TextView
    lateinit var appstatus:TextView
    lateinit var btnqr:Button
    lateinit var btncancel:Button
    lateinit var appid:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_details)

        appid=intent.getStringExtra("appid").toString()
        cname=findViewById(R.id.tvclinicname)
        apptype=findViewById(R.id.tvapptype)
        doctor=findViewById(R.id.tvdoctor)
        appdate=findViewById(R.id.tvappdate)
        apptime=findViewById(R.id.tvapptime)
        appdetails=findViewById(R.id.tvappdetails)
        bookingdate=findViewById(R.id.tvbookingdate)
        appstatus=findViewById(R.id.tvbookingdate)
        btnqr=findViewById(R.id.btnopenqr)
        btncancel=findViewById(R.id.btncancelapp)
        jsonfetch(appid)





    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun jsonfetch(id:String){
        var ip=DomainAddress()
        var url=ip.address +"clinic/controls/appointments.php?op=clientappdetails&id=" + id
        var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


            var obj1 = response.getJSONArray("results")
            if(obj1.length()==0){
                Toast.makeText(this,"No appointments",Toast.LENGTH_LONG).show()
            }
            else {

                for (i in 0 until obj1.length()) {

                    cname.text = obj1.getJSONObject(i).getString("cname")
                    doctor.text= obj1.getJSONObject(i).getString("dname")
                    var dt = DateFormatter(obj1.getJSONObject(i).getString("appdate"))
                    var today=LocalDate.now()
                    var dt2=DateFormatter(today.toString())
                    var formatted=dt2.convert_date()
                    btnqr.isVisible = dt.convert_date() == dt2.convert_date() && obj1.getJSONObject(i).getString("status").toString()!="checkedin"
                    btncancel.isVisible = dt.convert_date() != dt2.convert_date() && obj1.getJSONObject(i).getString("status").toString()=="finished"

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        appdate.text = dt.convert_date()
                    } else {
                        appdate.text = obj1.getJSONObject(i).getString("appdate")
                    }

                    apptime.text= obj1.getJSONObject(i).getString("apptime")
                    apptype.text = obj1.getJSONObject(i).getString("apptype")
                    appdetails.text = obj1.getJSONObject(i).getString("appdetails")
                    appstatus.text = obj1.getJSONObject(i).getString("status")
                    bookingdate.text = obj1.getJSONObject(i).getString("bookingdate")

                }


            }


        }, { error->
            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(this).addToRequestQueue(sr)

    }
fun button_click(view: View){
    var btn=view as Button
    when(btn.id){
        R.id.btnopenqr->{
            var i=Intent(this,QRReader::class.java)
            i.putExtra("appid",appid);
            startActivity(i)
            finish();

        }
        R.id.btncancelapp->{
            val ad=AlertDialog.Builder(this)
            ad.setTitle("Appointments")
            ad.setMessage("Cancel this appointment?")
            ad.setPositiveButton("Yes"){dialog,which->
             Toast.makeText(this,"You have cancelled this appointment",Toast.LENGTH_LONG).show()
            }
            ad.setNegativeButton("No"){dialog,which->

            }
            ad.show()
        }
    }
}
}