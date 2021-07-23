package com.example.healthscreening.viewadapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.healthscreening.DomainAddress
import com.example.healthscreening.R
import com.example.healthscreening.booking.booking2
import com.example.healthscreening.classes.Clinics
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class ClinicsAdapter(var c: Context, var list: ArrayList<Clinics>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view=LayoutInflater.from(c).inflate(R.layout.clinic_layout, parent, false)
        return ClinicApp(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ClinicApp).bind(
            list[position].name,
            list[position].address,
            list[position].phone1 + "/" + list[position].phone2,
            list[position].email,
            list[position].img
        )
        holder?.openmap.setOnClickListener{
            var lat:String=list[position].lat
            var lang:String=list[position].lang
            val gmmIntentUri = Uri.parse("geo:$lat,$lang" + Uri.decode(list[position].address))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            c.startActivity(mapIntent)
        }
        holder?.select.setOnClickListener{
            var id:String=list[position].id
            var clinicname=list[position].name
            var intent=Intent(c, booking2::class.java);
            intent.putExtra("clinic",clinicname)
            intent.putExtra("clinicid",id)
            c.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ClinicApp(view: View):RecyclerView.ViewHolder(view){
        var cname:TextView=view.findViewById(R.id.tvclinicname)
        var caddress:TextView=view.findViewById(R.id.tvclinicaddress)
        var cphone:TextView=view.findViewById(R.id.tvclinicphone)
        var cemail:TextView=view.findViewById(R.id.tvclinicemail)
        var img:ImageView=view.findViewById(R.id.ivclinicimage)
        var openmap: Button =view.findViewById(R.id.btnviewmap)
        var select:Button=view.findViewById(R.id.btnselect)
        fun bind(n: String, a: String, p: String, e: String, i: String){
            cname.text=n
            caddress.text=a
            cphone.text="Phone: $p"
            cemail.text="Email: $e"
            var ip=DomainAddress()
            var path=ip.address + i
            Picasso.get().load(path).into(img)
        }
    }
}