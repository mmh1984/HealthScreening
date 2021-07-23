package com.example.healthscreening

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.classes.DateFormatter

@Suppress("DEPRECATION")
class SecurityQuestion : AppCompatActivity() {
    lateinit var tvsec1:TextView
    lateinit var tvsec2:TextView
    lateinit var etans1:EditText
    lateinit var etans2:EditText
    lateinit var spsec1:Spinner
    lateinit var spsec2:Spinner
    lateinit var btnupdate:Button
    lateinit var btncancel:Button
    var patientid="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_question)

        tvsec1=findViewById(R.id.tvsecurity1)
        tvsec2=findViewById(R.id.tvsecurity2)
        etans1=findViewById(R.id.etsecurityanswer1)
        etans2=findViewById(R.id.etsecurityanswer2)
        spsec1=findViewById(R.id.spsec1)
        spsec2=findViewById(R.id.spsec2)
        btnupdate=findViewById(R.id.btnupdatesecurity)
        btncancel=findViewById(R.id.btncancel)

        load_spinner()
        var sp=getSharedPreferences("HEALTH_APP", Context.MODE_PRIVATE)
        patientid=sp.getString("patientid","").toString()
        jsonfetch(patientid)
    }
    fun jsonfetch(id:String){
        var ip=DomainAddress()
        var url=ip.address +"client/client.php?op=securityquestions&id=" + patientid
        var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


            var obj1 = response.getJSONArray("results")
            if(obj1.length()==0){
                Toast.makeText(this,"No security set",Toast.LENGTH_LONG).show()

            }
            else {

                for (i in 0 until obj1.length()) {
                    var sec1=obj1.getJSONObject(i).getString("securityquestion")
                    var sec2=obj1.getJSONObject(i).getString("securityquestion2")
                    var ans1=obj1.getJSONObject(i).getString("securityanswer")
                    var ans2=obj1.getJSONObject(i).getString("securityanswer2")

                    if(obj1.getJSONObject(i).isNull("securityquestion2")){
                        tvsec2.text="Not Set"

                    }
                    else{
                        tvsec2.text=sec2.toString()
                        etans2.setText(ans2)
                    }
                    if(obj1.getJSONObject(i).isNull("securityquestion")){
                        tvsec1.text="Not Set"

                    }
                    else{
                        tvsec1.text=sec1.toString()
                        etans1.setText(ans1)
                    }



                }


            }


        }, { error->
            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(this).addToRequestQueue(sr)

    }
    fun load_spinner(){
        ArrayAdapter.createFromResource(
            this,
        R.array.security_question,
        android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spsec1.adapter=adapter
            spsec2.adapter=adapter
        }
    }

    fun button_click(view: View){
        var button=view as Button
        when(button.id){
            R.id.btnupdatesecurity->{
               update_security()
            }
            R.id.btncancel->{
                finish()
            }
        }
    }
    fun update_security(){
        var sec1=""
        var sec2=""
        var ans1=""
        var ans2=""
        if(etans1.text.toString().isEmpty()){
            etans1.error="Enter your answer"
        }
        else if(spsec1.selectedItem.toString().equals(spsec2.selectedItem.toString())){
            var ad=AlertDialog.Builder(this)
            ad.setTitle("Security Questions")
            ad.setMessage("Security questions must be different")
            ad.setPositiveButton("OK"){dialog,which->}
            ad.show()
        }
        else if(etans2.text.toString().isEmpty()){
            etans2.error="Enter your answer"
        }
        else{
            sec1=spsec1.selectedItem.toString()
            sec2=spsec2.selectedItem.toString()
            ans1=etans1.text.toString()
            ans2=etans2.text.toString()
            var content=HashMap<String,String>()
            content.put("q1",sec1)
            content.put("q2",sec2)
            content.put("a1",ans1)
            content.put("a2",ans2)
            var pd=ProgressDialog(this)
            pd.setTitle("Security Questions")
            pd.setMessage("Saving.. ")
            pd.show()
            jsonfetch2(content)
            pd.dismiss()
        }

    }

    fun jsonfetch2(data:HashMap<String,String>){
        var param="";
        for(z in data){
            param+="&"+z.key+"="+z.value
        }
        var ip=DomainAddress()
        var url=ip.address +"client/client.php?op=updatesecurity$param&id=" + patientid
        var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


            var obj1 = response.getJSONArray("results")
            if(obj1.length()==0){
                Toast.makeText(this,"No security set",Toast.LENGTH_LONG).show()

            }
            else {

               when(obj1.getString(0).toString()){
                   "OK"->{
                       Toast.makeText(this,"Security questions set",Toast.LENGTH_LONG).show()
                       finish()
                   }
                   else->{
                       Toast.makeText(this,obj1.getString(0).toString(),Toast.LENGTH_LONG).show()
                   }
               }


            }


        }, { error->
            Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(this).addToRequestQueue(sr)

    }
}