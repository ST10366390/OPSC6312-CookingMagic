package cooking.rapp

import android.widget.Filter
import android.widget.Filterable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cooking.rapp.models.ModelCategory

class AdapterCategory(
    private val context: android.content.Context,
    private var categoryList: ArrayList<ModelCategory>
) : RecyclerView.Adapter<AdapterCategory.HolderCategory>(), Filterable {

    private var filterList: ArrayList<ModelCategory> = ArrayList(categoryList)

    inner class HolderCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTv: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        val view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return HolderCategory(view)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = categoryList[position]
        holder.categoryTv.text = model.category
    }

    override fun getItemCount(): Int = categoryList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    results.values = filterList
                } else {
                    val query = constraint.toString().lowercase()
                    val filtered = filterList.filter {
                        it.category.lowercase().contains(query)
                    }
                    results.values = ArrayList(filtered)
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoryList = results?.values as ArrayList<ModelCategory>
                notifyDataSetChanged()
            }
        }
    }
}
