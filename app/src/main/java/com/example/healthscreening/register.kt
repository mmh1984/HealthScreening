package com.example.healthscreening

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.DateTimeKeyListener
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.lang.Error
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
class register : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    var domainip=DomainAddress()
    var result:String=""
    var spin: Spinner? =null
    lateinit var etdob:EditText
    lateinit var btncancel:Button
    lateinit var etpassword:TextView

    var day=0
    var month=0
    var year=0
    var savedday=0
    var savedmonth=0
    var savedyear=0
    var dateselected=""
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btncancel=findViewById(R.id.btncancel)
        spin=findViewById(R.id.spreggender)
       ArrayAdapter.createFromResource(
               this,
               R.array.gender_array,
               android.R.layout.simple_spinner_item
       ).also { adapter->
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
           spin?.adapter=adapter
       }
                etdob=findViewById(R.id.etdob)
                etdob.setOnClickListener{
                    getDateTimeCalendar()
                    DatePickerDialog(this,this,day,month,year).show()

                }
                etpassword=findViewById(R.id.etpassword)

        btncancel.setOnClickListener {
            startActivity(Intent(this,ClinicLogin::class.java))
            finish()

        }

    }
    private fun getDateTimeCalendar(){
        val cal= Calendar.getInstance()
        day=cal.get(Calendar.DAY_OF_MONTH)
        month=cal.get(Calendar.MONTH)
        year=cal.get(Calendar.YEAR)

    }
    fun register_client(view: View){

        var no:EditText=findViewById(R.id.etregno)
        var name:EditText=findViewById(R.id.etregname)
        var ic:EditText=findViewById(R.id.etregic)
        var phone:EditText=findViewById(R.id.etregphone)
        var email:EditText=findViewById(R.id.etregemail)


        if(no.text.isEmpty() ){
            no.error="Invalid BRUHIMS number"
        }
        else if(name.text.isEmpty()){
            name.error="Please enter your full name"
        }
        else if(ic.text.isEmpty()){
            ic.error="Please enter your IC/Passport number"
        }
        else if (email.text.isEmpty()){
            email.error="Please enter your email"
        }
        else if (phone.text.isEmpty()){
            phone.error="Please enter your phone numer"
        }
        else if (dateselected.isEmpty()){
            etdob.error="Select your date of birth"
        }
        else if (etpassword.text.length<8){
            etpassword.error="Password must be al least 8 characters"
        }
        else{

              var content=HashMap<String,String>();
                content.put("no",no.text.toString())
                content.put("name",name.text.toString())
                content.put("ic",ic.text.toString())
                content.put("email",email.text.toString())
                content.put("phone",phone.text.toString())
                content.put("gender",spin?.selectedItem.toString())
                content.put("dob",dateselected)
                content.put("password",etpassword.text.toString())

                save_client(content)

            }

        }


    fun save_client(data:HashMap<String,String>){
        try{
            var param="";
            for(z in data){
               param+="&"+z.key+"="+z.value
            }
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Clinic")
            progressDialog.setMessage("Processing request")
            progressDialog.show()

            var url:String=domainip.address + "client/client.php?op=save" + param

            var sr=JsonObjectRequest(Request.Method.GET,url,null, { response ->

            when(response.getJSONArray("results").getString(0).toString()){
                "OK"->{
                    progressDialog.dismiss()
                    show_dialog("Client","Registration Completed")

                    finish()
                    startActivity(Intent(this,ClinicLogin::class.java))
                }
                else->{
                    show_dialog("Client",response.getJSONArray("results").getString(0).toString())
                }
            }


            }, { error->
                show_dialog("Error","ERROR" + error.message.toString())
            })

            MySingleton.getInstance(this).addToRequestQueue(sr)
            progressDialog.dismiss()

        }
        catch (ex:Exception){
            Toast.makeText(this,ex.message,Toast.LENGTH_LONG).show()
        }
    }

    fun show_dialog(title:String,message:String){
        val ad=AlertDialog.Builder(this)
        ad.setTitle(title)
        ad.setMessage(message)
        if(title=="error"){
            ad.setIcon(android.R.drawable.ic_dialog_alert)
        }
        else {
            ad.setIcon(android.R.drawable.ic_dialog_info)
        }
        ad.setPositiveButton("OK"){dialog,which ->}

        ad.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedday=dayOfMonth
        savedmonth=month+1
        savedyear=year
        when(savedmonth.toString().length){
            1->{
                dateselected="$savedday/0$savedmonth/$savedyear"
            }
            2->{
                dateselected="$savedday/$savedmonth/$savedyear"
            }
        }

        etdob.setText("$savedday/$savedmonth/$savedyear")
    }
}

