//package com.example.login_page
//
//import android.app.DatePickerDialog
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import com.example.login_page.databinding.ActivityPersonalDetailsBinding
//import java.text.SimpleDateFormat
//import java.util.*
//import android.app.Activity
//import android.net.Uri
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//
//// 🔥 Firebase imports
//
//
//class PersonalDetailsActivity : AppCompatActivity() {
//
//    // 🔥 Firebase references
//    private lateinit var binding: ActivityPersonalDetailsBinding
//    private lateinit var auth: FirebaseAuth
//    private lateinit var storage: FirebaseStorage
//    private val database = FirebaseDatabase.getInstance().reference
//
//
//    private val PICK_PERSONAL_IMAGE = 1001
//    private val PICK_AADHAR_IMAGE = 1002
//    private var personalImageUri: Uri? = null
//    private var aadharImageUri: Uri? = null
//    // Flag to prevent infinite loop during DOB formatting
//    private var isFormatting = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPersonalDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // 🔥 initialize FirebaseAuth
//        auth = FirebaseAuth.getInstance()
//        storage = FirebaseStorage.getInstance()
//
//        // --- Back button ---
//        binding.backButton.setOnClickListener {
//            onBackPressed()
//        }
//
//        // Setup Gender Spinner
//        val genderOptions = resources.getStringArray(R.array.gender_options)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.genderSpinner.adapter = adapter
//
//        // Set up DOB TextWatcher & DatePicker
//        setupDobTextWatcher()
//        setupDatePickerDialog()
//
//        // --- Save & Next button ---
//        binding.saveNextButton.setOnClickListener {
//            val fullName = binding.fullNameEditText.text.toString()
//            val fatherName = binding.fatherNameEditText.text.toString().trim()
//            val fullAddress = binding.fullAddressEditText.text.toString().trim()
//            val phoneNo = binding.phoneNoEditText.text.toString().trim()
//            val selectedGender = binding.genderSpinner.selectedItem.toString()
//            val dob = binding.dobEditText.text.toString().trim()
//            val age = binding.ageEditText.text.toString().trim()
//            val aadharNumber = binding.aadharNumberEditText.text.toString().trim()
//
//            // ✅ Validation
//            if (fullName.isEmpty()) {
//                Toast.makeText(this, "Please enter your full name.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (fullName.trim() != fullName) {
//                Toast.makeText(this, "Full Name cannot have leading/trailing spaces.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (fullName.contains("  ")) {
//                Toast.makeText(this, "Full Name cannot have multiple spaces.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (fatherName.isEmpty()) {
//                Toast.makeText(this, "Please enter your father's name.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (fullAddress.isEmpty()) {
//                Toast.makeText(this, "Please enter your full address.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (phoneNo.isEmpty()) {
//                Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (phoneNo.length != 10) {
//                Toast.makeText(this, "Phone number must be 10 digits.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (selectedGender == resources.getString(R.string.select_gender_hint)) {
//                Toast.makeText(this, "Please select your gender.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (dob.isEmpty()) {
//                Toast.makeText(this, "Please enter your date of birth.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (!isValidDateFormat(dob)) {
//                Toast.makeText(this, "Please enter DOB in DD/MM/YYYY format.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (age.isEmpty()) {
//                Toast.makeText(this, "Please enter your age.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (aadharNumber.isEmpty()) {
//                Toast.makeText(this, "Please enter your Aadhar number.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (aadharNumber.length != 12) {
//                Toast.makeText(this, "Aadhar number must be 12 digits.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // 🔥 Save to Firebase
//            val userId = auth.currentUser?.uid ?: database.push().key!!
//            val personalData = PersonalDetails(
//                fullName,
//                fatherName,
//                fullAddress,
//                phoneNo,
//                selectedGender,
//                dob,
//                age,
//                aadharNumber
//            )
//
//            database.child("users").child(userId).child("personalDetails")
//                .setValue(personalData)
//                .addOnSuccessListener {
//                    Toast.makeText(this, "✅ Personal details stored in Firebase", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "❌ Failed: ${it.message}", Toast.LENGTH_SHORT).show()
//                }
//
//            // Move to next activity
//            val intent = Intent(this, BusinessDetailsActivity::class.java)
//            intent.putExtra("userId", userId)
//            startActivity(intent)
//        }
//
//        binding.personalPhotoButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"
//            startActivityForResult(intent, PICK_PERSONAL_IMAGE)
//        }
//
//        binding.aadharPhotoButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"
//            startActivityForResult(intent, PICK_AADHAR_IMAGE)
//        }
//
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//            val imageUri = data.data
//            when (requestCode) {
//                PICK_PERSONAL_IMAGE -> {
//                    personalImageUri = imageUri
//                    uploadImageToFirebase("personalPhotos", personalImageUri!!)
//                }
//                PICK_AADHAR_IMAGE -> {
//                    aadharImageUri = imageUri
//                    uploadImageToFirebase("aadharPhotos", aadharImageUri!!)
//                }
//            }
//        }
//    }
//
//    private fun uploadImageToFirebase(folderName: String, fileUri: Uri) {
//        val fileName = UUID.randomUUID().toString() + ".jpg"
//        val ref = storage.reference.child("$folderName/$fileName")
//
//        ref.putFile(fileUri)
//            .addOnSuccessListener {
//                ref.downloadUrl.addOnSuccessListener { uri ->
//                    Toast.makeText(this, "✅ Uploaded: $uri", Toast.LENGTH_SHORT).show()
//
//                    // Agar database me URL store karna hai:
//                    val userId = auth.currentUser?.uid ?: database.push().key!!
//                    database.child("users").child(userId).child(folderName)
//                        .setValue(uri.toString())
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "❌ Upload Failed: ${it.message}", Toast.LENGTH_LONG).show()
//            }
//    }
//
//
//    // ✅ Date format check
//    private fun isValidDateFormat(dateString: String): Boolean {
//        return try {
//            val dobFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//            dobFormat.isLenient = false
//            dobFormat.parse(dateString)
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    // ✅ Auto format DOB & calculate age
//    private fun setupDobTextWatcher() {
//        binding.dobEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                if (isFormatting) return
//
//                val input = s.toString()
//                var formatted = input.replace("/", "")
//
//                if (formatted.length > 8) formatted = formatted.substring(0, 8)
//
//                val newFormatted = StringBuilder()
//                for (i in formatted.indices) {
//                    newFormatted.append(formatted[i])
//                    if ((i == 1 || i == 3) && i != formatted.length - 1) {
//                        newFormatted.append('/')
//                    }
//                }
//
//                isFormatting = true
//                if (newFormatted.toString() != input) {
//                    binding.dobEditText.setText(newFormatted.toString())
//                    binding.dobEditText.setSelection(newFormatted.length)
//                }
//                isFormatting = false
//
//                if (newFormatted.length == 10) {
//                    try {
//                        val dobFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//                        dobFormat.isLenient = false
//                        val dateOfBirth = dobFormat.parse(newFormatted.toString())
//                        if (dateOfBirth != null) {
//                            val age = calculateAge(dateOfBirth)
//                            binding.ageEditText.setText(age.toString())
//                        }
//                    } catch (e: Exception) {
//                        binding.ageEditText.text = null
//                    }
//                } else {
//                    binding.ageEditText.text = null
//                }
//            }
//        })
//    }
//
//    // ✅ Calculate age
//    private fun calculateAge(dateOfBirth: Date): Int {
//        val dob = Calendar.getInstance()
//        dob.time = dateOfBirth
//        val today = Calendar.getInstance()
//
//        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
//        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
//            age--
//        }
//        return age
//    }
//
//    // ✅ DatePicker for DOB
//    private fun setupDatePickerDialog() {
//        binding.dobEditText.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//            val datePickerDialog = DatePickerDialog(
//                this,
//                { _, selectedYear, selectedMonth, selectedDay ->
//                    val formattedDate = String.format(Locale.US, "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
//                    binding.dobEditText.setText(formattedDate)
//                },
//                year, month, day
//            )
//            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
//            datePickerDialog.show()
//        }
//    }
//}
//
//// 🔥 Data class for Firebase
//data class PersonalDetails(
//    val fullName: String? = null,
//    val fatherName: String? = null,
//    val fullAddress: String? = null,
//    val phoneNo: String? = null,
//    val gender: String? = null,
//    val dob: String? = null,
//    val age: String? = null,
//    val aadharNumber: String? = null
//)

