package com.example.playlistmaker.Search.presentation

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.Debounce.clickDebounce
import com.example.playlistmaker.Debounce.debounce
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.domain.api.ConsumerData
import com.example.playlistmaker.Search.domain.api.TrackConsumer
import com.example.playlistmaker.Search.domain.api.TrackListClickListenerInterface
import com.example.playlistmaker.Search.domain.impl.HistoryTrackInteractorImpl
import com.example.playlistmaker.Search.domain.model.SearchConst
import com.example.playlistmaker.Search.domain.model.Track
import com.example.playlistmaker.Search.presentation.model.SearchStatus
import com.google.android.material.button.MaterialButton


class SearchActivity : AppCompatActivity(), TrackListClickListenerInterface {

    private lateinit var editTextViewSearch: EditText
    private lateinit var btnClear: ImageButton
    private lateinit var btnBack: ImageButton
    private lateinit var layoutEmptyTrackList: LinearLayout
    private lateinit var layoutBadConnection: LinearLayout
    private lateinit var layoutHistory: LinearLayout
    private lateinit var layoutProgressBar: LinearLayout
    private lateinit var recyclerTrackList: RecyclerView
    private lateinit var recyclerHistoryList: RecyclerView
    private lateinit var btnRefresh: MaterialButton
    private lateinit var btnClearHistory: MaterialButton
    private var searchSavedText: String = String()
    private val searchRunnable = Runnable { searchRequest() }
    private lateinit var trackHistory: HistoryTrackInteractorImpl
    private val searchTrackRetrofit = Creator.provideSearchTrackInteractor()
    private var isBadConnection: Boolean = false;

    private val trackList = ArrayList<Track>()
    private var trackHistoryArray: ArrayList<Track> = ArrayList()
    override fun onClick(track: Track) {
        trackHistory.addToTrackHistoryWithSave(track, trackHistoryArray)
        recyclerHistoryList.adapter?.notifyDataSetChanged()
    }

    private fun setEvents() {

        btnBack.setOnClickListener {
            finish()
        }

        btnRefresh.setOnClickListener {
            debounce(searchRunnable, SearchConst.SEARCH_DEBOUNCE_DELAY_MILLIS)
            searchRequest()
        }

        btnClearHistory.setOnClickListener {
            trackHistoryArray.clear()
            recyclerHistoryList.adapter?.notifyDataSetChanged()
            changeVisibility(SearchStatus.NONE)
        }

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                editTextViewSearch.text.clear()
                changeVisibility(SearchStatus.SEARCH_HISTORY)
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(editTextViewSearch.windowToken, 0)
            }
        }

        btnClear.setOnClickListener(imageClickListener)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                btnClear.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                debounce(searchRunnable, SearchConst.SEARCH_DEBOUNCE_DELAY_MILLIS)
            }

            override fun afterTextChanged(s: Editable?) {

                if (editTextViewSearch.text.isNullOrEmpty()) {

                    btnClear.visibility = View.GONE
                } else {
                    searchSavedText = editTextViewSearch.text.toString()

                    btnClear.visibility = View.VISIBLE
                }
            }
        }

        editTextViewSearch.addTextChangedListener(simpleTextWatcher)

        editTextViewSearch.setOnFocusChangeListener { v, hasFocus ->
            if (trackHistoryArray.count() > 0)
                changeVisibility(
                    SearchStatus.SEARCH_HISTORY
                )
        }

        val trackAdapter = TrackAdapter(trackList, this)

        recyclerTrackList.layoutManager = LinearLayoutManager(this)
        recyclerTrackList.adapter = trackAdapter

        editTextViewSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE
                && clickDebounce()
                && editTextViewSearch.text.isNotEmpty()
            ) {
                changeVisibility(SearchStatus.PROGRESS_BAR)

                isBadConnection = false

                searchTrackRetrofit.searchTrack(
                    editTextViewSearch.text.toString(),
                    object : TrackConsumer<List<Track>> {
                        override fun consume(consumerData: ConsumerData<List<Track>>) {
                            trackList.clear()
                            if (consumerData is ConsumerData.Error) {
                                isBadConnection = true
                            } else if (consumerData is ConsumerData.Data) {
                                trackList.addAll(consumerData.data!!)
                            }
                        }

                    }
                )
                if (isBadConnection) {
                    changeVisibility(SearchStatus.ERROR_BAD_CONNECTION)
                }
                if (trackList.isEmpty()) {
                    changeVisibility(SearchStatus.ERROR_EMPTY)
                } else {
                    trackAdapter.notifyDataSetChanged()
                    changeVisibility(SearchStatus.SEARCH_RESULT)
                }
            }
            false
        }

        val historyAdapter = TrackAdapter(trackHistoryArray, null)

        recyclerHistoryList.layoutManager = LinearLayoutManager(this)
        recyclerHistoryList.adapter = historyAdapter


    }

    private fun searchRequest() {
        editTextViewSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }



    private fun initSearch() {
        trackHistory = Creator.getHistoryTrackInteractor(applicationContext)
        btnBack = findViewById(R.id.btnBack)
        layoutEmptyTrackList = findViewById(R.id.layoutEmptyTrackList)
        layoutBadConnection = findViewById(R.id.layoutBadConnection)
        layoutHistory = findViewById(R.id.layoutHistory)
        recyclerTrackList = findViewById(R.id.recyclerTrackList)
        recyclerHistoryList = findViewById(R.id.recyclerHistoryList)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnClear = findViewById(R.id.btnClear)
        editTextViewSearch = findViewById(R.id.edtxtSearch)
        btnClearHistory = findViewById(R.id.btnClearHistory)
        layoutProgressBar = findViewById(R.id.layoutProgressBar)
        trackHistoryArray = trackHistory.loadTrackHistory()

        setEvents()
        editTextViewSearch.requestFocus()
    }

    fun changeVisibility(
        visibleType: SearchStatus
    ) {
        layoutEmptyTrackList.isVisible = false
        layoutBadConnection.isVisible = false
        recyclerTrackList.isVisible = false
        layoutHistory.isVisible = false
        layoutProgressBar.isVisible = false
        when (visibleType) {
            SearchStatus.PROGRESS_BAR -> layoutProgressBar.isVisible = true
            SearchStatus.SEARCH_HISTORY -> layoutHistory.isVisible = true
            SearchStatus.SEARCH_RESULT -> recyclerTrackList.isVisible = true
            SearchStatus.ERROR_BAD_CONNECTION -> layoutBadConnection.isVisible = true
            SearchStatus.ERROR_EMPTY -> layoutEmptyTrackList.isVisible = true
            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initSearch()

    }

    override fun onStop() {
        super.onStop()
        trackHistory.saveTrackHistory(trackHistoryArray)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(
            SearchConst.SEARCH_TEXT,
            editTextViewSearch.getText().toString()
        )
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            editTextViewSearch.setText(savedInstanceState.getString(SearchConst.SEARCH_TEXT, ""))
        }
    }

}
