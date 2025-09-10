package com.example.login_page

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

// Corrected import path for LanguageSelectionActivity
// Assuming LanguageSelectionActivity is in the same package or a subpackage of login_page
// If it's in 'com.example.myapplication', change this import to:
// import com.example.myapplication.LanguageSelectionActivity
import com.example.login_page.LanguageSelectionActivity // Adjust this if your LanguageSelectionActivity is in a different package

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Sets the layout for the splash screen

        // Post a delayed action to switch to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            // Create an Intent to navigate from SplashActivity to LanguageSelectionActivity
            val intent = Intent(this, LanguageSelectionActivity::class.java)
            startActivity(intent) // Start the LanguageSelectionActivity

            // Finish the current SplashActivity to prevent the user from navigating back to it
            // using the back button.
            finish()
        }, 4000) // The delay in milliseconds (3000ms = 3 seconds)
    }
}