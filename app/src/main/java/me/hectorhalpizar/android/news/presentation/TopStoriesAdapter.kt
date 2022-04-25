package me.hectorhalpizar.android.news.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.service.autofill.Sanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_article.view.*
import me.hectorhalpizar.android.news.R
import me.hectorhalpizar.core.news.domain.Article
import me.hectorhalpizar.core.news.update
import java.text.DateFormat

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
        internal val publishedDate = view.published_on
        internal val readMore = view.read_more
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

        article.published_date?.let {
            holder.publishedDate.text = String.format(holder.publishedDate.resources.getString(R.string.published_on), DateFormat.getDateInstance(DateFormat.MEDIUM).format(it))
        } ?: run {
            holder.publishedDate.setText(R.string.date_unavailable)
        }

        Glide
            .with(holder.imageView.rootView)
            .load(photo?.url)
            .centerCrop()
            .error(R.drawable.ic_image_not_available)
            .into(holder.imageView)

        holder.readMore.setOnClickListener { view ->
            try {
                Intent(Intent.ACTION_VIEW, Uri.parse(article.url)).also {
                    view.context.startActivity(it)
                }
            } catch (e: Exception) {
                Toast.makeText(view.context,R.string.article_not_available, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = cachedTopStories.size
}