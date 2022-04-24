package me.hectorhalpizar.android.news.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article.view.*
import me.hectorhalpizar.android.news.R
import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.update

class TopStoriesAdapter(
    private val cachedTopStories: MutableList<Article> = mutableListOf()
) : RecyclerView.Adapter<TopStoriesAdapter.ViewHolder>() {

    fun update(newFeed: MutableList<Article>) {
        cachedTopStories.update(newFeed)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val imageView: ImageView = view.article_image
        internal val title = view.article_title
        internal val description = view.article_description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = cachedTopStories.toList()[position]
        val photo = list.multimedia?.firstOrNull()

        Glide
            .with(holder.imageView.rootView)
            .load(photo?.url)
            .centerCrop()
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imageView)

        holder.title.text = list.title
        holder.description.text = list.abstract
    }

    override fun getItemCount(): Int = cachedTopStories.size
}