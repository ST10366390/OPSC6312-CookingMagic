package cooking.rapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cooking.rapp.activities.RecipeActivity
import cooking.rapp.databinding.SearchRvBinding  // Fixed binding name
import cooking.rapp.models.ModelRecipe

class SearchAdapter(  // Fixed class name spelling
    private var dataList: ArrayList<ModelRecipe>,
    private val context: Context
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            //get data from model
            val model = dataList[position]

            //set data to views
            holder.binding.searchTxt.text = model.title ?: "No Title"

            //load image using glide
            model.img?.let { imageUrl ->
                if (imageUrl.isNotEmpty()) {
                    Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.pizza_sample) // Use your existing drawable
                        .error(R.drawable.pizza_sample) // Use your existing drawable
                        .into(holder.binding.searchImg)
                }
            }

            //handle item click
            holder.itemView.setOnClickListener {
                try {
                    val intent = Intent(context, RecipeActivity::class.java)
                    //pass recipe data to next activity - FIXED parameter names
                    intent.putExtra("id", model.id)
                    intent.putExtra("img", model.img)
                    intent.putExtra("tittle", model.title)  // Fixed typo
                    intent.putExtra("des", model.description)
                    intent.putExtra("ing", model.ingredients)
                    intent.putExtra("categoryId", model.categoryId) // Add categoryId
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error opening recipe: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: Exception) {
            Toast.makeText(context, "Error loading recipe", Toast.LENGTH_SHORT).show()
        }

        holder.binding.menuBtn.setOnClickListener { view ->
            showOptionsMenu(model, view)
        }

         fun showOptionsMenu(recipe: ModelRecipe, view: View) {
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: ArrayList<ModelRecipe>) {
        dataList = newList
        notifyDataSetChanged()
    }
}