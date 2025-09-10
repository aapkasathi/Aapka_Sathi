package com.example.login_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import com.example.login_page.databinding.ActivityLicenseInfoBinding

class LicenseInfoActivity : AppCompatActivity() {

    private val YOUTUBE_TRAINING_LINK = "https://www.youtube.com/@aapkasathi09"
    private lateinit var binding: ActivityLicenseInfoBinding
    private lateinit var allCards: List<Pair<LinearLayout, ImageView>>
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicenseInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- MENU BAR (copied from LicensesActivity) ---
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
                    startActivity(Intent(this, HomePageActivity::class.java))
                    finish()
                }
                R.id.nav_licenses -> {
                    startActivity(Intent(this, LicensesActivity::class.java))
                    finish()
                }
                R.id.nav_certificates -> {
                    startActivity(Intent(this, CertificatesActivity::class.java))
                }
                R.id.nav_policies -> {
                    startActivity(Intent(this, Policy::class.java))
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
                    Toast.makeText(this, "Already on License Info", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                }
                R.id.nav_help_support -> {
                    startActivity(Intent(this, HelpSupportActivity::class.java))
                }
                R.id.nav_log_out -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
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



        // --- YOUR CARD LOGIC (unchanged) ---
        val header1: RelativeLayout = findViewById(R.id.header1)
        val content1: LinearLayout = findViewById(R.id.content1)
        val arrow1: ImageView = findViewById(R.id.arrow1)
        val text1: TextView = findViewById(R.id.text1)

        val header2: RelativeLayout = findViewById(R.id.header2)
        val content2: LinearLayout = findViewById(R.id.content2)
        val arrow2: ImageView = findViewById(R.id.arrow2)
        val text2: TextView = findViewById(R.id.text2)

        val header3: RelativeLayout = findViewById(R.id.header3)
        val content3: LinearLayout = findViewById(R.id.content3)
        val arrow3: ImageView = findViewById(R.id.arrow3)
        val text3: TextView = findViewById(R.id.text3)

        val header4: RelativeLayout = findViewById(R.id.header4)
        val content4: LinearLayout = findViewById(R.id.content4)
        val arrow4: ImageView = findViewById(R.id.arrow4)
        val text4: TextView = findViewById(R.id.text4)

        val header5: RelativeLayout = findViewById(R.id.header5)
        val content5: LinearLayout = findViewById(R.id.content5)
        val arrow5: ImageView = findViewById(R.id.arrow5)
        val text5: TextView = findViewById(R.id.text5)

        val header6: RelativeLayout = findViewById(R.id.header6)
        val content6: LinearLayout = findViewById(R.id.content6)
        val arrow6: ImageView = findViewById(R.id.arrow6)
        val text6: TextView = findViewById(R.id.text6)

        allCards = listOf(
            Pair(content1, arrow1),
            Pair(content2, arrow2),
            Pair(content3, arrow3),
            Pair(content4, arrow4),
            Pair(content5, arrow5),
            Pair(content6, arrow6)
        )

        text1.text = HtmlCompat.fromHtml(getString(R.string.license_street_vendors), HtmlCompat.FROM_HTML_MODE_LEGACY)
        text2.text = HtmlCompat.fromHtml(getString(R.string.FSSAI_Food_Safety_Registration), HtmlCompat.FROM_HTML_MODE_LEGACY)
        text3.text = HtmlCompat.fromHtml(getString(R.string.Trade_License_or_Shop_Establishment_License), HtmlCompat.FROM_HTML_MODE_LEGACY)
        text4.text = HtmlCompat.fromHtml(getString(R.string.hawker_license), HtmlCompat.FROM_HTML_MODE_LEGACY)
        text5.text = HtmlCompat.fromHtml(getString(R.string.no_objection_certificate), HtmlCompat.FROM_HTML_MODE_LEGACY)
        text6.text = HtmlCompat.fromHtml(getString(R.string.Labour_Card_or_Unorganized_Worker_ID), HtmlCompat.FROM_HTML_MODE_LEGACY)

        header1.setOnClickListener { toggleCard(content1, arrow1) }
        header2.setOnClickListener { toggleCard(content2, arrow2) }
        header3.setOnClickListener { toggleCard(content3, arrow3) }
        header4.setOnClickListener { toggleCard(content4, arrow4) }
        header5.setOnClickListener { toggleCard(content5, arrow5) }
        header6.setOnClickListener { toggleCard(content6, arrow6) }
    }

    private fun toggleCard(selectedContent: LinearLayout, selectedArrow: ImageView) {
        val isSelectedOpen = selectedContent.visibility == android.view.View.VISIBLE

        allCards.forEach { (content, arrow) ->
            content.visibility = android.view.View.GONE
            arrow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_keyboard_arrow_down_24))
            content.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        }

        if (!isSelectedOpen) {
            selectedContent.visibility = android.view.View.VISIBLE
            selectedArrow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.outline_keyboard_arrow_up_24))
            selectedContent.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
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
