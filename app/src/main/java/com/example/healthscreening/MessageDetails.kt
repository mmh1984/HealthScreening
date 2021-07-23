package com.example.healthscreening

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.classes.DateFormatter
import java.time.LocalDate

@Suppress("DEPRECATION")
class MessageDetails : AppCompatActivity() {
    lateinit var id:String;
    lateinit var pd:ProgressDialog;
    lateinit var subject:TextView;
    lateinit var datesent:TextView;
    lateinit var from:TextView;
    lateinit var messagecontent:TextView;
    lateinit var btnback: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_details)

        subject=findViewById(R.id.tvsubject);
        datesent=findViewById(R.id.tvdate);
        from=findViewById(R.id.tvfrom);
        messagecontent=findViewById(R.id.tvmessage);
        btnback=findViewById(R.id.btnback);
        btnback.setOnClickListener {

            update_message();
        }

        id=intent.getStringExtra("id").toString();
        pd= ProgressDialog(this)
        pd.setTitle("Messages");
        pd.setMessage("Loading..")
        pd.setCancelable(false)
        pd.show();
        jsonfetch();

    }

    fun jsonfetch(){
        var ip=DomainAddress()
        var url=ip.address +"clinic/controls/messages.php?op=message&id=" + id
        var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


            var obj1 = response.getJSONArray("results")
                if(obj1.getJSONObject(0).toString()=="ERROR"){
                    Toast.makeText(this,"Cannot connect to server",Toast.LENGTH_LONG).show();
                }
                else {
                    for (i in 0 until obj1.length()) {
                        subject.text=obj1.getJSONObject(i).getString("nsubject");
                        datesent.text=obj1.getJSONObject(i).getString("ndate");
                        from.text=obj1.getJSONObject(i).getString("cname");
                        messagecontent.text=obj1.getJSONObject(i).getString("nmessage")
                    }
                }
pd.dismiss()
        }, { error->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(this).addToRequestQueue(sr)

    }

    fun update_message(){
        var ip=DomainAddress()
        var url=ip.address +"clinic/controls/messages.php?op=read&id=" + id
        var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->

            var obj1 = response.getJSONArray("results")
            if(obj1.getJSONObject(0).toString()=="ERROR"){
                Toast.makeText(this,"Cannot connect to server",Toast.LENGTH_LONG).show();
            }
            else {
                finish();
            }
            pd.dismiss()
        }, { error->
            Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(this).addToRequestQueue(sr)

    }
}


