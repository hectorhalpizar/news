package me.hectorhalpizar.android.news.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_top_stories.view.*
import me.hectorhalpizar.android.news.R
import me.hectorhalpizar.android.news.framework.NewsViewModelFactory
import me.hectorhalpizar.core.news.domain.Section

private const val TAG = "TopStoriesFragment"

class TopStoriesFragment : Fragment() {
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
        section?.let { viewModel.fetch(Section.valueOf(it)) }
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
                        }
                        is TopStoriesViewModel.RequestState.Failed -> {
                            Log.e(TAG, "Error: ${state.cause}")
                        }
                        else -> {
                            Log.v(TAG, "Unsupervised state: $state")
                        }
                    }
                }
            }
            v.top_stories_recycler_view.adapter = adapter
        }
    }

    companion object {
        private const val SECTION_EXTRA = "section"
        fun newInstance(section: String) =
            TopStoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(SECTION_EXTRA, section)
                }
            }
    }
}