package com.example.login_page

import android.app.DatePickerDialog // Import DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher // Import TextWatcher
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import com.example.login_page.databinding.ActivityBusinessDetailsBinding
import java.text.SimpleDateFormat // Import SimpleDateFormat
import java.util.* // Import Calendar and Date

class BusinessDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessDetailsBinding

    // Flag to prevent infinite loop during text changes for date field
    private var isFormattingDate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityBusinessDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Set up a click listener for the Back button ---
        binding.backButton.setOnClickListener { onBackPressed() }

        // --- Setup Business Type Spinner ---
        val businessTypeOptions = resources.getStringArray(R.array.business_type_options)
        val businessTypeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, businessTypeOptions)
        businessTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.businessTypeSpinner.adapter = businessTypeAdapter

        // --- Setup Licenses Spinner ---
        val licensesOptions = resources.getStringArray(R.array.licenses_options)
        val licensesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, licensesOptions)
        licensesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.licensesSpinner.adapter = licensesAdapter

        // --- Set up TextWatcher for howLongVendorEditText to auto-format as date ---
        setupHowLongVendorTextWatcher()

        // --- Set up DatePickerDialog for howLongVendorEditText click to allow easy date selection ---
        setupHowLongVendorPickerDialog()

        // --- Set up a click listener for the Save & Next button ---
        binding.saveNextButton.setOnClickListener {
            // Retrieve all business details input (do not trim initially for spacing validation on names/locations)
            val businessName = binding.businessNameEditText.text.toString()
            val selectedBusinessType = binding.businessTypeSpinner.selectedItem.toString()
            val locationOfCart = binding.locationCartEditText.text.toString()
            val selectedLicense = binding.licensesSpinner.selectedItem.toString()
            val howLongVendor = binding.howLongVendorEditText.text.toString().trim() // Date field

            // --- Business Name Validation ---
            if (businessName.isEmpty()) {
                Toast.makeText(this, "Please enter the business name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (businessName.trim() != businessName) {
                Toast.makeText(this, "Business Name cannot have leading or trailing spaces.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (businessName.contains("  ")) {
                Toast.makeText(this, "Business Name cannot have multiple spaces between words.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- Business Type Validation ---
            if (selectedBusinessType == resources.getString(R.string.select_business_type_hint)) {
                Toast.makeText(this, "Please select a business type.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- Location of Cart Validation ---
            if (locationOfCart.isEmpty()) {
                Toast.makeText(this, "Please enter the location of your cart.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (locationOfCart.trim() != locationOfCart) {
                Toast.makeText(this, "Location of Cart cannot have leading or trailing spaces.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (locationOfCart.contains("  ")) {
                Toast.makeText(this, "Location of Cart cannot have multiple spaces between words.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- Licenses Validation ---
            if (selectedLicense == resources.getString(R.string.select_licenses_hint)) {
                Toast.makeText(this, "Please select your licenses.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- How Long Vendor (Date) Validation ---
            if (howLongVendor.isEmpty()) {
                Toast.makeText(this, "Please enter the date you became a vendor.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isValidDateFormat(howLongVendor)) {
                Toast.makeText(this, "Please enter date in DD/MM/YYYY format.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (isFutureDate(howLongVendor)) {
                Toast.makeText(this, "Date cannot be in the future.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ If all validations pass
            Toast.makeText(this, "Business details saved (mock)! Proceeding to Bank Details...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BankDetailsActivity::class.java)
            startActivity(intent)
        }

        // --- Example: Click listener for photo upload button ---
        binding.photoCartButton.setOnClickListener {
            Toast.makeText(this, "Photo of Cart upload initiated!", Toast.LENGTH_SHORT).show()
            // TODO: Implement image selection/upload logic here
        }
    }

    // Function to set up a TextWatcher for the howLongVendorEditText
    private fun setupHowLongVendorTextWatcher() {
        binding.howLongVendorEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isFormattingDate) return
                val input = s.toString()
                var formatted = input.replace("/", "")
                if (formatted.length > 8) formatted = formatted.substring(0, 8)
                val newFormatted = StringBuilder()
                for (i in formatted.indices) {
                    newFormatted.append(formatted[i])
                    if ((i == 1 || i == 3) && i != formatted.length - 1) {
                        newFormatted.append('/')
                    }
                }
                isFormattingDate = true
                if (newFormatted.toString() != input) {
                    binding.howLongVendorEditText.setText(newFormatted.toString())
                    binding.howLongVendorEditText.setSelection(newFormatted.length)
                }
                isFormattingDate = false
            }
        })
    }

    // Function to set up a DatePickerDialog for selecting date
    private fun setupHowLongVendorPickerDialog() {
        binding.howLongVendorEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(Locale.US, "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.howLongVendorEditText.setText(formattedDate)
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    // Helper function for date validation
    private fun isValidDateFormat(dateString: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            dateFormat.isLenient = false
            dateFormat.parse(dateString)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Helper function to check if date is future
    private fun isFutureDate(dateString: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            dateFormat.isLenient = false
            val parsedDate = dateFormat.parse(dateString)
            parsedDate?.after(Calendar.getInstance().time) ?: true
        } catch (e: Exception) {
            true
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
//import com.example.login_page.databinding.ActivityBusinessDetailsBinding
//import java.text.SimpleDateFormat
//import java.util.*
//import android.app.Activity
//import android.net.Uri
//import com.google.firebase.storage.FirebaseStorage
//
//// 🔥 Firebase imports
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//
//class BusinessDetailsActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityBusinessDetailsBinding
//
//    private lateinit var storage: FirebaseStorage
//    private var imageUri: Uri? = null
//
//    // 🔥 Firebase references
//    private lateinit var auth: FirebaseAuth
//    private val database = FirebaseDatabase.getInstance().reference
//
//    private var isFormattingDate = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityBusinessDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // 🔥 Initialize Firebase
//        auth = FirebaseAuth.getInstance()
//        storage = FirebaseStorage.getInstance()
//
//        // --- Back button ---
//        binding.backButton.setOnClickListener { onBackPressed() }
//
//        // --- Setup Business Type Spinner ---
//        val businessTypeOptions = resources.getStringArray(R.array.business_type_options)
//        val businessTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, businessTypeOptions)
//        businessTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.businessTypeSpinner.adapter = businessTypeAdapter
//
//        // --- Setup Licenses Spinner ---
//        val licensesOptions = resources.getStringArray(R.array.licenses_options)
//        val licensesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, licensesOptions)
//        licensesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.licensesSpinner.adapter = licensesAdapter
//
//        // --- Setup Date handling ---
//        setupHowLongVendorTextWatcher()
//        setupHowLongVendorPickerDialog()
//
//        // --- Passport Upload Button ---
//        binding.photoCartButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, 200) // requestCode = 200
//        }
//
//        // --- Save & Next button ---
//        binding.saveNextButton.setOnClickListener {
//            val businessName = binding.businessNameEditText.text.toString()
//            val selectedBusinessType = binding.businessTypeSpinner.selectedItem.toString()
//            val locationOfCart = binding.locationCartEditText.text.toString()
//            val selectedLicense = binding.licensesSpinner.selectedItem.toString()
//            val howLongVendor = binding.howLongVendorEditText.text.toString().trim()
//
//            // ✅ Validation
//            if (businessName.isEmpty() ||
//                selectedBusinessType == resources.getString(R.string.select_business_type_hint) ||
//                locationOfCart.isEmpty() ||
//                selectedLicense == resources.getString(R.string.select_licenses_hint) ||
//                howLongVendor.isEmpty() ||
//                !isValidDateFormat(howLongVendor) ||
//                isFutureDate(howLongVendor)
//            ) {
//                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if (imageUri == null) {
//                Toast.makeText(this, "Please upload your Passport photo.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // 🔥 Upload Passport Image first
//            val storageRef = storage.reference.child("passport_images/${System.currentTimeMillis()}.jpg")
//            storageRef.putFile(imageUri!!)
//                .addOnSuccessListener {
//                    storageRef.downloadUrl.addOnSuccessListener { uri ->
//                        val userId = auth.currentUser?.uid ?: database.push().key!!
//
//                        // ✅ Create business data object
//                        val businessData = BusinessDetails(
//                            businessName,
//                            selectedBusinessType,
//                            locationOfCart,
//                            selectedLicense,
//                            howLongVendor,
//                            uri.toString() // ✅ Save passport URL
//                        )
//
//                        // ✅ Save in Realtime Database
//                        database.child("users").child(userId).child("businessDetails")
//                            .setValue(businessData)
//                            .addOnSuccessListener {
//                                Toast.makeText(this, "✅ Business details + Passport stored", Toast.LENGTH_SHORT).show()
//
//                                val intent = Intent(this, BankDetailsActivity::class.java)
//                                intent.putExtra("userId", userId)
//                                startActivity(intent)
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(this, "❌ Failed: ${it.message}", Toast.LENGTH_SHORT).show()
//                            }
//                    }
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "❌ Passport upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//    // --- Get selected passport image ---
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
//            imageUri = data?.data
//            // 🚫 No preview (since you don’t want it)
//        }
//    }
//
//    // --- Date formatting watcher ---
//    private fun setupHowLongVendorTextWatcher() {
//        binding.howLongVendorEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable?) {
//                if (isFormattingDate) return
//                val input = s.toString()
//                var formatted = input.replace("/", "")
//                if (formatted.length > 8) formatted = formatted.substring(0, 8)
//                val newFormatted = StringBuilder()
//                for (i in formatted.indices) {
//                    newFormatted.append(formatted[i])
//                    if ((i == 1 || i == 3) && i != formatted.length - 1) newFormatted.append('/')
//                }
//                isFormattingDate = true
//                if (newFormatted.toString() != input) {
//                    binding.howLongVendorEditText.setText(newFormatted.toString())
//                    binding.howLongVendorEditText.setSelection(newFormatted.length)
//                }
//                isFormattingDate = false
//            }
//        })
//    }
//
//    // --- Date picker ---
//    private fun setupHowLongVendorPickerDialog() {
//        binding.howLongVendorEditText.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//            val datePickerDialog = DatePickerDialog(
//                this,
//                { _, selectedYear, selectedMonth, selectedDay ->
//                    val formattedDate = String.format(Locale.US, "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
//                    binding.howLongVendorEditText.setText(formattedDate)
//                },
//                year, month, day
//            )
//            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
//            datePickerDialog.show()
//        }
//    }
//
//    private fun isValidDateFormat(dateString: String): Boolean {
//        return try {
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//            dateFormat.isLenient = false
//            dateFormat.parse(dateString)
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    private fun isFutureDate(dateString: String): Boolean {
//        return try {
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
//            dateFormat.isLenient = false
//            val parsedDate = dateFormat.parse(dateString)
//            parsedDate?.after(Calendar.getInstance().time) ?: true
//        } catch (e: Exception) {
//            true
//        }
//    }
//}
//
//// 🔥 Data class with passportUrl
//data class BusinessDetails(
//    val businessName: String? = null,
//    val businessType: String? = null,
//    val locationOfCart: String? = null,
//    val license: String? = null,
//    val howLongVendor: String? = null,
//    val passportUrl: String? = null // ✅ Passport Image URL
//)
