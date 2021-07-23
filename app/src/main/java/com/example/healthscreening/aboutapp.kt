package com.example.healthscreening

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class aboutapp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutapp)
    }

    fun back(view: View){
        startActivity(Intent(this,ClinicLogin::class.java));
        finish();
    }
}