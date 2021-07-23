package com.example.healthscreening.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.DomainAddress
import com.example.healthscreening.MySingleton
import com.example.healthscreening.R
import com.example.healthscreening.classes.Appointments
import com.example.healthscreening.classes.DateFormatter
import com.example.healthscreening.classes.Messages
import com.example.healthscreening.viewadapters.AppoinmentAdapter
import com.example.healthscreening.viewadapters.MessageAdapter


class MessageFragment : Fragment() {


    lateinit var sharedPreferences: SharedPreferences
    lateinit var messagelist:ArrayList<Messages>
    lateinit var rv: RecyclerView
    lateinit var sp: SharedPreferences
    lateinit var sw: SwipeRefreshLayout
    lateinit var adp: MessageAdapter
    lateinit var tvstatus: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_message, container, false)

        sp=requireActivity().getSharedPreferences("HEALTH_APP", Context.MODE_PRIVATE)
        sw=view.findViewById(R.id.swipe_refresh);
        rv=view.findViewById(R.id.rvappointments)
        tvstatus=view.findViewById(R.id.tvappcount)
        val pd= ProgressDialog(activity)
        pd.setTitle("Appointments")
        pd.setTitle("Fetching Records")
        pd.setIcon(R.drawable.ic_baseline_appointments)
        pd.setCancelable(false)
        pd.show()
        jsonfetch()
        pd.dismiss()
        sw.setOnRefreshListener{
            sw.isRefreshing=true;

            jsonfetch()
            if(rv.isVisible){
                adp.notifyDataSetChanged()
            }
            Handler().postDelayed(Runnable {
                sw.isRefreshing = false
            }, 4000)

        }

        return view

    }
    fun jsonfetch(){
        var ip= DomainAddress()
        var url=ip.address +"client/client.php?op=messages&id=" + sp.getString("patientid","")
        var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


            var obj1 = response.getJSONArray("results")
            if(obj1.length()==0){

                Toast.makeText(activity,"No messages", Toast.LENGTH_LONG).show()
                rv.isVisible=false
                tvstatus.isVisible=true
            }
            else {
                rv.isVisible=true
                tvstatus.isVisible=false
                messagelist = ArrayList<Messages>()
               // Toast.makeText(activity,obj1.length().toString(), Toast.LENGTH_LONG).show()
                for (i in 0 until obj1.length()) {
                    var app = Messages()
                    app.id = obj1.getJSONObject(i).getString("nid")
                    app.from = obj1.getJSONObject(i).getString("nfrom")
                    app.fromtb = obj1.getJSONObject(i).getString("nfromtable")
                    app.to = obj1.getJSONObject(i).getString("nto")
                    app.totb = obj1.getJSONObject(i).getString("ntotable")
                    app.subject = obj1.getJSONObject(i).getString("nsubject")
                    app.message = obj1.getJSONObject(i).getString("nmessage")
                    app.date = obj1.getJSONObject(i).getString("ndate")
                    app.notified = obj1.getJSONObject(i).getString("notified")
                    var dt = DateFormatter(obj1.getJSONObject(i).getString("ndate"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        app.date = dt.convert_date()
                    } else {
                        app.date = obj1.getJSONObject(i).getString("ndate")
                    }

                    messagelist.add(app)
                }
                adp = MessageAdapter(requireActivity(), messagelist)
                rv.layoutManager = LinearLayoutManager(activity)
                rv.adapter = adp

            }


        }, { error->
            Toast.makeText(activity,error.message, Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(requireActivity()).addToRequestQueue(sr)

    }


}