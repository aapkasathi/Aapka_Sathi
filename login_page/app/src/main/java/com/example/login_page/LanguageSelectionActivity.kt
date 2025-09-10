package com.example.login_page

import android.content.Context
import android.content.Intent // Import for starting new activities
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton // For the menu icon in the app bar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login_page.LocaleHelper // Make sure this import path is correct for your LocaleHelper class
import com.example.login_page.databinding.ActivityLanguageSelectionBinding // <-- ADD THIS if using ViewBinding

class LanguageSelectionActivity : AppCompatActivity() {

    // If using ViewBinding, declare your binding object
    private lateinit var binding: ActivityLanguageSelectionBinding

    // Define a map of button IDs to their locale codes for cleaner implementation
    private val languageMapping = mapOf(
        R.id.englishButton to "en", // Added English as it's a full-width button in the new XML
        R.id.hindiButton to "hi",
        R.id.teluguButton to "te",
        R.id.tamilButton to "ta",
        R.id.kannadaButton to "kn",
        R.id.malayalamButton to "ml",
        R.id.gujaratiButton to "gu",
        R.id.punjabiButton to "pa", // Corrected ID to match XML: 'punjabiButton'
        R.id.marathiButton to "mr",
        R.id.bengaliButton to "bn", // Corrected ID to match XML: 'bengaliButton'
        R.id.odiaButton to "or"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- ViewBinding Setup (Recommended) ---
        // 1. In your app's build.gradle (Module: app) file, add this inside the 'android' block:
        //    buildFeatures {
        //        viewBinding true
        //    }
        // 2. Sync your project.
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // --- End ViewBinding Setup ---


        // --- Handle Top AppBar Menu Button Click (if applicable) ---
//        binding.menuButton.setOnClickListener { // Using binding if ViewBinding is enabled
//            Toast.makeText(this, "Menu button clicked!", Toast.LENGTH_SHORT).show()
//            // Implement your drawer opening or other menu action here
//        }

        // Iterate through the map and set click listeners efficiently
        languageMapping.forEach { (buttonId, localeCode) ->
            // Using ViewBinding to access the buttons:
            val languageButton: Button? = when(buttonId) {
                R.id.englishButton -> binding.englishButton
                R.id.hindiButton -> binding.hindiButton
                R.id.teluguButton -> binding.teluguButton
                R.id.tamilButton -> binding.tamilButton
                R.id.kannadaButton -> binding.kannadaButton
                R.id.malayalamButton -> binding.malayalamButton
                R.id.gujaratiButton -> binding.gujaratiButton
                R.id.punjabiButton -> binding.punjabiButton
                R.id.marathiButton -> binding.marathiButton
                R.id.bengaliButton -> binding.bengaliButton
                R.id.odiaButton -> binding.odiaButton
                else -> null
            }

            // Fallback for direct findViewById if ViewBinding isn't fully set up or preferred
            // val languageButton: Button? = findViewById(buttonId) // Use this line if NOT using ViewBinding

            if (languageButton == null) {
                android.util.Log.e("LangSelection", "Button with ID $buttonId not found in layout!")
                return@forEach // Skip to the next iteration if button not found
            }

            languageButton.setOnClickListener {
                // 1. Use the LocaleHelper to change the locale AND save the preference
                LocaleHelper.setLocale(this, localeCode)
                Toast.makeText(this, "Language set to $localeCode. Proceeding to Login.", Toast.LENGTH_SHORT).show()
                android.util.Log.d("LangSelection", "Language set to $localeCode, launching MainActivity.")

                // 2. Navigate to MainActivity (your login page)
                val intent = Intent(this, MainActivity::class.java)
                // If MainActivity is designed to be the single entry point after language selection
                // and you want to clear the back stack, use these flags:
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                // 3. Finish this activity so the user can't come back to language selection
                //    after choosing a language and going to login.
                finish()
            }
        }
    }

    /**
     * Crucial for ensuring this Activity respects the currently selected locale.
     * This method is called before onCreate and allows you to wrap the Activity's context
     * with the desired locale.
     *
     * This is especially important if the user hits the back button to return here
     * after changing the language.
     */
    override fun attachBaseContext(newBase: Context) {
        // This applies the persisted language setting to the Activity's context
        super.attachBaseContext(LocaleHelper.getPersistedLocale(newBase))
    }
}