package com.example.healthscreening

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
@Suppress("DEPRECATION")

class ClinicLogin : AppCompatActivity() {
    var domainip=DomainAddress()
    lateinit var tvforgotpass:TextView
    lateinit var ivinfo:ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_login)
        var checknetwork=NetworkStatus()
        when (checknetwork.isOnline(this)){
            true-> {
                Toast.makeText(this, "Network Connected", Toast.LENGTH_SHORT).show()
            }
            false-> {
                Toast.makeText(this, "Cannot detect network", Toast.LENGTH_SHORT).show()
                finish();
            }
            else -> {
                Toast.makeText(this, "Cannot detect network", Toast.LENGTH_SHORT).show()
                finish()
            }

        }
        ivinfo=findViewById(R.id.ivinfo);
        tvforgotpass=findViewById(R.id.tvforgotpassword)
        tvforgotpass.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
        }

    }
    fun show_info(view:View){
        startActivity(Intent(this,aboutapp::class.java));
    }

    fun button_click(view:View){
        var btn=view as Button
        when(btn.id){
            R.id.btnlogin -> {
                login()
            }
            R.id.btnregister->{
                var i=Intent(this,register::class.java);
                startActivity(i);
                finish()
            }
        }
    }

    fun login(){
        try{
            var etno:EditText=findViewById(R.id.etclinicno)
            var etpass:EditText=findViewById(R.id.etclinicpassword)

            if (etno.text.isEmpty()){
                etno.error="Enter clinic registration number"
            }
            else if(etpass.text.isEmpty()){
                etpass.error="Enter clinic password"
            }
            else{
                var clinicno=etno.text
                var clinicpass=etpass.text

                val progress = ProgressDialog(this)
                progress.setTitle("Clinic Login")
                progress.setMessage("Logging in")
                progress.setCancelable(false) // disable dismiss by tapping outside of the dialog

                progress.show()
// To dismiss the dialog
// To dismiss the dialog

                var url:String=domainip.address + "client/client.php?op=login&bruhims=$clinicno&password=$clinicpass"
                var rq:RequestQueue=Volley.newRequestQueue(this)
                //Toast.makeText(this, url.toString(), Toast.LENGTH_LONG).show()
                var sr=StringRequest(Request.Method.GET, url, { response ->
                    //Toast.makeText(this,response,Toast.LENGTH_LONG).show();
                    when(response){

                        "NONE"->{
                            message("Invalid username/password")
                        }
                        else->{
                            initialize_pref(clinicno.toString(),response.toString())
                            var i= Intent(this,ClinicMain::class.java)
                            i.putExtra("id",clinicno)
                            startActivity(i)
                            finish();
                        }

                    }

                }, { error ->
                    message("Error ${error.message}")

                })
                rq.add(sr)
                progress.dismiss()
            }


        }
        catch (ex: Exception){
            message(ex.message.toString())

        }


    }

    fun initialize_pref(bruhims:String,patientid:String){
        var sp =  getSharedPreferences("HEALTH_APP", Context.MODE_PRIVATE)
        var editor = sp.edit()
        editor.putString("bruhimsid",bruhims)
        editor.putString("patientid",patientid)
        editor.commit()
    }

    fun message(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}