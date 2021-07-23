package com.example.healthscreening.booking

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.DomainAddress
import com.example.healthscreening.MySingleton
import com.example.healthscreening.R
import com.example.healthscreening.classes.Clinics
import com.example.healthscreening.classes.Doctors
import com.example.healthscreening.viewadapters.ClinicsAdapter
import com.example.healthscreening.viewadapters.DoctorAdapter

@Suppress("DEPRECATION")
class booking3 : AppCompatActivity() {
    lateinit var clinic:String
    lateinit var clinicid:String
    lateinit var apptype:String

    lateinit var doctorlist:ArrayList<Doctors>
    lateinit var rvdoctors:RecyclerView
    lateinit var pd: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking3)
        clinic= intent.getStringExtra("clinic").toString()
        apptype=intent.getStringExtra("apptype").toString()
        clinicid=intent.getStringExtra("clinicid").toString()

        var tvclinic: TextView =findViewById(R.id.tvclinicname)
        var tvappname:TextView=findViewById(R.id.tvappname)
        tvappname.text="Appointment Type:  $apptype"
        tvclinic.text="Clinic Name: "+ clinic

        pd= ProgressDialog(this)
        pd.setTitle("Clinics")
        pd.setMessage("Loading Clinics")
        pd.show()
        jsonfetch()
    }

    fun jsonfetch(){
        try {
            var ip = DomainAddress()
            var url = ip.address + "client/clinics.php?op=listdoctors&id=$clinicid"
            var sr = JsonObjectRequest(Request.Method.GET, url, null, { response ->


                var obj1 = response.getJSONArray("results")
                if (obj1.length() == 0) {
                    Toast.makeText(this, "No results", Toast.LENGTH_LONG).show()
                } else {
                    doctorlist = ArrayList<Doctors>()
                    for (i in 0 until obj1.length()) {
                        var cl = Doctors()
                        cl.id = obj1.getJSONObject(i).getString("doctorid")
                        cl.regno = obj1.getJSONObject(i).getString("regno")
                        cl.name = obj1.getJSONObject(i).getString("dname")
                        cl.special1 = obj1.getJSONObject(i).getString("dspecial1")
                        cl.special2 = obj1.getJSONObject(i).getString("dspecial2")
                        cl.phone = obj1.getJSONObject(i).getString("dphone")
                        cl.email = obj1.getJSONObject(i).getString("demail")
                        cl.nationality = obj1.getJSONObject(i).getString("dnationality")
                        cl.gender = obj1.getJSONObject(i).getString("dgender")
                        cl.img = obj1.getJSONObject(i).getString("dimg")
                        doctorlist.add(cl)
                    }
                    var paramList=ArrayList<String>()
                    paramList.add(clinic)
                    paramList.add(apptype)
                    paramList.add(clinicid)
                    var adp = DoctorAdapter(this, doctorlist,paramList)
                    rvdoctors= findViewById(R.id.rvdoctor)
                    rvdoctors.layoutManager = LinearLayoutManager(this)
                    rvdoctors.adapter = adp


                }


            }, { error ->
                Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
            })
            MySingleton.getInstance(this).addToRequestQueue(sr)
            pd.dismiss()
        }
        catch(ex:Exception){
            Toast.makeText(this,ex.message.toString(), Toast.LENGTH_LONG).show()
        }
    }


}