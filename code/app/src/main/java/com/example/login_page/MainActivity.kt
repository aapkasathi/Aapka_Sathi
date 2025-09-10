package com.example.login_page

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login_page.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        // --- Login/Register Button ---
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                showToast("Please enter both username and password")
                return@setOnClickListener
            }

            loginOrRegisterUser(username, password)
        }

        // --- Register Navigation (if you want a separate screen) ---
        binding.clickHereTextView.setOnClickListener {
            val intent = Intent(this, PersonalDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginOrRegisterUser(username: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE

        usersRef.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progressBar.visibility = View.GONE

                if (snapshot.exists()) {
                    // ✅ User exists → check password
                    val storedPassword = snapshot.child("password").getValue(String::class.java)
                    if (storedPassword == password) {
                        showToast("Login successful! Redirecting to Home...")
                        val intent = Intent(this@MainActivity, HomePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToast("Incorrect password")
                    }
                } else {
                    // ✅ User doesn’t exist → create new user
                    val newUser = HashMap<String, String>()
                    newUser["username"] = username
                    newUser["password"] = password

                    usersRef.child(username).setValue(newUser)
                        .addOnSuccessListener {
                            showToast("User registered successfully! Redirecting to Home...")
                            val intent = Intent(this@MainActivity, HomePageActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            showToast("Failed to login: ${it.message}")
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                showToast("Database error: ${error.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
