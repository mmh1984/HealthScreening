package com.example.healthscreening.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.healthscreening.R

class booking2 : AppCompatActivity() {
    lateinit var clinic:String
    lateinit var clinicid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking2)

        clinic= intent.getStringExtra("clinic").toString()
        clinicid=intent.getStringExtra("clinicid").toString()

        var tvclinic:TextView=findViewById(R.id.tvclinicname)
        tvclinic.text="Clinic Name: "+ clinic

    }

    fun button_click(view: View){
        val button=view as Button
        var apptype:String=""
        when(button.id){
            R.id.btnphysical->{
                apptype="Physical"
            }
            R.id.btnphysicalsports->{
                apptype="School or Sports Physical"
            }
            R.id.btnofficevisit->{
                apptype="Office Visit"
            }
            R.id.btnscreening->{
                apptype="Screening and Diagnostics"
            }

        }
        var intent= Intent(this,booking3::class.java)
        intent.putExtra("clinic",clinic)
        intent.putExtra("clinicid",clinicid)
        intent.putExtra("apptype",apptype)
        startActivity(intent)
    }
}