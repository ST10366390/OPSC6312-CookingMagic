package cooking.rapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import cooking.rapp.databinding.ActivityDashUserBinding
import cooking.rapp.models.ModelCategory
import cooking.rapp.models.RecipeUserFragment

class DashUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashUserBinding

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    // Category list
    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    // Adapter
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        setupWithViewPagerAdapter(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        // Logout button
        binding.LogoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupWithViewPagerAdapter(viewPager: ViewPager) {
        viewPagerAdapter = ViewPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            this
        )

        // Initialize list
        categoryArrayList = ArrayList()

        // Load categories from Firebase
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()

                // Add static categories
                val modelAll = ModelCategory("01", "All", 1, "")
                val modelMostViewed = ModelCategory("02", "Most Viewed", 1, "")
                val modelMostDownloaded = ModelCategory("03", "Most Downloaded", 1, "")

                categoryArrayList.add(modelAll)
                categoryArrayList.add(modelMostViewed)
                categoryArrayList.add(modelMostDownloaded)

                // Add default fragments
                viewPagerAdapter.addFragment(
                    RecipeUserFragment.newInstance(modelAll.id, modelAll.category, modelAll.uid),
                    modelAll.category
                )
                viewPagerAdapter.addFragment(
                    RecipeUserFragment.newInstance(modelMostViewed.id, modelMostViewed.category, modelMostViewed.uid),
                    modelMostViewed.category
                )
                viewPagerAdapter.addFragment(
                    RecipeUserFragment.newInstance(modelMostDownloaded.id, modelMostDownloaded.category, modelMostDownloaded.uid),
                    modelMostDownloaded.category
                )

                // Add dynamic categories from Firebase
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCategory::class.java)
                    if (model != null) {
                        categoryArrayList.add(model)
                        viewPagerAdapter.addFragment(
                            RecipeUserFragment.newInstance(model.id, model.category, model.uid),
                            model.category
                        )
                    }
                }

                // Notify adapter and attach to ViewPager
                viewPagerAdapter.notifyDataSetChanged()
                viewPager.adapter = viewPagerAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Optional: log or show error
            }
        })
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            binding.subTitleTv.text = "Not Logged In"
        } else {
            binding.subTitleTv.text = firebaseUser.email
        }
    }

    // ViewPagerAdapter class
    class ViewPagerAdapter(
        fm: FragmentManager,
        behavior: Int,
        private val context: Context
    ) : FragmentPagerAdapter(fm, behavior) {

        // Holds fragments and titles
        private val fragmentsList: ArrayList<RecipeUserFragment> = ArrayList()
        private val fragmentTitleList: ArrayList<String> = ArrayList()

        override fun getCount(): Int = fragmentsList.size

        override fun getItem(position: Int): Fragment = fragmentsList[position]

        override fun getPageTitle(position: Int): CharSequence = fragmentTitleList[position]

        fun addFragment(fragment: RecipeUserFragment, title: String) {
            fragmentsList.add(fragment)
            fragmentTitleList.add(title)
        }
    }
}
