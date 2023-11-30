package com.example.playlistmaker

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Debounce.clickDebounce
import com.example.playlistmaker.Debounce.debounce
import com.example.playlistmaker.TrackHistory.Companion.TRACK_HISTORY
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
    private lateinit var trackHistory: TrackHistory
    private var searchSavedText: String = String()

    private val searchRunnable = Runnable { searchRequest() }

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesAPI::class.java)

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
            debounce(searchRunnable)
            searchRequest()
        }

        btnClearHistory.setOnClickListener {
            trackHistoryArray.clear()
            recyclerHistoryList.adapter?.notifyDataSetChanged()
            changeVisibility(SearchVisibility.NONE)
        }

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                editTextViewSearch.text.clear()
                changeVisibility(SearchVisibility.SEARCH_HISTORY)
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
                debounce(searchRunnable)
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
                    SearchVisibility.SEARCH_HISTORY
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
                changeVisibility(SearchVisibility.PROGRESS_BAR)
                itunesService.search(editTextViewSearch.text.toString())
                    .enqueue(object : Callback<SearchResponse> {
                        override fun onResponse(
                            call: Call<SearchResponse>,
                            response: Response<SearchResponse>
                        ) {
                            if (response.code() == 200) {
                                trackList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.addAll(response.body()?.results!!)
                                    trackAdapter.notifyDataSetChanged()
                                }
                                if (trackList.isEmpty()) {
                                    changeVisibility(SearchVisibility.ERROR_EMPTY)
                                } else {
                                    changeVisibility(SearchVisibility.SEARCH_RESULT)
                                }
                            } else {
                                changeVisibility(SearchVisibility.ERROR_BAD_CONNECTION)
                            }
                        }

                        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                            changeVisibility(SearchVisibility.ERROR_BAD_CONNECTION)
                        }

                    })
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
        trackHistory = TrackHistory(getSharedPreferences(TRACK_HISTORY, MODE_PRIVATE))
        trackHistoryArray = trackHistory.loadTrackHistory()

        setEvents()
        editTextViewSearch.requestFocus()
    }

    fun changeVisibility(
        visibleType: SearchVisibility
    ) {
        layoutEmptyTrackList.visibility = View.GONE
        layoutBadConnection.visibility = View.GONE
        recyclerTrackList.visibility = View.GONE
        layoutHistory.visibility = View.GONE
        layoutProgressBar.visibility = View.GONE
        when (visibleType) {
            SearchVisibility.PROGRESS_BAR -> layoutProgressBar.visibility = View.VISIBLE
            SearchVisibility.SEARCH_HISTORY -> layoutHistory.visibility = View.VISIBLE
            SearchVisibility.SEARCH_RESULT -> recyclerTrackList.visibility = View.VISIBLE
            SearchVisibility.ERROR_BAD_CONNECTION -> layoutBadConnection.visibility = View.VISIBLE
            SearchVisibility.ERROR_EMPTY -> layoutEmptyTrackList.visibility = View.VISIBLE
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
            SEARCH_TEXT,
            editTextViewSearch.getText().toString()
        )
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            editTextViewSearch.setText(savedInstanceState.getString(SEARCH_TEXT, ""))
        }
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val MAX_SEARCH_COUNT = 10
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

    enum class SearchVisibility {
        PROGRESS_BAR, SEARCH_HISTORY, ERROR_EMPTY, ERROR_BAD_CONNECTION, SEARCH_RESULT, NONE
    }
}
