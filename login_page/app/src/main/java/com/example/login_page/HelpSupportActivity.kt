package com.example.login_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import com.example.login_page.databinding.ActivityHelpSupportBinding

class HelpSupportActivity : AppCompatActivity() {
    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"
    private lateinit var binding: ActivityHelpSupportBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            submitForm()
        }

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
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_licenses -> {
                    val intent = Intent(this, LicensesActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_certificates -> {
                    val intent = Intent(this, CertificatesActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_policies -> {
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
                R.id.nav_licenses_info -> {
                    val intent = Intent(this, LicenseInfoActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_setting -> {
                    Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_help_support -> {
                    Toast.makeText(this, "Already on Help & Support", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_log_out -> {
                    // Show the custom logout dialog instead of a Toast
                    showLogoutConfirmationDialog()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }

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
    }

    private fun submitForm(): Boolean {
        val name = binding.etName.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val subject = binding.etSubject.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()

        var isValid = true

        if (name.isEmpty()) {
            binding.etName.error = "Name is required"
            isValid = false
        } else {
            binding.etName.error = null
        }

        if (phoneNumber.isEmpty()) {
            binding.etPhoneNumber.error = "Phone Number is required"
            isValid = false
        } else {
            binding.etPhoneNumber.error = null
        }

        if (subject.isEmpty()) {
            binding.etSubject.error = "Subject is required"
            isValid = false
        } else {
            binding.etSubject.error = null
        }

        if (message.isEmpty()) {
            binding.etMessage.error = "Message is required"
            isValid = false
        } else {
            binding.etMessage.error = null
        }

        if (!isValid) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            return false
        }

        val formData = "Name: $name\nAddress: ${if (address.isEmpty()) "N/A" else address}\nEmail: ${if (email.isEmpty()) "N/A" else email}\nPhone: $phoneNumber\nSubject: $subject\nMessage: $message"
        Toast.makeText(this, "Form Submitted:\n$formData", Toast.LENGTH_LONG).show()

        clearForm()
        return true
    }

    private fun clearForm() {
        binding.etName.text.clear()
        binding.etAddress.text.clear()
        binding.etEmail.text.clear()
        binding.etPhoneNumber.text.clear()
        binding.etSubject.text.clear()
        binding.etMessage.text.clear()
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