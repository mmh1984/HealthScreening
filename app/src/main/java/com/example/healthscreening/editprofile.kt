package com.example.healthscreening

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*

class editprofile : AppCompatActivity(),DatePickerDialog.OnDateSetListener {
    var domainip=DomainAddress()
    var result:String=""
    var spin: Spinner? =null
    lateinit var etdob: EditText
    lateinit var btncancel: Button
    lateinit var etpassword: TextView
    lateinit var id:String;
    lateinit var no:EditText;
    lateinit var name:EditText;
    lateinit var ic:EditText;
    lateinit var phone:EditText;
    lateinit var email:EditText;
    lateinit var address:EditText;

    var day=0
    var month=0
    var year=0
    var savedday=0
    var savedmonth=0
    var savedyear=0
    var dateselected=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
        id=intent.getStringExtra("id").toString();
        btncancel=findViewById(R.id.btncancel)
        spin=findViewById(R.id.spreggender)
        no=findViewById(R.id.etregno)
        name=findViewById(R.id.etregname)
        ic=findViewById(R.id.etregic)
        phone=findViewById(R.id.etregphone)
        email=findViewById(R.id.etregemail)
        address=findViewById(R.id.etaddress);
        load_details();
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
            finish()

        }

    }
    fun load_details(){
        try {


            var ip= DomainAddress()
            var url=ip.address +"client/client.php?op=clientdetails&id=" + id
            var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


                var obj1 = response.getJSONArray("results")
                if(obj1.length()==0){
                    Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                }
                else {

                    for (i in 0 until obj1.length()) {
                        name.setText(obj1.getJSONObject(i).getString("cname").toString())
                        no.setText(obj1.getJSONObject(i).getString("cbruhims"))
                        ic.setText(obj1.getJSONObject(i).getString("cic") + "/" + obj1.getJSONObject(i).getString("cpassport"))
                        email.setText(obj1.getJSONObject(i).getString("cemail"))
                        phone.setText(obj1.getJSONObject(i).getString("cphone"))
                        etdob.setText(obj1.getJSONObject(i).getString("cdob"))
                        etpassword.setText(obj1.getJSONObject(i).getString("cpassword"))

                    }


                }


            }, { error->
                Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
            })
            MySingleton.getInstance(this).addToRequestQueue(sr)
        }
        catch (ex:Exception){
            Toast.makeText(this,ex.message.toString(),Toast.LENGTH_LONG).show()
        }
    }



    fun show_dialog(title:String,message:String){
        val ad= AlertDialog.Builder(this)
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
    fun validate_client(view: View){

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
        else if (address.text.isEmpty()){
            address.error="Enter your address"
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
            content.put("address",address.text.toString())
            content.put("password",etpassword.text.toString())


            update_client(content)

        }

    }
    fun update_client(data:HashMap<String,String>){
        try{
            var param="";
            for(z in data){
                param+="&"+z.key+"="+z.value
            }
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Clinic")
            progressDialog.setMessage("Processing request")
            progressDialog.show()

            var url:String=domainip.address + "client/client.php?op=update" + param +"&id=" + id
            Toast.makeText(this,url,Toast.LENGTH_LONG).show()
            var sr= JsonObjectRequest(Request.Method.GET,url,null, { response ->

                when(response.getJSONArray("results").getString(0).toString()){
                    "OK"->{
                        progressDialog.dismiss()
                        show_dialog("Client","Update Complete")

                        finish()
                        //startActivity(Intent(this,ClinicLogin::class.java))
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
        catch (ex: Exception){
            Toast.makeText(this,ex.message,Toast.LENGTH_LONG).show()
        }
    }
    private fun getDateTimeCalendar(){
        val cal= Calendar.getInstance()
        day=cal.get(Calendar.DAY_OF_MONTH)
        month=cal.get(Calendar.MONTH)
        year=cal.get(Calendar.YEAR)

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