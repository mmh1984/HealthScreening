package com.example.healthscreening.viewadapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthscreening.DomainAddress
import com.example.healthscreening.R
import com.example.healthscreening.booking.booking4
import com.example.healthscreening.classes.Doctors
import com.squareup.picasso.Picasso

class DoctorAdapter(var c: Context, var list:ArrayList<Doctors>, var params:List<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view=LayoutInflater.from(c).inflate(R.layout.doctor_layout,parent,false)


        return DoctorApp(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DoctorApp).bind(
            list[position].name,
            list[position].regno,
            list[position].special1 + "/" + list[position].special2,
            list[position].email,
            list[position].phone,
            list[position].img,
            list[position].gender + "(" + list[position].nationality + ")"
        )
        holder?.itemView.setOnClickListener{


            var intent= Intent(c,booking4::class.java)
            intent.putExtra("clinic",params[0])
            intent.putExtra("appname",params[1])
            intent.putExtra("clinicid",params[2])
            intent.putExtra("doctorid",list[position].id)
            intent.putExtra("doctor",list[position].name)
            c.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class DoctorApp(view: View):RecyclerView.ViewHolder(view){
        var dname:TextView=view.findViewById(R.id.tvdoctorname)
        var regno:TextView=view.findViewById(R.id.tvregistrationno)
        var special:TextView=view.findViewById(R.id.tvspecial)
        var email:TextView=view.findViewById(R.id.tvemail)
        var phone:TextView=view.findViewById(R.id.tvphone)
        var img:ImageView=view.findViewById(R.id.ivdoctorimg)
        var gender:TextView=view.findViewById(R.id.tvgendernationality)
        fun bind(n:String,r:String,s:String,e:String,p:String,i:String,g:String){
            dname.text=n
            regno.text=r
            special.text=s
            email.text=e
            phone.text=p
            gender.text=g
            var ip= DomainAddress()
            var path=ip.address + i
            Picasso.get().load(path).into(img)
        }

    }
}