package www.FallenKnowledge.com.sv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ResourceAdapter(private val resources: List<Resource>) : RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {
    private var onItemClick: OnItemClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titlerv: TextView = view.findViewById(R.id.Title)
        val descriptionrv: TextView = view.findViewById(R.id.Description)
        val imagerv: ImageView = view.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resource_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resource = resources[position]
        if (resource.title == ""){
            holder.titlerv.text = "No Title found"
        }else{
            holder.titlerv.text = resource.title
        }

        if (resource.description == ""){
            holder.descriptionrv.text = "No Description found"
        }else{
            holder.descriptionrv.text = resource.description
        }

        Picasso.get().load(resource.image).into(holder.imagerv)


        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(resource)
        }
    }

    override fun getItemCount(): Int {
        return resources.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClick = listener
    }

    interface OnItemClickListener {
        fun onItemClick(resource: Resource)
    }
}