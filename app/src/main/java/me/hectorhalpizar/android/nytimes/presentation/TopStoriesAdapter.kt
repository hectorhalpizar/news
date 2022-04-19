package me.hectorhalpizar.android.nytimes.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_article.view.*
import me.hectorhalpizar.android.nytimes.R
import me.hectorhalpizar.core.nytimes.domain.Article

class TopStoriesAdapter(
    private val documents: MutableList<Article> = mutableListOf()
) : RecyclerView.Adapter<TopStoriesAdapter.ViewHolder>() {

    fun update(list: List<Article>) {
        documents.clear()
        documents.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // val imageView: ImageView = view.article_image
        internal val title = view.article_title
        internal val description = view.article_description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = documents[position].title
        holder.description.text = documents[position].abstract
    }

    override fun getItemCount(): Int = documents.size
}