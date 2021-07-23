package com.example.healthscreening.viewadapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.healthscreening.AppointmentDetails
import com.example.healthscreening.R
import com.example.healthscreening.classes.Appointments
import com.example.healthscreening.paymentdetails
import java.io.Serializable

class AppoinmentAdapter(var c: Context,var list:ArrayList<Appointments>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(c).inflate(R.layout.appointment_layout, parent, false)
        return ClientApp(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ClientApp).bind(list[position].clinic,list[position].appdate,list[position].apptime,list[position].apptype,list[position].status)

        holder?.itemView.setOnClickListener{

            if(list[position].status=="paid"){
                var intent=Intent(c,paymentdetails::class.java)
                intent.putExtra("appid", list[position].id)
                c.startActivity(intent)
            }
            else {
                var intent=Intent(c,AppointmentDetails::class.java)
                intent.putExtra("appid", list[position].id)
                c.startActivity(intent)
            }

        }
    }



    override fun getItemCount(): Int {
       return list.size
    }

    class ClientApp(view: View):RecyclerView.ViewHolder(view){
        var clinicname=view.findViewById<TextView>(R.id.tv1lclinicname)
        var appdate=view.findViewById<TextView>(R.id.tv1lappdate)
        var apptime=view.findViewById<TextView>(R.id.tv1lapptime)
        var apptype=view.findViewById<TextView>(R.id.tv1lapptype)
        var appstatus=view.findViewById<TextView>(R.id.tvstatus)
        fun bind(c:String,d:String,t:String,tp:String,s:String){
            clinicname.text=c
            appdate.text=d
            apptime.text=t
            apptype.text=tp
            appstatus.text=s;
        }

    }
}