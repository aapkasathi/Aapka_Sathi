package com.example.login_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback // Import OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat // Import GravityCompat for drawer
import com.example.login_page.databinding.ActivityProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileActivity : AppCompatActivity() {
    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"
    private lateinit var binding: ActivityProfileBinding
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var onBackPressedCallback: OnBackPressedCallback // Declare for back press handling

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                binding.profileImageView.setImageURI(uri)
                Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Handle Menu Button Click (to open THIS activity's drawer) ---
        binding.menuButton.setOnClickListener { // IMPORTANT: This ID should match your XML
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
                    finish() // Close this activity to prevent deep back stack
                }
//                R.id.nav_my_profile -> {
//                    Toast.makeText(this, "Already on My Profile", Toast.LENGTH_SHORT).show()
//                    // Already on this screen, no need to launch new activity
//                }
                R.id.nav_licenses -> {
                    Toast.makeText(this, "Licenses clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LicensesActivity::class.java) // Ensure LicensesActivity exists
                    startActivity(intent)
                }
                R.id.nav_certificates -> {
                    Toast.makeText(this, "Certificates clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CertificatesActivity::class.java) // Ensure CertificatesActivity exists
                    startActivity(intent)
                }
                R.id.nav_policies -> {
                    Toast.makeText(this, "Policies clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Policy::class.java) // Ensure Policy Activity exists
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
                R.id.nav_licenses_info -> {
                    Toast.makeText(this, "license info clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LicenseInfoActivity::class.java)
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
                R.id.nav_setting -> {
                    Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SettingActivity::class.java) // Assuming SettingActivity exists
                    startActivity(intent)
                }
                R.id.nav_log_out -> {
                    Toast.makeText(this, "Log Out clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
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

        // --- Handle Edit Profile Image Click (or the image itself) ---
        binding.editProfileImage.setOnClickListener {
            launchImagePicker()
        }
        binding.profileImageView.setOnClickListener {
            launchImagePicker()
        }

        // --- Handle "Save Changes" Button Click ---
        binding.btnSaveChanges.setOnClickListener {
            val userName = binding.etUserName.text.toString().trim()
            val phoneNumber = binding.etPhoneNumber.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (userName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Changes saved for: $userName, $phoneNumber", Toast.LENGTH_LONG).show()
            }
        }

        // --- Handle Chat FAB Click ---
        binding.chatFab.setOnClickListener {
            Toast.makeText(this, "Chat FAB clicked!", Toast.LENGTH_SHORT).show()
        }

        // --- Handle Edit Icons next to text fields (optional) ---
        binding.editUserName.setOnClickListener {
            binding.etUserName.isEnabled = !binding.etUserName.isEnabled
            if (binding.etUserName.isEnabled) binding.etUserName.requestFocus()
            Toast.makeText(this, "User Name field toggled!", Toast.LENGTH_SHORT).show()
        }
        binding.editPhoneNumber.setOnClickListener {
            binding.etPhoneNumber.isEnabled = !binding.etPhoneNumber.isEnabled
            if (binding.etPhoneNumber.isEnabled) binding.etPhoneNumber.requestFocus()
            Toast.makeText(this, "Phone Number field toggled!", Toast.LENGTH_SHORT).show()
        }
        binding.editPassword.setOnClickListener {
            binding.etPassword.isEnabled = !binding.etPassword.isEnabled
            if (binding.etPassword.isEnabled) binding.etPassword.requestFocus()
            Toast.makeText(this, "Password field toggled!", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to launch the image picker
    private fun launchImagePicker() {
        pickImageLauncher.launch("image/*")
    }

    override fun attachBaseContext(newBase: Context) {
        try {
            super.attachBaseContext(LocaleHelper.getPersistedLocale(newBase))
        } catch (e: Exception) {
            super.attachBaseContext(newBase)
        }
    }
}