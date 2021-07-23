package com.example.healthscreening.fragments.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.healthscreening.fragments.AppointmentFragment
import com.example.healthscreening.fragments.HistoryFragment
import com.example.healthscreening.fragments.MessageFragment
import com.example.healthscreening.fragments.ProfileFragment
@Suppress("DEPRECATION")
class ViewPagerAdapter(private val mycontext: Context, fm: FragmentManager, var totaltabs:Int):FragmentPagerAdapter(fm)

{
    override fun getCount(): Int {
        return totaltabs;
    }

    override fun getItem(position: Int): Fragment {

        return when (position){
        0->{
            return AppointmentFragment();

        }
        1-> {
            return HistoryFragment();
        }
        2-> {
            return ProfileFragment();
        }
            3-> {
                return MessageFragment();
            }
        else -> getItem(position)
        }


    }


}