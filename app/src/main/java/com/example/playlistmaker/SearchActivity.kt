package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton

class SearchActivity : AppCompatActivity() {

    private lateinit var editTextViewSearch: EditText
    private lateinit var btnClear: ImageButton
    private lateinit var btnBack: ImageButton
    var searchSavedText: String = String()


    fun initSearch() {

        btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }


        btnClear = findViewById<ImageButton>(R.id.btnClear)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                editTextViewSearch.text.clear()
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

                    btnClear.visibility = View.INVISIBLE
                } else {
                    searchSavedText = editTextViewSearch.text.toString()

                    btnClear.visibility = View.VISIBLE
                }
            }
        }

        editTextViewSearch.addTextChangedListener(simpleTextWatcher)


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