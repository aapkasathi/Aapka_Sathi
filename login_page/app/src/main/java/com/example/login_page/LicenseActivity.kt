package com.example.login_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
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
import com.example.login_page.databinding.ActivityLicensesBinding
import com.google.android.material.button.MaterialButton
import java.util.Locale
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Build

class LicensesActivity : AppCompatActivity() {

    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"
    private lateinit var binding: ActivityLicensesBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    // New variables for dynamic functionality
    private var licenseCount = 0
    private var lastClickedImageView: ImageView? = null
    private lateinit var dynamicImagePickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the single reusable launcher for dynamic cards
        dynamicImagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                lastClickedImageView?.setImageURI(uri)
                Toast.makeText(this, "License image updated!", Toast.LENGTH_SHORT).show()
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
                R.id.nav_licenses -> {
                    Toast.makeText(this, "Already on Licenses", Toast.LENGTH_SHORT).show()
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
                R.id.nav_licenses_info -> {
                    Toast.makeText(this, "License info clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LicenseInfoActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_setting -> {
                    Toast.makeText(this, "Setting clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_help_support -> {
                    Toast.makeText(this, "Help & Support clicked", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HelpSupportActivity::class.java)
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

        // Set the listener for the new "Add another License" button
        binding.addLicenseButton.setOnClickListener {
            addNewLicenseCard()
        }

        // Add the initial license card when the activity starts
        addNewLicenseCard()
    }

    // Function to dynamically add a new license card
    private fun addNewLicenseCard() {
        licenseCount++ // Increment the counter

        // Inflate the new license card layout
        val licenseCardView = LayoutInflater.from(this)
            .inflate(R.layout.layout_license_card, binding.licenseContainer, false)

        // Find and set the views inside the new card
        val titleTextView = licenseCardView.findViewById<TextView>(R.id.tvLicenseTitle)
        val uploadButton = licenseCardView.findViewById<MaterialButton>(R.id.uploadButton)
        val downloadButton = licenseCardView.findViewById<ImageView>(R.id.downloadButton) // Find the download button
        val licenseImageView = licenseCardView.findViewById<ImageView>(R.id.licenseImage)

        // Update the title and button text based on the current license count
        titleTextView.text = "License No. $licenseCount"
        uploadButton.text = "Upload License $licenseCount"

        // Set up the click listener for this card's upload button
        uploadButton.setOnClickListener {
            // Store a reference to the ImageView that belongs to this card
            lastClickedImageView = licenseImageView
            // Launch the image picker
            dynamicImagePickerLauncher.launch("image/*")
        }

        // Set up the click listener for the new download button
        downloadButton.setOnClickListener {
            // Check if an image is already uploaded
            if (licenseImageView.drawable != null && licenseImageView.drawable !is BitmapDrawable) {
                // If an image is present, handle the download logic here
                Toast.makeText(this, "Downloading License No. $licenseCount...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No license to download. Please upload one first.", Toast.LENGTH_SHORT).show()
            }
        }

        // Add the new license card to the container, before the "Add" button
        binding.licenseContainer.addView(licenseCardView, binding.licenseContainer.childCount - 1)
    }

    // This is the function to show the custom logout pop-up
    private fun showLogoutConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()

        val yesButton = dialogView.findViewById<Button>(R.id.btn_yes)
        val noButton = dialogView.findViewById<Button>(R.id.btn_no)

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

    // This is just an example function, it requires permission handling
    private fun saveBitmap(bitmap: Bitmap, filename: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 (Q) and above, use MediaStore
            // This is the recommended approach but requires more setup
            Toast.makeText(this, "Saving to MediaStore...", Toast.LENGTH_SHORT).show()
        } else {
            // For older Android versions, use external storage
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                try {
                    val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val imageFile = File(imagesDir, filename)
                    val outputStream = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                    Toast.makeText(this, "Image saved to gallery!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            } else {
                // Request permission if not granted
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        try {
            super.attachBaseContext(LocaleHelper.getPersistedLocale(newBase))
        } catch (e: Exception) {
            super.attachBaseContext(newBase)
        }
    }
}