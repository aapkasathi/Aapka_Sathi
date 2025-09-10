package com.example.login_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.util.Log
import com.example.login_page.databinding.ActivityBankDetailsBinding

class BankDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBankDetailsBinding
    private val TAG = "BankDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Setup spinner
        val bankDetailsOptions = resources.getStringArray(R.array.bank_details_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bankDetailsOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.bankDetailsAvailableSpinner.adapter = adapter

        // Spinner listener
        binding.bankDetailsAvailableSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = parent?.getItemAtPosition(position).toString()
                Log.d(TAG, "Selected option: $selectedOption")

                when {
                    selectedOption.equals("YES", ignoreCase = true) -> {
                        setBankDetailsVisibility(View.VISIBLE)
                        binding.submitButton.visibility = View.VISIBLE // ✅ show button
                        Log.d(TAG, "YES selected → show fields + button")
                    }
                    selectedOption.equals("NO", ignoreCase = true) -> {
                        setBankDetailsVisibility(View.GONE)
                        binding.submitButton.visibility = View.VISIBLE // ✅ show button
                        Log.d(TAG, "NO selected → hide fields, show button")
                    }
                    else -> {
                        setBankDetailsVisibility(View.GONE)
                        binding.submitButton.visibility = View.GONE // ✅ hide everything
                        Log.d(TAG, "Hint/other selected → hide everything")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                setBankDetailsVisibility(View.GONE)
                binding.submitButton.visibility = View.GONE
            }
        }

        // Submit button click
        binding.submitButton.setOnClickListener {
            val bankDetailsAvailable = binding.bankDetailsAvailableSpinner.selectedItem.toString()
            val accountNumber = binding.accountNumberEditText.text.toString()
            val ifscCode = binding.ifscCodeEditText.text.toString()
            val accountHolderName = binding.accountHolderNameEditText.text.toString()
            val termsAccepted = binding.termsCheckBox.isChecked

            if (bankDetailsAvailable.equals("YES", ignoreCase = true)) {
                if (accountNumber.isEmpty() || ifscCode.isEmpty() || accountHolderName.isEmpty()) {
                    Toast.makeText(this, "Please fill all required bank details.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!termsAccepted) {
                    Toast.makeText(this, "Please agree to the Terms and Conditions.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Toast.makeText(this, "Bank details submitted!", Toast.LENGTH_SHORT).show()
            } else if (bankDetailsAvailable.equals("NO", ignoreCase = true)) {
                Toast.makeText(this, "Bank details skipped as per selection.", Toast.LENGTH_SHORT).show()
            }
        }

        // Passbook upload button
        binding.passbookPhotoButton.setOnClickListener {
            Toast.makeText(this, "Passbook photo upload initiated!", Toast.LENGTH_SHORT).show()
        }
    }

    // Hide/Show only the detail fields (not submit button)
    private fun setBankDetailsVisibility(visibility: Int) {
        binding.accountNumberLabel.visibility = visibility
        binding.accountNumberEditText.visibility = visibility
        binding.ifscCodeLabel.visibility = visibility
        binding.ifscCodeEditText.visibility = visibility
        binding.accountHolderNameLabel.visibility = visibility
        binding.accountHolderNameEditText.visibility = visibility
        binding.passbookPhotoLabel.visibility = visibility
        binding.passbookPhotoButton.visibility = visibility
        binding.termsCheckBox.visibility = visibility
    }
}
//package com.example.login_page
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.ImageButton
//import android.widget.Toast
//import android.util.Log // Import Log for debugging
//import com.example.login_page.databinding.ActivityBankDetailsBinding
//
//class BankDetailsActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityBankDetailsBinding
//
//    // Tag for logging
//    private val TAG = "BankDetailsActivity"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityBankDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // --- Set up a click listener for the Back button ---
//        binding.backButton.setOnClickListener {
//            onBackPressed()
//        }
//
//        // Setup Bank Details Available Spinner
//        val bankDetailsOptions = resources.getStringArray(R.array.bank_details_options)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bankDetailsOptions)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.bankDetailsAvailableSpinner.adapter = adapter
//
//        // --- Set up OnItemSelectedListener for bankDetailsAvailableSpinner ---
//        binding.bankDetailsAvailableSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedOption = parent?.getItemAtPosition(position).toString()
//                Log.d(TAG, "Selected option: $selectedOption at position: $position")
//
//                // Check if the selected option is exactly "YES"
//                if (selectedOption.equals("YES", ignoreCase = true)) { // Use equals(..., ignoreCase = true) for robustness
//                    setBankDetailsVisibility(View.VISIBLE)
//                    Log.d(TAG, "Setting bank details visibility to VISIBLE")
//                } else {
//                    // For "NO", the hint, or any other option, hide the fields
//                    setBankDetailsVisibility(View.GONE)
//                    Log.d(TAG, "Setting bank details visibility to GONE")
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // If nothing is selected (e.g., spinner loses focus without selection), hide fields
//                setBankDetailsVisibility(View.GONE)
//                Log.d(TAG, "Nothing selected, setting bank details visibility to GONE")
//            }
//        }
//
//        // Set up a click listener for the Submit button
//        binding.submitButton.setOnClickListener {
//            val bankDetailsAvailable = binding.bankDetailsAvailableSpinner.selectedItem.toString()
//            val accountNumber = binding.accountNumberEditText.text.toString()
//            val ifscCode = binding.ifscCodeEditText.text.toString()
//            val accountHolderName = binding.accountHolderNameEditText.text.toString()
//            val termsAccepted = binding.termsCheckBox.isChecked
//
//            // Perform validation ONLY if bank details are supposed to be available
//            if (bankDetailsAvailable.equals("YES", ignoreCase = true)) {
//                if (accountNumber.isEmpty() || ifscCode.isEmpty() || accountHolderName.isEmpty()) {
//                    Toast.makeText(this, "Please fill all required bank details.", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//
//                if (!termsAccepted) {
//                    Toast.makeText(this, "Please agree to the Terms and Conditions.", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//
//                Toast.makeText(this, "Bank details submitted (mock)!", Toast.LENGTH_SHORT).show()
//                // TODO: Implement actual bank details submission logic here
//            } else {
//                Toast.makeText(this, "Bank details skipped as per selection.", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // Example: Click listener for passbook photo upload button
//        binding.passbookPhotoButton.setOnClickListener {
//            Toast.makeText(this, "Passbook photo upload initiated!", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Helper function to set visibility of bank details fields
//    private fun setBankDetailsVisibility(visibility: Int) {
//        binding.accountNumberLabel.visibility = visibility
//        binding.accountNumberEditText.visibility = visibility
//        binding.ifscCodeLabel.visibility = visibility
//        binding.ifscCodeEditText.visibility = visibility
//        binding.accountHolderNameLabel.visibility = visibility
//        binding.accountHolderNameEditText.visibility = visibility
//        binding.passbookPhotoLabel.visibility = visibility
//        binding.passbookPhotoButton.visibility = visibility
//        binding.termsCheckBox.visibility = visibility
//        // ❌ Do NOT hide/show submit button here
//    }
//
//}
