package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Debounce
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.api.TrackListClickListenerInterface
import com.example.playlistmaker.search.domain.model.SearchConst
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.model.SearchStatus
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel.Companion.getViewModelFactory
import com.example.playlistmaker.search.ui.state.SearchState


class SearchActivity : AppCompatActivity(), TrackListClickListenerInterface {

    private val trackAdapter = TrackAdapter(this)
    private val historyAdapter = TrackAdapter(this)

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this, getViewModelFactory(this))[SearchViewModel::class.java]
    }

    private val trackList = ArrayList<Track>()
    private var trackHistoryArray: ArrayList<Track> = ArrayList()

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapters()
        setEvents()

        searchViewModel.getSearchLiveData().observe(this) { searchState ->
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
            LinearLayoutManager(binding.recyclerTrackList.context)
        binding.recyclerHistoryList.adapter = historyAdapter


    }

    private fun setEvents() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRefresh.setOnClickListener {
            searchViewModel.searchDebounce(binding.edtxtSearch.text.toString())
        }

        binding.btnClearHistory.setOnClickListener {
            searchViewModel.clearHistory()
        }

        binding.btnClear.setOnClickListener {
            binding.edtxtSearch.text.clear()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.edtxtSearch.windowToken, 0)
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
                && Debounce.clickDebounce()
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


    override fun onDestroy() {
        super.onDestroy()
        searchViewModel.onDestroy()
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

    override fun onClick(track: Track) {
        searchViewModel.addToTrackHistoryWithSave(track)
        historyAdapter.notifyDataSetChanged()
    }
    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            binding.edtxtSearch.setText(
                savedInstanceState.getString(
                    SearchConst.SEARCH_TEXT, ""
                )
            )
        }
    }


}
