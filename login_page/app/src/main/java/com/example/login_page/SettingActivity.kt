package com.example.login_page

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class SettingActivity : AppCompatActivity() {


    // Define the YouTube URL you want to open
    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"
    // Declare all the views from your XML layout

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var menuButton: ImageButton
    private lateinit var btnLogout: TextView // Using TextView for logout as a simple example

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure you use the correct layout file name here
        setContentView(R.layout.activity_setting)

        // Initialize the views by finding them by their IDs
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        menuButton = findViewById(R.id.menuButton)
        btnLogout = findViewById(R.id.btn_logout)

        // Set a click listener for the menu button to open the navigation drawer
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


// Set a listener for the navigation drawer menu item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    Toast.makeText(this, "Dashboard clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_licenses_info -> {
                    Toast.makeText(this, "Licenses info clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LicenseInfoActivity::class.java)
                    startActivity(intent)
                }
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

                R.id.nav_setting -> {
                    Toast.makeText(this, "Already on Settings", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_log_out -> {
                    Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Set a listener for the "Log Out" button
        btnLogout.setOnClickListener {
            // Replace this with your actual logout logic
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
        }

        // Set listeners for other clickable TextViews in the settings list
        findViewById<TextView>(R.id.tv_edit_profile).setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_change_password).setOnClickListener {
            Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_linked_documents).setOnClickListener {
            Toast.makeText(this, "Linked Documents clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_terms_conditions).setOnClickListener {
            Toast.makeText(this, "Terms & Conditions clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TermsConditionsActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tv_privacy_policy).setOnClickListener {
            Toast.makeText(this, "Privacy Policy clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PrivacyPoliciesActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.tv_about).setOnClickListener {
            Toast.makeText(this, "About Aapka Sathi clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_faqs).setOnClickListener {
            Toast.makeText(this, "FAQs clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_contact_support).setOnClickListener {
            Toast.makeText(this, "Contact Support clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_request_callback).setOnClickListener {
            Toast.makeText(this, "Request a Callback clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_report_problem).setOnClickListener {
            Toast.makeText(this, "Report a Problem clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_check_for_update).setOnClickListener {
            Toast.makeText(this, "Check for update clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tv_feedback_rate_us).setOnClickListener {
            Toast.makeText(this, "Feedback/Rate us clicked", Toast.LENGTH_SHORT).show()
        }
    }

    // Override onBackPressed to close the drawer if it's open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}