package com.example.login_page

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import com.example.login_page.databinding.ActivityPersonalDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class PersonalDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Set up a click listener for the Back button (Arrow Icon Only) ---
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Setup Gender Spinner
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = adapter

        // Set up TextWatcher for Date of Birth field to auto-calculate age and format
        setupDobTextWatcher()

        // Set up DatePickerDialog for dobEditText click to allow easy date selection
        setupDatePickerDialog()

        // --- Set up a click listener for the Save & Next button ---
        binding.saveNextButton.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString()
            val fatherName = binding.fatherNameEditText.text.toString().trim()
            val fullAddress = binding.fullAddressEditText.text.toString().trim()
            val phoneNo = binding.phoneNoEditText.text.toString().trim()
            val selectedGender = binding.genderSpinner.selectedItem.toString()
            val dob = binding.dobEditText.text.toString().trim()
            val age = binding.ageEditText.text.toString().trim()
            val aadharNumber = binding.aadharNumberEditText.text.toString().trim()

            // Perform validation for all required fields
            if (fullName.isEmpty()) {
                Toast.makeText(this, "Please enter your full name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (fullName.trim() != fullName) {
                Toast.makeText(this, "Full Name cannot have leading or trailing spaces.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (fullName.contains("  ")) {
                Toast.makeText(this, "Full Name cannot have multiple spaces between words.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (fatherName.isEmpty()) {
                Toast.makeText(this, "Please enter your father's name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (fullAddress.isEmpty()) {
                Toast.makeText(this, "Please enter your full address.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (phoneNo.isEmpty()) {
                Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (phoneNo.length != 10) {
                Toast.makeText(this, "Phone number must be 10 digits.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedGender == resources.getString(R.string.select_gender_hint)) {
                Toast.makeText(this, "Please select your gender.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dob.isEmpty()) {
                Toast.makeText(this, "Please enter your date of birth.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Additional DOB format validation for submission
            if (!isValidDateFormat(dob)) {
                Toast.makeText(this, "Please enter DOB in DD/MM/YYYY format.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (age.isEmpty()) {
                Toast.makeText(this, "Please enter your age.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (aadharNumber.isEmpty()) {
                Toast.makeText(this, "Please enter your Aadhar number.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (aadharNumber.length != 12) {
                Toast.makeText(this, "Aadhar number must be 12 digits.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Personal details saved (mock)! Proceeding to Business Details...", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, BusinessDetailsActivity::class.java)
            startActivity(intent)
            // finish()
        }

        binding.personalPhotoButton.setOnClickListener {
            Toast.makeText(this, "Personal photo upload initiated!", Toast.LENGTH_SHORT).show()
        }

        binding.aadharPhotoButton.setOnClickListener {
            Toast.makeText(this, "Aadhar photo upload initiated!", Toast.LENGTH_SHORT).show()
        }
    }

    // Flag to prevent infinite loop during text changes
    private var isFormatting = false

    private fun setupDobTextWatcher() {
        binding.dobEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return // Prevent re-triggering during formatting

                val input = s.toString()
                var formatted = input.replace("/", "") // Remove all slashes for clean processing

                if (formatted.length > 8) { // Max 8 digits (DDMMYYYY)
                    formatted = formatted.substring(0, 8)
                }

                // Basic validation as user types
                if (formatted.length >= 2) {
                    val day = formatted.substring(0, 2).toIntOrNull()
                    if (day == null || day < 1 || day > 31) {
                        // Optionally, show a temporary error or prevent typing invalid day
                        // Toast.makeText(this@PersonalDetailsActivity, "Invalid Day", Toast.LENGTH_SHORT).show()
                    }
                }
                if (formatted.length >= 4) {
                    val month = formatted.substring(2, 4).toIntOrNull()
                    if (month == null || month < 1 || month > 12) {
                        // Optionally, show a temporary error or prevent typing invalid month
                        // Toast.makeText(this@PersonalDetailsActivity, "Invalid Month", Toast.LENGTH_SHORT).show()
                    }
                }

                // Auto-insert slashes
                val newFormatted = StringBuilder()
                for (i in formatted.indices) {
                    newFormatted.append(formatted[i])
                    if ((i == 1 || i == 3) && i != formatted.length - 1) { // Add slash after 2nd and 4th digit
                        newFormatted.append('/')
                    }
                }

                isFormatting = true
                if (newFormatted.toString() != input) {
                    binding.dobEditText.setText(newFormatted.toString())
                    binding.dobEditText.setSelection(newFormatted.length) // Keep cursor at the end
                }
                isFormatting = false

                // Calculate age only if the format is complete (DD/MM/YYYY)
                if (newFormatted.length == 10) {
                    try {
                        val dobFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        dobFormat.isLenient = false // Strict parsing
                        val dateOfBirth = dobFormat.parse(newFormatted.toString())
                        if (dateOfBirth != null) {
                            val age = calculateAge(dateOfBirth)
                            binding.ageEditText.setText(age.toString())
                        } else {
                            binding.ageEditText.text = null
                        }
                    } catch (e: Exception) {
                        binding.ageEditText.text = null
                        e.printStackTrace()
                    }
                } else {
                    binding.ageEditText.text = null
                }
            }
        })
    }

    // Function to calculate age from a Date object
    private fun calculateAge(dateOfBirth: Date): Int {
        val dob = Calendar.getInstance()
        dob.time = dateOfBirth
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

    // Function to set up a DatePickerDialog for selecting Date of Birth
    private fun setupDatePickerDialog() {
        binding.dobEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(Locale.US, "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.dobEditText.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    // Helper function for final DOB format validation before submission
    private fun isValidDateFormat(dateString: String): Boolean {
        return try {
            val dobFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            dobFormat.isLenient = false // Strict parsing
            dobFormat.parse(dateString)
            true
        } catch (e: Exception) {
            false
        }
    }
}
//package com.example.login_page
//
//import android.app.DatePickerDialog
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import com.example.login_page.databinding.ActivityPersonalDetailsBinding
//import java.text.SimpleDateFormat
//import java.util.*
//
//// 🔥 added Firebase imports
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//
//class PersonalDetailsActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityPersonalDetailsBinding
//
//    // 🔥 added Firebase references
//    private lateinit var auth: FirebaseAuth
//    private val database = FirebaseDatabase.getInstance().reference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPersonalDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // 🔥 initialize FirebaseAuth
//        auth = FirebaseAuth.getInstance()
//
//        // --- Set up a click listener for the Back button (Arrow Icon Only) ---
//        binding.backButton.setOnClickListener {
//            onBackPressed()
//        }
//
//        // Setup Gender Spinner
//        val genderOptions = resources.getStringArray(R.array.gender_options)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.genderSpinner.adapter = adapter
//
//        // Set up TextWatcher for Date of Birth field to auto-calculate age and format
//        setupDobTextWatcher()
//
//        // Set up DatePickerDialog for dobEditText click to allow easy date selection
//        setupDatePickerDialog()
//
//        // --- Set up a click listener for the Save & Next button ---
//        binding.saveNextButton.setOnClickListener {
//            val fullName = binding.fullNameEditText.text.toString()
//            val fatherName = binding.fatherNameEditText.text.toString().trim()
//            val fullAddress = binding.fullAddressEditText.text.toString().trim()
//            val phoneNo = binding.phoneNoEditText.text.toString().trim()
//            val selectedGender = binding.genderSpinner.selectedItem.toString()
//            val dob = binding.dobEditText.text.toString().trim()
//            val age = binding.ageEditText.text.toString().trim()
//            val aadharNumber = binding.aadharNumberEditText.text.toString().trim()
//
//            // Perform validation for all required fields
//            if (fullName.isEmpty()) {
//                Toast.makeText(this, "Please enter your full name.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (fullName.trim() != fullName) {
//                Toast.makeText(this, "Full Name cannot have leading or trailing spaces.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (fullName.contains("  ")) {
//                Toast.makeText(this, "Full Name cannot have multiple spaces between words.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if (fatherName.isEmpty()) {
//                Toast.makeText(this, "Please enter your father's name.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (fullAddress.isEmpty()) {
//                Toast.makeText(this, "Please enter your full address.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (phoneNo.isEmpty()) {
//                Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (phoneNo.length != 10) {
//                Toast.makeText(this, "Phone number must be 10 digits.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (selectedGender == resources.getString(R.string.select_gender_hint)) {
//                Toast.makeText(this, "Please select your gender.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (dob.isEmpty()) {
//                Toast.makeText(this, "Please enter your date of birth.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            // Additional DOB format validation for submission
//            if (!isValidDateFormat(dob)) {
//                Toast.makeText(this, "Please enter DOB in DD/MM/YYYY format.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (age.isEmpty()) {
//                Toast.makeText(this, "Please enter your age.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (aadharNumber.isEmpty()) {
//                Toast.makeText(this, "Please enter your Aadhar number.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (aadharNumber.length != 12) {
//                Toast.makeText(this, "Aadhar number must be 12 digits.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            Toast.makeText(this, "Personal details saved (mock)! Proceeding to Business Details...", Toast.LENGTH_SHORT).show()
//
//            // 🔥 Firebase update: Save data before moving to next screen
//            val userId = auth.currentUser?.uid ?: database.push().key!! // use UID if logged in, else random key
//            val personalData = PersonalDetails(
//                fullName,
//                fatherName,
//                fullAddress,
//                phoneNo,
//                selectedGender,
//                dob,
//                age,
//                aadharNumber
//            )
//
//            database.child("users").child(userId).child("personalDetails")
//                .setValue(personalData)
//                .addOnSuccessListener {
//                    Toast.makeText(this, "✅ Personal details stored in Firebase", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "❌ Failed: ${it.message}", Toast.LENGTH_SHORT).show()
//                }
//
//            // move to next activity (your code kept same)
//            val intent = Intent(this, BusinessDetailsActivity::class.java)
//            intent.putExtra("userId", userId) // 🔥 pass uid to next page
//            startActivity(intent)
//            // finish()
//        }
//
//        binding.personalPhotoButton.setOnClickListener {
//            Toast.makeText(this, "Personal photo upload initiated!", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.aadharPhotoButton.setOnClickListener {
//            Toast.makeText(this, "Aadhar photo upload initiated!", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Helper function for final DOB format validation before submission
//    private fun isValidDateFormat(dateString: String): Boolean {
//        return try {
//            val dobFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//            dobFormat.isLenient = false // Strict parsing
//            dobFormat.parse(dateString)
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
//}
//
//// 🔥 added data class for Firebase
//data class PersonalDetails(
//    val fullName: String? = null,
//    val fatherName: String? = null,
//    val fullAddress: String? = null,
//    val phoneNo: String? = null,
//    val gender: String? = null,
//    val dob: String? = null,
//    val age: String? = null,
//    val aadharNumber: String? = null
//)
