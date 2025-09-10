package com.example.login_page

import android.content.Context
import android.content.Intent
import android.net.Uri // Import Uri for parsing URLs
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import com.example.login_page.databinding.ActivityHomeBinding

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    // Define the YouTube URL you want to open
    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menuButton.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    Toast.makeText(this, "Dashboard clicked", Toast.LENGTH_SHORT).show()
                    // Corrected: Explicitly navigate to the HomePageActivity
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish() // Close the current activity
                }
//                R.id.nav_my_profile -> {
//                    Toast.makeText(this, "My Profile clicked", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                }
                R.id.nav_licenses -> {
                    Toast.makeText(this, "Licenses clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LicensesActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_certificates -> {
                    Toast.makeText(this, "Certificates clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CertificatesActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_policies -> {
                    Toast.makeText(this, "Policies clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Policy::class.java)
                    startActivity(intent)
                }
                R.id.nav_training -> {
                    // FIX: Open YouTube link directly
                    Toast.makeText(this, "Opening Training Video...", Toast.LENGTH_SHORT).show()
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_TRAINING_LINK))
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(this, "Could not open YouTube link.", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }
                R.id.nav_help_support -> {
                    Toast.makeText(this, "Help & Support clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HelpSupportActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_licenses_info -> {
                    Toast.makeText(this, "License info page clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LicenseInfoActivity::class.java)
                    startActivity(intent)
                }
//                R.id.nav_privacy_policies -> {
//                    Toast.makeText(this, "Privacy & Policies clicked", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, PrivacyPoliciesActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.nav_terms_conditions -> {
//                    Toast.makeText(this, "Terms & Conditions clicked", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, TermsConditionsActivity::class.java)
//                    startActivity(intent)
//                }
                R.id.nav_setting -> {
                    Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_log_out -> {
                    // Show the custom logout dialog instead of a Toast
                    showLogoutConfirmationDialog()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
        binding.chatFab.setOnClickListener {
            val intent = Intent(this, ChatBotActivity::class.java)
            startActivity(intent)
        }


//        R.id.chatFab -> {
//            Toast.makeText(this, "this is your chatbot clicked", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ChatBotActivity::class.java)
//            startActivity(intent)
//        }
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

//        findViewById<TextView>(R.id.chatFab).setOnClickListener {
//            Toast.makeText(this, "chatbot clicked", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ChatBotActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun showLogoutConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val yesButton = dialogView.findViewById<Button>(R.id.btn_yes)
        val noButton = dialogView.findViewById<Button>(R.id.btn_no)

        yesButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
            alertDialog.dismiss()
        }

        noButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun attachBaseContext(newBase: Context) {
        try {
            super.attachBaseContext(LocaleHelper.getPersistedLocale(newBase))
        } catch (e: Exception) {
            super.attachBaseContext(newBase)
        }
    }

}