package me.hectorhalpizar.android.nytimes.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_top_stories.*
import kotlinx.android.synthetic.main.fragment_top_stories.view.*
import me.hectorhalpizar.android.nytimes.R
import me.hectorhalpizar.android.nytimes.framework.NyTimesViewModelFactory
import me.hectorhalpizar.core.nytimes.domain.Section

class TopStoriesFragment : Fragment() {
    private var section: String? = null
    private lateinit var viewModel: TopStoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            section = it.getString(SECTION_EXTRA)
        } ?: throw IllegalArgumentException("Section must be passed into the fragment")
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
            viewModel = NyTimesViewModelFactory.create(TopStoriesViewModel::class.java).apply {
                articles.observe(viewLifecycleOwner) {
                    adapter.update(it.values.toList())
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