package com.example.healthscreening.booking

import android.app.ProgressDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.DomainAddress
import com.example.healthscreening.MySingleton
import com.example.healthscreening.R
import com.example.healthscreening.classes.Appointments
import com.example.healthscreening.classes.Clinics
import com.example.healthscreening.classes.DateFormatter
import com.example.healthscreening.viewadapters.AppoinmentAdapter
import com.example.healthscreening.viewadapters.ClinicsAdapter

@Suppress("DEPRECATION")
class booking1 : AppCompatActivity() {
    lateinit var pd:ProgressDialog
    lateinit var rvclinic:RecyclerView
    lateinit var cliniclist:ArrayList<Clinics>
    lateinit var txtsearch:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking1)


        txtsearch=findViewById(R.id.etsearchclinic)
        txtsearch.isEnabled=false
        pd= ProgressDialog(this)
        pd.setTitle("Clinics")
        pd.setMessage("Loading Clinics")
        pd.show()
        jsonfetch()
        txtsearch.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             search_clinics(txtsearch.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }
    fun search_clinics(qry:String){
        try {
            var ip = DomainAddress()
            var url = ip.address + "client/clinics.php?op=search&keyword=$qry"
            var sr = JsonObjectRequest(Request.Method.GET, url, null, { response ->


                var obj1 = response.getJSONArray("results")
                if (obj1.length() == 0) {
                    Toast.makeText(this, "No results", Toast.LENGTH_LONG).show()
                } else {
                    cliniclist = ArrayList<Clinics>()
                    for (i in 0 until obj1.length()) {
                        var cl = Clinics()
                        cl.id = obj1.getJSONObject(i).getString("clinicid")
                        cl.regno = obj1.getJSONObject(i).getString("cregno")
                        cl.name = obj1.getJSONObject(i).getString("cname")
                        cl.address = obj1.getJSONObject(i).getString("caddress")
                        cl.phone1 = obj1.getJSONObject(i).getString("cphone1")
                        cl.phone2 = obj1.getJSONObject(i).getString("cphone2")
                        cl.ophours = obj1.getJSONObject(i).getString("cophours")
                        cl.lat = obj1.getJSONObject(i).getString("clat")
                        cl.lang = obj1.getJSONObject(i).getString("clang")
                        cl.img = obj1.getJSONObject(i).getString("img1")
                        cliniclist.add(cl)
                    }
                    var adp = ClinicsAdapter(this, cliniclist)
                    rvclinic = findViewById(R.id.rvclinic)
                    rvclinic.layoutManager = LinearLayoutManager(this)
                    rvclinic.adapter = adp
                    txtsearch.isEnabled = true

                }


            }, { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
            MySingleton.getInstance(this).addToRequestQueue(sr)
            pd.dismiss()
        }
        catch(ex:Exception){
            Toast.makeText(this,ex.message.toString(),Toast.LENGTH_LONG).show()
        }

    }
    fun jsonfetch(){
        try {
            var ip = DomainAddress()
            var url = ip.address + "client/clinics.php?op=listactive"
            var sr = JsonObjectRequest(Request.Method.GET, url, null, { response ->


                var obj1 = response.getJSONArray("results")
                if (obj1.length() == 0) {
                    Toast.makeText(this, "No results", Toast.LENGTH_LONG).show()
                } else {
                    cliniclist = ArrayList<Clinics>()
                    for (i in 0 until obj1.length()) {
                        var cl = Clinics()
                        cl.id = obj1.getJSONObject(i).getString("clinicid")
                        cl.regno = obj1.getJSONObject(i).getString("cregno")
                        cl.name = obj1.getJSONObject(i).getString("cname")
                        cl.address = obj1.getJSONObject(i).getString("caddress")
                        cl.phone1 = obj1.getJSONObject(i).getString("cphone1")
                        cl.phone2 = obj1.getJSONObject(i).getString("cphone2")
                        cl.ophours = obj1.getJSONObject(i).getString("cophours")
                        cl.lat = obj1.getJSONObject(i).getString("clat")
                        cl.lang = obj1.getJSONObject(i).getString("clang")
                        cl.img = obj1.getJSONObject(i).getString("img1")
                        cliniclist.add(cl)
                    }
                    var adp = ClinicsAdapter(this, cliniclist)
                    rvclinic = findViewById(R.id.rvclinic)
                    rvclinic.layoutManager = LinearLayoutManager(this)
                    rvclinic.adapter = adp
                    txtsearch.isEnabled = true

                }


            }, { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
            MySingleton.getInstance(this).addToRequestQueue(sr)
            pd.dismiss()
        }
        catch(ex:Exception){
            Toast.makeText(this,ex.message.toString(),Toast.LENGTH_LONG).show()
        }
    }


}