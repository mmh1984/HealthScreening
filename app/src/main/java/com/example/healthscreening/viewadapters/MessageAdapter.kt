package com.example.healthscreening.viewadapters

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthscreening.MessageDetails
import com.example.healthscreening.R
import com.example.healthscreening.classes.Messages
import org.w3c.dom.Text

class MessageAdapter(var c: Context, var list:ArrayList<Messages>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       var view=LayoutInflater.from(c).inflate(R.layout.message_layout,parent,false)
        return MessageApp(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageApp).bind(list[position].subject,list[position].date,list[position].notified)
        holder.itemView.setOnClickListener{
            val i= Intent(c,MessageDetails::class.java);
            i.putExtra("id",list[position].id);
            c.startActivity(i);

        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

    class MessageApp(view: View):RecyclerView.ViewHolder(view){
        var subject=view.findViewById<TextView>(R.id.tvmsgsubject)
        var date=view.findViewById<TextView>(R.id.tvmsgdate)

        fun bind(s:String,d:String,n:String){
            if(n == "NO"){
                subject.setTypeface(null,Typeface.BOLD)

            }
            subject.text=s;
            date.text=d;
        }
    }
}