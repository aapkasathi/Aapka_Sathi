package com.example.login_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.login_page.databinding.ActivityTrainingBinding // Ensure this binding is generated

class TrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingBinding
    private lateinit var pickTrainingImage1Launcher: ActivityResultLauncher<String>
    private lateinit var pickTrainingImage2Launcher: ActivityResultLauncher<String>
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup image pickers for each training image
        setupImagePicker(binding.trainingImage1) { launcher ->
            pickTrainingImage1Launcher = launcher
        }
        setupImagePicker(binding.trainingImage2) { launcher ->
            pickTrainingImage2Launcher = launcher
        }

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
                    Toast.makeText(this, "Already on Training", Toast.LENGTH_SHORT).show()
                    // No need to launch new activity, already on this screen
                }
                R.id.nav_help_support -> {
                    Toast.makeText(this, "Help & Support clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HelpSupportActivity::class.java) // Assuming HelpSupportActivity exists
                    startActivity(intent)
                }
//                R.id.nav_privacy_policies -> {
//                    Toast.makeText(this, "Privacy & Policies clicked", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, PrivacyPoliciesActivity::class.java) // Assuming PrivacyPoliciesActivity exists
//                    startActivity(intent)
//                }
//                R.id.nav_terms_conditions -> {
//                    Toast.makeText(this, "Terms & Conditions clicked", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, TermsConditionsActivity::class.java) // Assuming TermsConditionsActivity exists
//                    startActivity(intent)
//                }
//                R.id.nav_setting -> {
//                    Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, SettingActivity::class.java) // Assuming SettingActivity exists
//                    startActivity(intent)
//                }
                R.id.nav_log_out -> {
                    Toast.makeText(this, "Log Out clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java) // Assuming MainActivity is your login/intro
                    startActivity(intent)
                    finishAffinity() // Close all activities in the current task
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after selection
            true
        }

        // Handle back press to close drawer first
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


        // Set click listeners for the upload buttons
        binding.uploadTrainingImage1.setOnClickListener {
            pickTrainingImage1Launcher.launch("image/*")
        }
        binding.uploadTrainingImage2.setOnClickListener {
            pickTrainingImage2Launcher.launch("image/*")
        }

        // Chat FAB
        binding.chatFab.setOnClickListener {
            Toast.makeText(this, "Chat FAB clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupImagePicker(imageView: ImageView, launcherSetter: (ActivityResultLauncher<String>) -> Unit) {
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageView.setImageURI(uri)
                Toast.makeText(this, "Training image updated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            }
        }
        launcherSetter(launcher)
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