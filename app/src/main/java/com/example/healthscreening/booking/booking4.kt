package com.example.healthscreening.booking

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.DomainAddress
import com.example.healthscreening.MySingleton
import com.example.healthscreening.R
import com.example.healthscreening.classes.Clinics
import com.example.healthscreening.viewadapters.ClinicsAdapter
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class booking4 : AppCompatActivity(),DatePickerDialog.OnDateSetListener {
    lateinit var clinic:String
    lateinit var clinicid:String
    lateinit var apptype:String
    lateinit var doctor:String
    lateinit var doctorid:String

    lateinit var tvdate:TextView
    lateinit var tvtime:TextView
    lateinit var sptime: Spinner
    var dateselected:String=""
    var timeselected:String=""
    var isavailable:Boolean=false

    //date and time
    var day=0
    var month=0
    var year=0
    var hour=0
    var minutes=0

    var savedday=0
    var savedmonth=0
    var savedyear=0
    var savedhour=0
    var savedminutes=0

private fun getDateTimeCalendar(){
    val cal=Calendar.getInstance()
    day=cal.get(Calendar.DAY_OF_MONTH)
    month=cal.get(Calendar.MONTH)
    year=cal.get(Calendar.YEAR)
    hour=cal.get(Calendar.HOUR)
    day=cal.get(Calendar.MINUTE)
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking4)

        clinic= intent.getStringExtra("clinic").toString()
        apptype=intent.getStringExtra("appname").toString()
        clinicid=intent.getStringExtra("clinicid").toString()
        doctor=intent.getStringExtra("doctor").toString()
        doctorid=intent.getStringExtra("doctorid").toString()

        var tvclinic: TextView =findViewById(R.id.tvclinicname)
        var tvappname: TextView =findViewById(R.id.tvappname)
        var tvdoctor:TextView=findViewById(R.id.tvdoctorname)
        tvdate=findViewById(R.id.tvselecteddate)
        tvtime=findViewById(R.id.tvselecttime)

        tvappname.text="Appointment Type:  $apptype"
        tvclinic.text="Clinic Name: "+ clinic
        tvdoctor.text="Doctor: " + doctor

        load_time()

        timeselected=sptime.selectedItem.toString()

    }
fun load_time(){
    sptime=findViewById(R.id.sptime)
    ArrayAdapter.createFromResource(
            this,
            R.array.time_array,
            android.R.layout.simple_spinner_item
    ).also { adapter->
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sptime.adapter=adapter

    }
}

fun check_schedule(view:View){
    if(dateselected.isEmpty()){
        Toast.makeText(this,"No date selected",Toast.LENGTH_LONG).show()
    }
    else if(timeselected.isEmpty()){
        Toast.makeText(this,"No time selected",Toast.LENGTH_LONG).show()
    }
    else{
        timeselected=sptime.selectedItem.toString()
       fetch_results()
    }

}

    fun fetch_results(){
        try {
            var ip = DomainAddress()
            var url = ip.address + "client/clinics.php?op=checkschedule&clinicid=$clinicid&doctorid=$doctorid&appdate=$dateselected&apptime=$timeselected"
            Toast.makeText(this, url, Toast.LENGTH_LONG).show()
            var sr = JsonObjectRequest(Request.Method.GET, url, null, { response ->

                var obj1 = response.getJSONArray("results")
                if (obj1.length() == 0) {
                    isavailable=true


                } else {
                    isavailable=false
                }
                show_dialog()

            }, { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
            MySingleton.getInstance(this).addToRequestQueue(sr)

        }
        catch(ex:Exception){
            Toast.makeText(this,ex.message.toString(),Toast.LENGTH_LONG).show()
        }
    }
    fun show_dialog() {
        val ad = androidx.appcompat.app.AlertDialog.Builder(this)
        ad.setTitle("Appointment")

        if (isavailable) {

            ad.setMessage("This schedule is available, click proceed to continue")
        } else {
            ad.setMessage("This schedule is not available, select another date/time")
        }
        ad.setPositiveButton("OK") { dialog, which ->
            when(isavailable){
                true->{
                    var intent= Intent(this,booking5::class.java)
                    intent.putExtra("clinicid",clinicid)
                    intent.putExtra("clinic",clinic)
                    intent.putExtra("apptype",apptype)
                    intent.putExtra("doctorid",doctorid)
                    intent.putExtra("doctor",doctor)
                    intent.putExtra("date",dateselected)
                    intent.putExtra("time",timeselected)
                    startActivity(intent)
                }
                false->{
                    Toast.makeText(this,"Date not available",Toast.LENGTH_LONG).show()
                }
            }

        }
        ad.setNegativeButton("Close"){dialog,which->

        }

        ad.show()
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedday=dayOfMonth
        savedmonth=month+1
        savedyear=year
        when(savedmonth.toString().length){
            1->{
                dateselected="$savedyear-0$savedmonth-$savedday"
            }
            2->{
                dateselected="$savedyear-$savedmonth-$savedday"
            }
        }

        tvdate.text="$savedday/$savedmonth/$savedyear"
    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun selectdate(view: View){
        getDateTimeCalendar()
        DatePickerDialog(this,this, day,month,year).show()
    }


}


