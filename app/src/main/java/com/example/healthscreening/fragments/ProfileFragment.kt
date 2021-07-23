package com.example.healthscreening.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.healthscreening.*
import com.example.healthscreening.classes.DateFormatter
import com.squareup.picasso.Picasso
import java.lang.Exception


class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var tvpatient:TextView
    lateinit var tvbruhims:TextView
    lateinit var tvicpassport:TextView
    lateinit var tvemail:TextView
    lateinit var tvphone:TextView
    lateinit var tvgender:TextView
    lateinit var tvaddress:TextView
    lateinit var tvdob:TextView
    lateinit var ivimg:ImageView
    lateinit var btnlogout: Button
    lateinit var btnsecurity:Button
    lateinit var btneditprofile:Button
    var pickImage:Int=100;
    var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_profile, container, false)

        tvpatient=view.findViewById(R.id.clientname)
        tvbruhims=view.findViewById(R.id.tvbruhims)
        tvicpassport=view.findViewById(R.id.tvic)
        tvemail=view.findViewById(R.id.tvemail)
        tvphone=view.findViewById(R.id.tvphone)
        tvgender=view.findViewById(R.id.tvgender)
        tvaddress=view.findViewById(R.id.tvaddress)
        tvdob=view.findViewById(R.id.tvdob)
        ivimg=view.findViewById(R.id.ivprofile)
        btnlogout=view.findViewById(R.id.btnlogout)
        btnsecurity=view.findViewById(R.id.btnsecurityqs)
        btneditprofile=view.findViewById(R.id.btneditprofile)
        var sp=requireActivity().getSharedPreferences("HEALTH_APP", Context.MODE_PRIVATE)
        var id=sp.getString("patientid","")
        jsonfetch(id.toString())

        ivimg.setOnClickListener{
            change_image()
        }
        btnlogout.setOnClickListener{
            var intent=Intent(activity,ClinicLogin::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        btnsecurity.setOnClickListener{
            var intent=Intent(activity,SecurityQuestion::class.java)
            startActivity(intent)

        }
        btneditprofile.setOnClickListener {
            var intent=Intent(activity,editprofile::class.java)
            intent.putExtra("id",id);
            startActivity(intent)
        }

        return view

    }
    fun jsonfetch(id:String){
        try {


        var ip= DomainAddress()
        var url=ip.address +"client/client.php?op=clientdetails&id=" + id
        var sr= JsonObjectRequest(Request.Method.GET,url,null, { response->


            var obj1 = response.getJSONArray("results")
            if(obj1.length()==0){
                Toast.makeText(activity,"Error", Toast.LENGTH_LONG).show()
            }
            else {

                for (i in 0 until obj1.length()) {
                    tvpatient.text=obj1.getJSONObject(i).getString("cname")
                    tvbruhims.text=obj1.getJSONObject(i).getString("cbruhims")
                    tvicpassport.text=obj1.getJSONObject(i).getString("cic") + "/" + obj1.getJSONObject(i).getString("cpassport")
                    tvemail.text=obj1.getJSONObject(i).getString("cemail")
                    tvphone.text=obj1.getJSONObject(i).getString("cphone")
                    tvgender.text=obj1.getJSONObject(i).getString("cgender")
                    tvdob.text=obj1.getJSONObject(i).getString("cdob")
                    var imgurl=ip.address + "client/" +obj1.getJSONObject(i).getString("cimg")
                    Picasso.get().load(imgurl).into(ivimg)
                }


            }


        }, { error->
            Toast.makeText(activity,error.message, Toast.LENGTH_LONG).show()
        })
        MySingleton.getInstance(requireActivity()).addToRequestQueue(sr)
        }
        catch (ex:Exception){
            Toast.makeText(activity,ex.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun change_image(){
        try{
            val gallery= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,pickImage)
        }

        catch (ex:Exception){
            Toast.makeText(activity,ex.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK && requestCode==pickImage){
            imgUri=data?.data
            ivimg.setImageURI(imgUri)
        }
    }


}