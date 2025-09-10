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
import androidx.activity.OnBackPressedCallback // Import OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat // Import GravityCompat for drawer
import com.example.login_page.databinding.PolicyBinding // UPDATED: Use PolicyBinding (singular)

class Policy : AppCompatActivity() {

    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"

    private lateinit var binding: PolicyBinding // UPDATED: Binding type is PolicyBinding

    // Variable to keep track of the currently open expandable content LinearLayout
    private var currentlyOpenContent: LinearLayout? = null
    // Variable to keep track of the arrow icon associated with the currently open content
    private var currentlyOpenArrow: ImageView? = null
    private lateinit var onBackPressedCallback: OnBackPressedCallback // Declare for back press handling

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewBinding and set the content view
        binding = PolicyBinding.inflate(layoutInflater) // UPDATED: Inflate PolicyBinding
        setContentView(binding.root) // Set content view using binding root

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
                    Toast.makeText(this, "Already on Policies", Toast.LENGTH_SHORT).show()
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

        // List of all policy headers, expandable contents, and arrow icons
        // Store them as triples (headerLayout, contentLayout, arrowIcon) references using binding
        val policies = listOf(
            Triple(binding.policy1Header, binding.policy1ExpandableContent, binding.policy1Arrow),
            Triple(binding.policy2Header, binding.policy2ExpandableContent, binding.policy2Arrow),
            Triple(binding.policy3Header, binding.policy3ExpandableContent, binding.policy3Arrow),
            Triple(binding.policy4Header, binding.policy4ExpandableContent, binding.policy4Arrow),
            Triple(binding.policy5Header, binding.policy5ExpandableContent, binding.policy5Arrow),
            Triple(binding.policy6Header, binding.policy6ExpandableContent, binding.policy6Arrow),
            Triple(binding.policy7Header, binding.policy7ExpandableContent, binding.policy7Arrow),
            Triple(binding.policy8Header, binding.policy8ExpandableContent, binding.policy8Arrow)
        )

        // Iterate through each policy and set up the toggle logic
        policies.forEach { (headerLayout, contentLayout, arrowIcon) ->
            setupPolicyToggle(headerLayout, contentLayout, arrowIcon)
        }
    }

    private fun setupPolicyToggle(headerLayout: LinearLayout, expandableContent: LinearLayout, arrowIcon: ImageView) {
        headerLayout.setOnClickListener {
            // If the clicked content is currently open, close it
            if (expandableContent.visibility == View.VISIBLE) {
                expandableContent.visibility = View.GONE
                arrowIcon.rotation = 0f
                currentlyOpenContent = null
                currentlyOpenArrow = null
            } else {
                // If another content is open, close it first
                currentlyOpenContent?.let {
                    it.visibility = View.GONE
                }
                currentlyOpenArrow?.let {
                    it.rotation = 0f
                }

                // Open the newly clicked content
                expandableContent.visibility = View.VISIBLE
                arrowIcon.rotation = 180f // Rotate arrow up
                currentlyOpenContent = expandableContent
                currentlyOpenArrow = arrowIcon
            }
        }
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