package com.example.dashboardmobileapp.Activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dashboardmobileapp.R
import com.example.dashboardmobileapp.databinding.ActivityProfileBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private var name: TextView? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var topToolView: MaterialToolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initView()
        setButtons()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ProfileActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun initView(){
        bottomNavigationView = findViewById(R.id.bottomNavigationViewProfile)
        topToolView = findViewById(R.id.materialToolbarProfile)

        name = findViewById(R.id.profilIcon)
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val nameObject = preferences.getString("username","")
        val firstChar = nameObject?.substring(0, 1)?.uppercase(Locale.getDefault())
        val secondChar = nameObject?.substring(1,2)?.lowercase(Locale.getDefault())
        val result = firstChar + secondChar
        name!!.text = "$result"


    }

    fun setButtons(){
        bottomNavigationView?.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.incomeItem -> {
                    startActivity(
                        Intent(
                            this@ProfileActivity,
                            MainActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@ProfileActivity).toBundle()
                    )
                }

                R.id.homeItem -> {
                    startActivity(
                        Intent(
                            this@ProfileActivity,
                            MainActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@ProfileActivity).toBundle()
                    )
                }
                R.id.addItem -> {
                    startActivity(
                        Intent(
                            this@ProfileActivity,
                            SearchActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@ProfileActivity).toBundle()
                    )
                }

                R.id.expenseItem -> {
                    startActivity(
                        Intent(
                            this@ProfileActivity,
                            ExpenseActivity::class.java
                        ), ActivityOptions.makeSceneTransitionAnimation(this@ProfileActivity).toBundle()
                    )
                }
            }
            true

        }
        topToolView?.setOnMenuItemClickListener{item ->
            when(item.itemId){
                R.id.logoutItem -> {
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.remove("token")
                    editor.remove("username")
                    editor.apply()
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
    }
}