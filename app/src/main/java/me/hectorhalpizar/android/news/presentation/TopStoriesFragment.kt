package me.hectorhalpizar.android.news.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_top_stories.*
import kotlinx.android.synthetic.main.fragment_top_stories.view.*
import me.hectorhalpizar.android.news.R
import me.hectorhalpizar.android.news.framework.NewsViewModelFactory
import me.hectorhalpizar.core.news.domain.Section

private const val TAG = "TopStoriesFragment"

class TopStoriesFragment : BaseFragment() {
    private var section: String? = null
    private lateinit var viewModel: TopStoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            section = it.getString(SECTION_EXTRA)
        } ?: throw IllegalArgumentException("A valid section must be provided")
    }

    override fun onStart() {
        super.onStart()
        fetch()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_top_stories, container, false).also { v ->
        activity?.let {
            val adapter = TopStoriesAdapter()
            viewModel = NewsViewModelFactory.create(TopStoriesViewModel::class.java).apply {
                fetchingState.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is TopStoriesViewModel.RequestState.Successful -> {
                            adapter.update(state.topStories)
                            top_stories_recycler_view.visibility = View.VISIBLE
                            failed_fetch_top_story.visibility = View.GONE
                        }
                        is TopStoriesViewModel.RequestState.Failed -> {
                            Log.e(TAG, "Error: ${state.cause}")
                            top_stories_recycler_view.visibility = View.GONE
                            failed_fetch_top_story.visibility = View.VISIBLE
                        }
                        else -> {
                            Log.v(TAG, "Unsupervised state: $state")
                        }
                    }
                }
            }
            v.top_stories_recycler_view.apply {
                this.adapter = adapter
                visibility = View.GONE
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        (layoutManager as LinearLayoutManager).orientation
                    ).apply {
                        this@TopStoriesFragment.activity?.getDrawable(R.drawable.divider)
                            ?.let { it1 -> setDrawable(it1) }
                    }
                )
            }

            v.failed_fetch_top_story.visibility = View.GONE
            v.failed_fetch_top_story_textview.text =
                String.format(
                    getText(R.string.failed_fetch_top_story_message).toString(),
                    (activity as AppCompatActivity?)?.supportActionBar?.title ?: section
                )
            v.retry_fetch_top_stories.setOnClickListener { fetch() }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh) {
            fetch()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetch() = section?.let { viewModel.fetch(Section.valueOf(it)) }

    companion object {
        private const val SECTION_EXTRA = "section"
    }
}