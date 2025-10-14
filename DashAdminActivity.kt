package cooking.rapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import cooking.rapp.activities.WelcomeActivity
import cooking.rapp.databinding.ActivityDashAdminBinding
import cooking.rapp.models.ModelCategory
import cooking.rapp.AdapterCategory

class DashAdminActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityDashAdminBinding

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    // Category list
    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    // Adapter
    private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if user is logged in
        checkUser()

        // Load all categories from Firebase
        loadCategories()

        // Search bar listener
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterCategory.filter.filter(s)
                } catch (e: Exception) {
                    // prevent crash if adapter not yet loaded
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // no-op
            }
        })

        // Logout button
        binding.LogoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        // Add category button
        binding.addCategoryBtn.setOnClickListener {
            startActivity(Intent(this, CategoryAddActivity::class.java))
        }
    }

    /** Loads category list from Firebase Realtime Database */
    private fun loadCategories() {
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCategory::class.java)
                    model?.let { categoryArrayList.add(it) }
                }

                // Set up adapter once data is loaded
                adapterCategory = AdapterCategory(this@DashAdminActivity, categoryArrayList)
                binding.categoriesRv.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if needed
            }
        })
    }

    /** Check if a user is logged in, otherwise redirect to WelcomeActivity */
    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            // Show user email on UI
            binding.subTitleTv.text = firebaseUser.email
        }
    }
}
