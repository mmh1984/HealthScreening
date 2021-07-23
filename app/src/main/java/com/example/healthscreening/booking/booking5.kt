package com.example.healthscreening.booking

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.healthscreening.ClinicMain
import com.example.healthscreening.DomainAddress
import com.example.healthscreening.MySingleton
import com.example.healthscreening.R
import com.example.healthscreening.classes.DateFormatter
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@Suppress("DEPRECATION")
class booking5 : AppCompatActivity() {
    lateinit var clinic:String
    lateinit var clinicid:String
    lateinit var apptype:String
    lateinit var doctor:String
    lateinit var doctorid:String
    lateinit var dateselected:String
    lateinit var timeselected:String
    lateinit var details:String

    lateinit var tvdetails:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking5)

        clinic= intent.getStringExtra("clinic").toString()
        apptype=intent.getStringExtra("apptype").toString()
        clinicid=intent.getStringExtra("clinicid").toString()
        doctor=intent.getStringExtra("doctor").toString()
        doctorid=intent.getStringExtra("doctorid").toString()
        dateselected=intent.getStringExtra("date").toString()
        timeselected=intent.getStringExtra("time").toString()
        var dt=DateFormatter(dateselected)

        var tvclinic:TextView=findViewById(R.id.tvclinicname)
        tvclinic.text=clinic

        var tvappname:TextView=findViewById(R.id.tvappname)
        tvappname.text=apptype

        var tvdoctor:TextView=findViewById(R.id.tvdoctorname)
        tvdoctor.text=doctor

        var tvdate:TextView=findViewById(R.id.tvdate)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            tvdate.text=dt.convert_date()
        }

        var tvtime:TextView=findViewById(R.id.tvtime)
        tvtime.text=timeselected

        tvdetails=findViewById(R.id.etDetails)


    }

    fun button_click(view: View){
        var button=view as Button
        when(button.id){
            R.id.btnconfirm->{
                details=tvdetails.text.toString()
                var pd=ProgressDialog(this)
                pd.setTitle("Appointment")
                pd.setMessage("Saving Appointment")
                pd.setCancelable(false)
                pd.show()
                save_appointment()
                pd.dismiss()
            }
            R.id.btncancel->{
                var ad=AlertDialog.Builder(this)
                ad.setTitle("Appointment")
                ad.setMessage("Cancel this appointment?")
                ad.setPositiveButton("Yes"){dialog,which->
                    var intent= Intent(this,ClinicMain::class.java)
                    startActivity(intent)
                    finish()

                }
                ad.setNegativeButton("No"){dialog,which->}
                ad.show()
            }
            R.id.btnstartover->{
                var intent= Intent(this,booking1::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun save_appointment() = try {
        var sp = getSharedPreferences("HEALTH_APP", Context.MODE_PRIVATE)
        var clientid=sp.getString("patientid","")
        var ip=DomainAddress()
        var url=ip.address + "client/clinics.php?op=saveappointment&clinicid=$clinicid&doctorid=$doctorid"
        url+="&apptype=$apptype&date=$dateselected&time=$timeselected&details=$details&clientid=$clientid"

        var sr=JsonObjectRequest(Request.Method.POST,url,null, { response ->

            var obj1=response.getJSONArray("results")
           if(obj1.length()==0){
               Toast.makeText(this,"Error Saving",Toast.LENGTH_LONG).show()
           }
            else{
                when(obj1.getString(0)){
                    "OK"->{
                        Toast.makeText(this,"Appointment Saved",Toast.LENGTH_LONG).show()
                        var intent=Intent(this,ClinicMain::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "EXISTS"->{
                        Toast.makeText(this,"You have an appointment on this date",Toast.LENGTH_LONG).show()
                    }
                }


            }
        },{error->
            Toast.makeText(this,"RESPONSE ERROR" + error.toString(),Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(this).addToRequestQueue(sr)
    }
    catch(ex:Exception){
        Toast.makeText(this,"save_appointments"+ex.message.toString(),Toast.LENGTH_LONG).show()
    }
}