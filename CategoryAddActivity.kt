package cooking.rapp

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import cooking.rapp.databinding.ActivityCategoryAddBinding

class CategoryAddActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivityCategoryAddBinding

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    // Category text
    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configure Progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        // Handle Back Button
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Handle Submit Button
        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        // Get text from EditText
        category = binding.categoryEt.text.toString().trim()

        // Validate input
        if (category.isEmpty()) {
            Toast.makeText(this, "Enter category...", Toast.LENGTH_SHORT).show()
        } else {
            addCategoryFirebase()
        }
    }

    private fun addCategoryFirebase() {
        // Show progress
        progressDialog.setMessage("Adding category...")
        progressDialog.show()

        // Get timestamp
        val timestamp = System.currentTimeMillis()

        // Setup data to add in Firebase
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = timestamp.toString()
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = firebaseAuth.uid!!

        // Reference to Firebase Realtime Database
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child(timestamp.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Added successfully!", Toast.LENGTH_SHORT).show()
                binding.categoryEt.text?.clear()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
