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
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private lateinit var editTextViewSearch: EditText
    private lateinit var btnClear: ImageButton
    private lateinit var btnBack: ImageButton
    private lateinit var layoutEmptyTrackList: LinearLayout
    private lateinit var layoutBadConnection: LinearLayout
    private lateinit var recyclerTrackList: RecyclerView
    private lateinit var btnRefresh: MaterialButton
    var searchSavedText: String = String()


    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesAPI::class.java)

    private val trackList = ArrayList<Track>()

    fun initSearch() {

        btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        layoutEmptyTrackList = findViewById(R.id.layoutEmptyTrackList)
        layoutBadConnection = findViewById(R.id.layoutBadConnection)
        recyclerTrackList = findViewById(R.id.recyclerTrackList)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnClear = findViewById(R.id.btnClear)

        btnRefresh.setOnClickListener {
            editTextViewSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }


        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                editTextViewSearch.text.clear()
                changeVisibility(false, false, false)
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(editTextViewSearch.windowToken, 0)
            }
        }
        btnClear.setOnClickListener(imageClickListener)

        editTextViewSearch = findViewById<EditText>(R.id.edtxtSearch)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                btnClear.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // empty
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


        val trackAdapter = TrackAdapter(trackList)


        recyclerTrackList.layoutManager = LinearLayoutManager(this)
        recyclerTrackList.adapter = trackAdapter


        editTextViewSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
                                    changeVisibility(true, false, false)
                                } else {
                                    changeVisibility(false, false, true)
                                }
                            } else {
                                changeVisibility(false, true, false)
                            }
                        }

                        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                            changeVisibility(false, true, false)
                        }

                    })
            }
            false
        }


    }

    fun changeVisibility(
        isEmpty: Boolean, isBadConnectin: Boolean, isShowTrackList: Boolean
    ) {
        layoutEmptyTrackList.visibility = if (isEmpty) View.VISIBLE else View.GONE
        layoutBadConnection.visibility = if (isBadConnectin) View.VISIBLE else View.GONE
        recyclerTrackList.visibility = if (isShowTrackList) View.VISIBLE else View.GONE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initSearch()

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
    }

}