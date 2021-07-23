package com.example.healthscreening.classes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.Debug
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateFormatter {

    var strdate:String=""

    constructor(strdate:String){
        this.strdate=strdate
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun convert_date():String{
        var converted=""
        try {
            val pattern="dd/MM/yyyy"
            var formatter=DateTimeFormatter.ofPattern(pattern)
            var datestr=LocalDate.parse(strdate)
            converted=datestr.format(formatter)


        }
        catch(ex:Exception){
            Log.e("Date Class",ex.message.toString())
        }
        return converted
    }
}