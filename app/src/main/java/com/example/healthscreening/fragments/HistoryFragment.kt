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
import com.example.healthscreening.viewadapters.AppoinmentAdapter


class HistoryFragment : Fragment() {
    lateinit var btnbook: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var tvbruhims: TextView
    lateinit var appointmentlist:ArrayList<Appointments>
    lateinit var rv: RecyclerView
    lateinit var sp: SharedPreferences
    lateinit var tvstatus:TextView;
    lateinit var sw:SwipeRefreshLayout;
    lateinit var adp:AppoinmentAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun load_pref(){
        val sharedPreferences =  requireActivity().getSharedPreferences("HEALTH_APP", Context.MODE_PRIVATE)

        // tvbruhims.text=sharedPreferences.getString("patientid","")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_history, container, false)
        sp=requireActivity().getSharedPreferences("HEALTH_APP",Context.MODE_PRIVATE)

        tvbruhims=view.findViewById(R.id.tvbruhims)
        tvbruhims.text=sp.getString("bruhimsid","")
        rv=view.findViewById(R.id.rvappointments)
        tvstatus=view.findViewById(R.id.tvappcount)
        sw=view.findViewById(R.id.swipe_refresh)
        val pd= ProgressDialog(activity)
        pd.setTitle("Appointments")
        pd.setTitle("Fetching Records")
        pd.setIcon(R.drawable.ic_baseline_appointments)
        pd.setCancelable(false)
        pd.show()


        jsonfetch()

        pd.dismiss()

        sw.setOnRefreshListener {
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
        var url=ip.address +"clinic/controls/appointments.php?op=clienthistory&id=" + sp.getString("patientid","")
        var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


            var obj1 = response.getJSONArray("results")
            if(obj1.length()==0){
                Toast.makeText(activity,"No appointments", Toast.LENGTH_LONG).show()
                rv.isVisible=false
                tvstatus.isVisible=true
            }
            else {
                rv.isVisible=true
                tvstatus.isVisible=false
                appointmentlist = ArrayList<Appointments>()
                for (i in 0 until obj1.length()) {
                    var app = Appointments()
                    app.id = obj1.getJSONObject(i).getString("appointmenid")
                    app.clientid = obj1.getJSONObject(i).getString("clinicid")
                    app.clinic = obj1.getJSONObject(i).getString("cname")
                    app.clientid = obj1.getJSONObject(i).getString("clientid")
                    app.doctorid = obj1.getJSONObject(i).getString("doctorid")
                    app.doctor = obj1.getJSONObject(i).getString("dname")
                    var dt = DateFormatter(obj1.getJSONObject(i).getString("appdate"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        app.appdate = dt.convert_date()
                    } else {
                        app.appdate = obj1.getJSONObject(i).getString("appdate")
                    }

                    app.apptime = obj1.getJSONObject(i).getString("apptime")
                    app.apptype = obj1.getJSONObject(i).getString("apptype")
                    app.appdetails = obj1.getJSONObject(i).getString("appdetails")
                    app.status = obj1.getJSONObject(i).getString("status")
                    app.bookingdate = obj1.getJSONObject(i).getString("bookingdate")
                    appointmentlist.add(app)
                }
                adp = AppoinmentAdapter(requireActivity(), appointmentlist)
                rv.layoutManager = LinearLayoutManager(activity)
                rv.adapter = adp

            }


        }, { error->
            Toast.makeText(activity,error.message, Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(requireActivity()).addToRequestQueue(sr)

    }
}