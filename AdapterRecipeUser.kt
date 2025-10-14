package cooking.rapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookmagic.filter.FilterRecipe
import com.example.cookmagic.activities.RecipeActivity
import com.example.cookmagic.databinding.RowRecipeUserBinding  // You'll need to create this layout
import com.example.cookmagic.models.ModelRecipe

class AdapterRecipeUser : RecyclerView.Adapter<AdapterRecipeUser.HolderRecipe>, Filterable {

    private val context: Context
    public var recipeArrayList: ArrayList<ModelRecipe>
    private var filterList: ArrayList<ModelRecipe>
    private var filter: FilterRecipe? = null

    private lateinit var binding: RowRecipeUserBinding

    constructor(context: Context, recipeArrayList: ArrayList<ModelRecipe>) : super() {
        this.context = context
        this.recipeArrayList = recipeArrayList
        this.filterList = recipeArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecipe {
        binding = RowRecipeUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderRecipe(binding.root)
    }

    override fun onBindViewHolder(holder: HolderRecipe, position: Int) {
        // Get data
        val model = recipeArrayList[position]
        val id = model.id
        val title = model.title
        val description = model.description
        val imageUrl = model.img
        val cookingTime = "30 mins" // You might want to add this to your ModelRecipe

        // Set data to views
        holder.titleTv.text = title ?: "No Title"
        holder.timeTv.text = cookingTime
        holder.descriptionTv.text = description ?: "No description"

        // Load image using Glide
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.pizza_sample)
                .error(R.drawable.pizza_sample)
                .into(holder.recipeIv)
        }

        // Handle item click
        holder.itemView.setOnClickListener {
            try {
                val intent = Intent(context, RecipeActivity::class.java)
                intent.putExtra("id", model.id)
                intent.putExtra("img", model.img)
                intent.putExtra("tittle", model.title)
                intent.putExtra("des", model.description)
                intent.putExtra("ing", model.ingredients)
                intent.putExtra("categoryId", model.categoryId)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Error opening recipe: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }


        holder.binding.menuBtn.setOnClickListener { view ->
            showOptionsMenu(model, view)
        }

        private fun showOptionsMenu(recipe: ModelRecipe, view: View) {
            val popup = android.widget.PopupMenu(context, view)
            popup.menuInflater.inflate(R.menu.recipe_options_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        val intent = Intent(context, RecipeEditActivity::class.java)
                        intent.putExtra("recipeId", recipe.id)
                        context.startActivity(intent)
                        true
                    }
                    R.id.menu_delete -> {
                        // Show delete confirmation dialog
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return recipeArrayList.size
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterRecipe(filterList, this)
        }
        return filter as FilterRecipe
    }

    fun updateList(filteredList: ArrayList<ModelRecipe>) {
        recipeArrayList = filteredList
        notifyDataSetChanged()
    }

    inner class HolderRecipe(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = binding.titleTv
        val timeTv = binding.timeTv
        val descriptionTv = binding.descriptionTv
        val recipeIv = binding.recipeIv
    }
}