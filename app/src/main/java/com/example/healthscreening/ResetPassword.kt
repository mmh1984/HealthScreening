package com.example.healthscreening

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class ResetPassword : AppCompatActivity() {
    lateinit var patientid:String;
    lateinit var txtpass:EditText;
    lateinit var txtconfirm:EditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        patientid=intent.getStringExtra("patientid").toString()
        txtpass=findViewById(R.id.etpassword)
        txtconfirm=findViewById(R.id.etconfirmpassword)

    }

    fun button_click(view: View){
        var button=view as Button
        when(button.id){
            R.id.btnsubmit->{
                if(txtpass.text.isEmpty() || txtpass.text.length < 8){
                    Toast.makeText(this,"Password must be at least 8 characters",Toast.LENGTH_LONG).show()
                }
                else if(txtconfirm.text.isEmpty() || txtconfirm.text.length < 8){
                    Toast.makeText(this,"Password must be at least 8 characters",Toast.LENGTH_LONG).show()
                }
                else if(!(txtpass.text.toString()==txtconfirm.text.toString())){
                    Toast.makeText(this,"Password(s) didn't match",Toast.LENGTH_LONG).show()
                }
                else {
                    var content=HashMap<String,String>()
                    content.put("password",txtpass.text.toString())
                    content.put("patientid",patientid)
                    reset_password(content);

                }
            }
            R.id.btncancel->{
                startActivity(Intent(this,ClinicLogin::class.java))
                finish()
            }
        }
    }

    fun reset_password(data:HashMap<String,String>) {
        try {
            var domainip=DomainAddress();
            var param="";
            for(z in data){
                param+="&"+z.key+"="+z.value
            }
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Clinic")
            progressDialog.setMessage("Processing request")
            progressDialog.show()

            var url:String=domainip.address + "client/client.php?op=resetpass" + param

            var sr= JsonObjectRequest(Request.Method.GET,url,null, { response ->

                when(response.getJSONArray("results").getString(0).toString()){
                    "OK"->{
                        progressDialog.dismiss()
                        Toast.makeText(this,"Password Reset",Toast.LENGTH_LONG).show()

                        finish()
                        startActivity(Intent(this,ClinicLogin::class.java))
                    }
                    else->{
                       Toast.makeText(this,response.getJSONArray("results").getString(0).toString(),Toast.LENGTH_LONG).show()
                    }
                }


            }, { error->
                Toast.makeText(this,error.message.toString(),Toast.LENGTH_LONG).show()
            })

            MySingleton.getInstance(this).addToRequestQueue(sr)
            progressDialog.dismiss()

        }
        catch (ex:Exception){
            Toast.makeText(this,ex.message.toString(),Toast.LENGTH_LONG).show()
        }
    }
}