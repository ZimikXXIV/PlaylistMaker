package com.example.playlistmaker.Search.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Debounce
import com.example.playlistmaker.Debounce.removeCallbacks
import com.example.playlistmaker.Search.domain.api.ConsumerData
import com.example.playlistmaker.Search.domain.api.HistoryTrackInteractor
import com.example.playlistmaker.Search.domain.api.SearchTrackInteractor
import com.example.playlistmaker.Search.domain.api.TrackConsumer
import com.example.playlistmaker.Search.domain.model.SearchConst
import com.example.playlistmaker.Search.domain.model.Track
import com.example.playlistmaker.Search.presentation.model.SearchStatus
import com.example.playlistmaker.databinding.ActivitySearchBinding

class TrackSerachController(
    private val activity: Activity,
    private val trackAdapter: TrackAdapter,
    private val historyAdapter: TrackAdapter
) {

    private val binding by lazy {
        ActivitySearchBinding.inflate(activity.layoutInflater)
    }

    private var searchSavedText: String = String()
    private val searchRunnable = Runnable { searchRequest() }
    private lateinit var trackHistory: HistoryTrackInteractor
    private lateinit var searchTrackRetrofit: SearchTrackInteractor
    private var isBadConnection: Boolean = false

    private val trackList = ArrayList<Track>()
    private var trackHistoryArray: ArrayList<Track> = ArrayList()

    fun onCreate() {
        trackAdapter.setTrackList(trackList)
        activity.setContentView(binding.root)
        initSearch()
    }

    fun onStop() {
        trackHistory.saveTrackHistory(trackHistoryArray)
    }

    fun onDestroy() {
        removeCallbacks(searchRunnable)
    }

    fun onSaveInstanceState(outState: Bundle) {

        outState.putString(
            SearchConst.SEARCH_TEXT,
            binding.edtxtSearch.getText().toString()
        )
    }

    fun onClick(track: Track) {
        trackHistory.addToTrackHistoryWithSave(track, trackHistoryArray)
        binding.recyclerTrackList.adapter?.notifyDataSetChanged()
    }

    fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        if (savedInstanceState != null) {
            binding.edtxtSearch.setText(savedInstanceState.getString(SearchConst.SEARCH_TEXT, ""))
        }
    }

    private fun setupAdapters() {

        trackAdapter.setTrackList(trackList)
        binding.recyclerTrackList.layoutManager =
            LinearLayoutManager(binding.recyclerTrackList.context)
        binding.recyclerTrackList.adapter = trackAdapter

        trackHistoryArray = trackHistory.loadTrackHistory()
        historyAdapter.setTrackList(trackHistoryArray)
        binding.recyclerHistoryList.layoutManager =
            LinearLayoutManager(binding.recyclerTrackList.context)
        binding.recyclerHistoryList.adapter = historyAdapter
    }

    private fun setEvents() {

        binding.btnBack.setOnClickListener {
            activity.finish()
        }

        binding.btnRefresh.setOnClickListener {
            Debounce.debounce(searchRunnable, SearchConst.SEARCH_DEBOUNCE_DELAY_MILLIS)
            searchRequest()
        }

        binding.btnClearHistory.setOnClickListener {
            trackHistoryArray.clear()
            binding.recyclerHistoryList.adapter?.notifyDataSetChanged()
            changeVisibility(SearchStatus.NONE)
        }

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                binding.edtxtSearch.text.clear()
                changeVisibility(SearchStatus.SEARCH_HISTORY)
                val inputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.edtxtSearch.windowToken, 0)
            }
        }

        binding.btnClear.setOnClickListener(imageClickListener)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnClear.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Debounce.debounce(searchRunnable, SearchConst.SEARCH_DEBOUNCE_DELAY_MILLIS)
            }

            override fun afterTextChanged(s: Editable?) {

                if (binding.edtxtSearch.text.isNullOrEmpty()) {

                    binding.btnClear.visibility = View.GONE
                } else {
                    searchSavedText = binding.edtxtSearch.text.toString()

                    binding.btnClear.visibility = View.VISIBLE
                }
            }
        }

        binding.edtxtSearch.addTextChangedListener(simpleTextWatcher)

        binding.edtxtSearch.setOnFocusChangeListener { v, hasFocus ->
            if (trackHistoryArray.count() > 0)
                changeVisibility(
                    SearchStatus.SEARCH_HISTORY
                )
        }

        binding.edtxtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE
                && Debounce.clickDebounce()
                && binding.edtxtSearch.text.isNotEmpty()
            ) {
                changeVisibility(SearchStatus.PROGRESS_BAR)

                isBadConnection = false
                val expression = binding.edtxtSearch.text.toString()
                searchTrackRetrofit.searchTrack(
                    expression,
                    object : TrackConsumer<List<Track>> {
                        override fun consume(consumerData: ConsumerData<List<Track>>) {
                            trackList.clear()
                            if (consumerData is ConsumerData.Error) {
                                isBadConnection = true
                            } else if (consumerData is ConsumerData.Data) {
                                trackList.addAll(consumerData.data)
                            }
                        }

                    }
                )
                if (isBadConnection) {
                    changeVisibility(SearchStatus.ERROR_BAD_CONNECTION)
                } else if (trackList.isEmpty()) {
                    changeVisibility(SearchStatus.ERROR_EMPTY)
                } else {
                    trackAdapter.notifyDataSetChanged()
                    changeVisibility(SearchStatus.SEARCH_RESULT)
                }
            }
            false
        }
    }

    private fun searchRequest() {
        binding.edtxtSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }

    private fun initSearch() {

        trackHistory = Creator.getHistoryTrackInteractor(activity.applicationContext)
        searchTrackRetrofit = Creator.getSearchTrackInteractor(activity.applicationContext)

        setupAdapters()
        setEvents()
        binding.edtxtSearch.requestFocus()
    }

    fun changeVisibility(
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
}