package com.example.login_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.example.login_page.databinding.ActivityCertificateBinding
import com.google.android.material.button.MaterialButton
import java.util.Locale
import android.graphics.drawable.BitmapDrawable // Import for drawable to bitmap conversion
import android.os.Environment // Import for external storage directory
import android.Manifest // Import for permission handling
import android.content.pm.PackageManager // Import for permission check
import androidx.core.app.ActivityCompat // Import for permission request
import androidx.core.content.ContextCompat // Import for permission check
import android.os.Build // Import for Build version check
import java.io.File
import java.io.FileOutputStream
import android.graphics.Bitmap

class CertificatesActivity : AppCompatActivity() {

    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"
    private lateinit var binding: ActivityCertificateBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    // New variables for dynamic functionality
    private var certificateCount = 0
    private var lastClickedImageView: ImageView? = null
    private lateinit var dynamicImagePickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertificateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the single reusable launcher for dynamic cards
        dynamicImagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                lastClickedImageView?.setImageURI(uri)
                Toast.makeText(this, "Certificate image updated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            }
            lastClickedImageView = null // Clear the reference
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
                    Toast.makeText(this, "Already on Certificates", Toast.LENGTH_SHORT).show()
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

        // Handle back press to close drawer first
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

        // --- Handle Chat FAB Click ---
        binding.chatFab.setOnClickListener {
            Toast.makeText(this, "Chat FAB clicked!", Toast.LENGTH_SHORT).show()
        }

        // Set the listener for the new "Add another Certificate" button
        binding.addCertificateButton.setOnClickListener {
            addNewCertificateCard()
        }

        // Add the initial certificate card when the activity starts
        addNewCertificateCard()
    }

    // Function to dynamically add a new certificate card
    private fun addNewCertificateCard() {
        certificateCount++ // Increment the counter

        // Inflate the new certificate card layout
        val certificateCardView = LayoutInflater.from(this)
            .inflate(R.layout.layout_certificate_card, binding.certificateContainer, false)

        // Find and set the views inside the new card
        val titleTextView = certificateCardView.findViewById<TextView>(R.id.tvCertificateTitle)
        val uploadButton = certificateCardView.findViewById<MaterialButton>(R.id.uploadButton)
        val downloadButton = certificateCardView.findViewById<ImageButton>(R.id.downloadButton)
        val certificateImageView = certificateCardView.findViewById<ImageView>(R.id.certificateImage)

        // Update the title and button text based on the current certificate count
        titleTextView.text = "Certificate No. $certificateCount"
        uploadButton.text = "Upload Certificate $certificateCount"

        // Set up the click listener for this card's upload button
        uploadButton.setOnClickListener {
            // Store a reference to the ImageView that belongs to this card
            lastClickedImageView = certificateImageView
            // Launch the image picker
            dynamicImagePickerLauncher.launch("image/*")
        }

        // Set up the click listener for the new download button
        downloadButton.setOnClickListener {
            // Check if an image is already uploaded
            if (certificateImageView.drawable != null) {
                // Check if the current drawable is not the default placeholder
                if (certificateImageView.drawable !is BitmapDrawable) {
                    Toast.makeText(this, "Downloading Certificate No. $certificateCount...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No certificate to download. Please upload one first.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Add the new certificate card to the container, before the "Add" button
        binding.certificateContainer.addView(certificateCardView, binding.certificateContainer.childCount - 1)
    }

    // This is the function to show the custom logout pop-up
    private fun showLogoutConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()

        val yesButton = dialogView.findViewById<MaterialButton>(R.id.btn_yes)
        val noButton = dialogView.findViewById<MaterialButton>(R.id.btn_no)

        yesButton.setOnClickListener {
            // Handle logout logic here
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity() // Close all activities in the current task
            alertDialog.dismiss()
        }

        noButton.setOnClickListener {
            // Just dismiss the dialog, no action needed
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