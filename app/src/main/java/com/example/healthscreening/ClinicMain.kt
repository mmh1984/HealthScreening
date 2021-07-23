package com.example.healthscreening

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.TableLayout
import androidx.viewpager.widget.ViewPager
import com.example.healthscreening.fragments.AppointmentFragment
import com.example.healthscreening.fragments.HistoryFragment
import com.example.healthscreening.fragments.ProfileFragment
import com.example.healthscreening.fragments.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class ClinicMain : AppCompatActivity() {

    lateinit var tabLayout:TabLayout
    lateinit var viewPager:ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_main)


        setupTabs();


    }
private fun setupTabs(){
    tabLayout=findViewById(R.id.tab_layout);
    viewPager=findViewById(R.id.view_pager);

    tabLayout.addTab(tabLayout.newTab().setText("Appointments"))
    tabLayout.addTab(tabLayout.newTab().setText("History"))
    tabLayout.addTab(tabLayout.newTab().setText("Profile"))
    tabLayout.addTab(tabLayout.newTab().setText("Messages"))
    tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_appointments)
    tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_history_24)
    tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_account_circle_24)
    tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_baseline_message_24)

    val adapter=ViewPagerAdapter(this,supportFragmentManager,tabLayout.tabCount)
    viewPager.adapter=adapter

    viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewPager!!.currentItem=tab.position
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            viewPager!!.currentItem=tab.position
        }


    })
}


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.clinic_menu,menu)
        return true

    }
}