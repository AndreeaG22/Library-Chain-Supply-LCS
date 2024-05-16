package com.teamapp.lcs

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.teamapp.client_management.ClientManagementFragment
import com.teamapp.employee_management.EmployeeManagementFragment
import com.teamapp.home.HomeFragment
import com.teamapp.lcs.databinding.ActivityMainBinding
import com.teamapp.receipt_history.ReceiptHistoryFragment

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getArguments()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        initDrawer(binding)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }

    private fun getArguments() {
        email = intent.getStringExtra("email") ?: ""
    }

    private fun initDrawer(binding: ActivityMainBinding) {
        drawerLayout = findViewById(R.id.main)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarTitle.text = "Acasa"
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()

        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nav_text).text = email

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    binding.toolbarTitle.text = "Acasa"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }
                R.id.manage_workers -> {
                    binding.toolbarTitle.text = "Gestionare angajati"
                        supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, EmployeeManagementFragment())
                        .commit()
                }
                R.id.manage_clients -> {
                    binding.toolbarTitle.text = "Gestionare clienti"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ClientManagementFragment())
                        .commit()
                }
                R.id.receipt_history -> {
                    binding.toolbarTitle.text = "Istoric facturi"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ReceiptHistoryFragment())
                        .commit()
                }
                R.id.logout -> {
//                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }
}
