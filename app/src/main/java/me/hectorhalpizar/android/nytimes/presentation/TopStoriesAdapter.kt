package me.hectorhalpizar.android.nytimes.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_article.view.*
import me.hectorhalpizar.android.nytimes.R
import me.hectorhalpizar.core.nytimes.domain.Article
import me.hectorhalpizar.core.nytimes.update

class TopStoriesAdapter(
    private val cachedTopStories: MutableList<Article> = mutableListOf()
) : RecyclerView.Adapter<TopStoriesAdapter.ViewHolder>() {

    fun update(newFeed: MutableList<Article>) {
        cachedTopStories.update(newFeed)
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
        val list = cachedTopStories.toList()
        holder.title.text = list[position].title
        holder.description.text = list[position].abstract
    }

    override fun getItemCount(): Int = cachedTopStories.size
}