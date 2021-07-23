package com.example.healthscreening

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class ForgotPassword : AppCompatActivity() {
    lateinit var etans1: EditText
    lateinit var etans2: EditText
    lateinit var spsec1: Spinner
    lateinit var spsec2: Spinner
    lateinit var btnsubmit: Button
    lateinit var btncancel: Button
    lateinit var etbruhims:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etans1=findViewById(R.id.etsecurityanswer1)
        etans2=findViewById(R.id.etsecurityanswer2)
        spsec1=findViewById(R.id.spsec1)
        spsec2=findViewById(R.id.spsec2)
        btnsubmit=findViewById(R.id.btnsubmit)
        btncancel=findViewById(R.id.btncancel)
        etbruhims=findViewById(R.id.etbruhims)
        load_spinner()
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
            R.id.btnsubmit->{
                if(etbruhims.text.isEmpty()){
                    etbruhims.error="Enter your BRUHIMS number"
                }
                else if(etans1.text.isEmpty()){
                    etans1.error="Enter your security answer"
                }
                else if(etans2.text.isEmpty()){
                    etans2.error="Enter your security answer"
                }
                else {

                    var content=HashMap<String,String>()
                    content.put("bruhims",etbruhims.text.toString())
                    content.put("q1",spsec1.selectedItem.toString())
                    content.put("q2",spsec2.selectedItem.toString())
                    content.put("a1",etans1.text.toString())
                    content.put("a2",etans2.text.toString())
                    check_security(content)
                }
            }
            R.id.btncancel->{
                finish()
            }
        }
    }

    fun check_security(data:HashMap<String,String>) {
        var param="";
        for(z in data){
            param+="&"+z.key+"="+z.value
        }
            var ip=DomainAddress()
            var url=ip.address +"client/client.php?op=verifysecurity" + param
            var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


                var obj1 = response.getJSONArray("results")
                if(obj1.length()==0){
                    Toast.makeText(this,"Invalid combinations", Toast.LENGTH_LONG).show()

                }
                else {
                    for (i in 0 until obj1.length()) {
                        var id = obj1.getJSONObject(0).getString("clientid").toString()
                        var intent = Intent(this, ResetPassword::class.java)
                        intent.putExtra("patientid", id)
                        startActivity(intent)
                        finish()
                    }
                }


            }, { error->
                Toast.makeText(this,error.message.toString(), Toast.LENGTH_LONG).show()
            })
            MySingleton.getInstance(this).addToRequestQueue(sr)

        }
    }
