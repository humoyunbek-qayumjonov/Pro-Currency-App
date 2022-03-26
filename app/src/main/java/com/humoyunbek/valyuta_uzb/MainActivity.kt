package com.humoyunbek.valyuta_uzb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.humoyunbek.valyuta_uzb.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.help_main_layout.*

class MainActivity : AppCompatActivity(),HomeFragment.onSomeEventListener {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navCantroller =findNavController(R.id.nav_host_fragment_activity_main)
        bottom_bar.setupWithNavController(navCantroller)
        bottom_bar.itemIconTintList = null
        binding.navView.setupWithNavController(navCantroller)

        menu_btn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }


        search_btn.setOnClickListener {
            val intent = Intent(this,SearchActivity::class.java)
            startActivity(intent)
        }
        val s = intent.getIntExtra("key1",-1)
        if (s == 4){
            binding.include1.bottomBar.selectedItemId = R.id.calcFragment
        }

    }

    override fun someEvent(s: Int?) {
        if (s==1){
            search_btn.visibility = View.INVISIBLE
        } else if (s ==2){
            search_btn.visibility = View.VISIBLE
        } else if (s == 4){
            binding.include1.bottomBar.selectedItemId = R.id.calcFragment
        }
    }
}