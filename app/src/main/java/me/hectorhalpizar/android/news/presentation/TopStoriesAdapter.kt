package me.hectorhalpizar.android.news.presentation

import android.annotation.SuppressLint
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
        internal val abstract = view.article_description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = cachedTopStories.toList()[position]
        val photo = article.multimedia?.firstOrNull()

        if (article.title.trim().isNotBlank()) {
            holder.title.text = article.title
        } else {
            holder.title.setText(R.string.title_not_available)
        }

        if (article.abstract.trim().isNotBlank()) {
            holder.abstract.text = article.abstract
        } else {
            holder.abstract.setText(R.string.abstract_not_available)
        }

        Glide
            .with(holder.imageView.rootView)
            .load(photo?.url)
            .centerCrop()
            .error(R.drawable.ic_image_not_available)
            .into(holder.imageView)


    }

    override fun getItemCount(): Int = cachedTopStories.size
}