package com.example.login_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.text.HtmlCompat // Import for HTML rendering
import com.example.login_page.databinding.ActivityTermsConditionsBinding // Ensure this binding is generated

class TermsConditionsActivity : AppCompatActivity() {

    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"

    private lateinit var binding: ActivityTermsConditionsBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // --- Set the Terms & Conditions Content with HTML Rendering ---
        // You will define the actual content in your strings.xml file under the name "terms_conditions_content"
        val termsConditionsHtml = getString(R.string.terms_conditions_content)
        binding.tvTermsConditionsContent.text = HtmlCompat.fromHtml(termsConditionsHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)


        // --- Handle Top AppBar Menu Button to OPEN/CLOSE THE DRAWER ---
        binding.menuButton.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // --- Set up navigation item clicks for THIS activity's drawer ---
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    Toast.makeText(this, "Dashboard clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish() // Close this activity to avoid deep back stack
                }
//                R.id.nav_my_profile -> {
//                    Toast.makeText(this, "My Profile clicked", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                }
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
                    val intent = Intent(this, Policy::class.java) // Assuming Policy is your main Policy page
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
//                R.id.nav_privacy_policies -> {
//                    Toast.makeText(this, "Privacy & Policies clicked", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, PrivacyPoliciesActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.nav_terms_conditions -> {
//                    Toast.makeText(this, "Already on Terms & Conditions", Toast.LENGTH_SHORT).show()
//                    // No need to launch new activity, already on this screen
//                }
                R.id.nav_setting -> {
                    Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_log_out -> {
                    Toast.makeText(this, "Log Out clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after selection
            true
        }

        // Handle back press to close drawer first, then exit activity
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false // Disable this callback to allow default behavior
                    onBackPressedDispatcher.onBackPressed() // Perform default back (exit activity)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        // Chat FAB click listener
        binding.chatFab.setOnClickListener {
            Toast.makeText(this, "Chat FAB clicked!", Toast.LENGTH_SHORT).show()
            // Add your chat functionality here, e.g., launch a chat activity
        }
    }

    // This function seems to be part of your base activity setup for LocaleHelper
    override fun attachBaseContext(newBase: Context) {
        try {
            super.attachBaseContext(LocaleHelper.getPersistedLocale(newBase))
        } catch (e: Exception) {
            super.attachBaseContext(newBase)
        }
    }
}