package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.domain.model.PlayerConst
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.api.TrackListClickListenerInterface
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.model.SearchStatus
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.example.playlistmaker.search.ui.state.SearchState
import com.example.playlistmaker.utils.BindingFragment
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BindingFragment<FragmentSearchBinding>(), TrackListClickListenerInterface {

    private val trackAdapter = TrackAdapter(this)
    private val historyAdapter = TrackAdapter(this)

    private val searchViewModel by viewModel<SearchViewModel>()

    private val trackList = ArrayList<Track>()
    private var trackHistoryArray: ArrayList<Track> = ArrayList()

    private lateinit var trackClickDebounce: (Track) -> Unit
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackClickDebounce = debounce<Track>(
            SearchConst.CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            searchViewModel.addToTrackHistoryWithSave(track)
            openPlayer(track)
        }

        setupAdapters()
        setEvents()

        searchViewModel.getSearchLiveData().observe(viewLifecycleOwner) { searchState ->
            updateView(searchState)
        }

        binding.edtxtSearch.requestFocus()
    }

    private fun setupAdapters() {

        trackAdapter.setTrackList(trackList)
        binding.recyclerTrackList.layoutManager =
            LinearLayoutManager(binding.recyclerTrackList.context)
        binding.recyclerTrackList.adapter = trackAdapter

        historyAdapter.setTrackList(trackHistoryArray)
        binding.recyclerHistoryList.layoutManager =
            LinearLayoutManager(binding.recyclerHistoryList.context)
        binding.recyclerHistoryList.adapter = historyAdapter

    }
    private var isClickAllowed = false

    override fun onClick(track: Track) {
        trackClickDebounce(track)
    }

    private fun openPlayer(track: Track) {
        val intent = Intent(activity, AudioPlayerActivity::class.java)
        intent.putExtra(PlayerConst.TRACK_INFO, track)
        activity?.startActivity(intent)
        isClickAllowed = true
    }
    private fun setEvents() {

        binding.btnRefresh.setOnClickListener {
            searchViewModel.searchDebounce(binding.edtxtSearch.text.toString())
        }

        binding.btnClearHistory.setOnClickListener {
            searchViewModel.clearHistory()
        }

        binding.btnClear.setOnClickListener {
            binding.edtxtSearch.text.clear()
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.edtxtSearch.windowToken, 0)
            searchViewModel.searchDebounce(binding.edtxtSearch.text.toString())
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnClear.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchViewModel.searchDebounce(binding.edtxtSearch.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

                if (binding.edtxtSearch.text.isNullOrEmpty()) {

                    binding.btnClear.visibility = View.GONE
                } else {
                    searchViewModel.saveSearchText(binding.edtxtSearch.text.toString())

                    binding.btnClear.visibility = View.VISIBLE
                }
            }
        }

        binding.edtxtSearch.addTextChangedListener(simpleTextWatcher)

        binding.edtxtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE
                && binding.edtxtSearch.text.isNotEmpty()
            ) {
                searchViewModel.searchDebounce(binding.edtxtSearch.text.toString())
            }
            false
        }
    }

    fun showViewStatus(
        visibleType: SearchStatus
    ) {
        binding.layoutEmptyTrackList.isVisible = false
        binding.layoutBadConnection.isVisible = false
        binding.recyclerTrackList.isVisible = false
        binding.layoutHistory.isVisible = false
        binding.layoutProgressBar.isVisible = false
        when (visibleType) {
            SearchStatus.PROGRESS_BAR -> binding.layoutProgressBar.isVisible = true
            SearchStatus.SEARCH_HISTORY -> binding.layoutHistory.isVisible = true
            SearchStatus.SEARCH_RESULT -> binding.recyclerTrackList.isVisible = true
            SearchStatus.ERROR_BAD_CONNECTION -> binding.layoutBadConnection.isVisible = true
            SearchStatus.ERROR_EMPTY -> binding.layoutEmptyTrackList.isVisible = true
            else -> {}
        }
    }

    fun updateView(state: SearchState) {
        when (state) {
            is SearchState.History -> {
                updateHistoryList(state.historyList)
                showViewStatus(state.status)
            }

            is SearchState.Search -> {
                updateTrackList(state.searchList)
                showViewStatus(state.status)
            }

            is SearchState.Loading -> {
                showViewStatus(state.status)
            }

            is SearchState.Error -> {
                showViewStatus(state.status)
            }

            is SearchState.Empty -> {
                showViewStatus(state.status)
            }

            is SearchState.ClearHistory -> {
                showViewStatus(state.status)
                updateHistoryList(ArrayList<Track>())
            }
            else -> {}
        }

    }


    fun updateHistoryList(historyTrackList: List<Track>) {
        historyAdapter.setTrackList(historyTrackList)
        historyAdapter.notifyDataSetChanged()
    }

    fun updateTrackList(trackList: List<Track>) {
        trackAdapter.setTrackList(trackList)
        trackAdapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        searchViewModel.onDestroyView()
    }
    override fun onStop() {
        super.onStop()
        searchViewModel.saveTrackHistory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            SearchConst.SEARCH_TEXT,
            binding.edtxtSearch.text.toString()
        )
    }


}
