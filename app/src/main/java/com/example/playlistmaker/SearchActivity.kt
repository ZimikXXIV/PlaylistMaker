package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView

class SearchActivity : AppCompatActivity() {

    private lateinit var editTextViewSearch: EditText
    private lateinit var textViewMedialibrary: TextView

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }


    fun init_search(): Unit {

        textViewMedialibrary = findViewById<TextView>(R.id.btnBack)

        textViewMedialibrary.setOnClickListener{
            finish()
        }


        editTextViewSearch = findViewById<EditText>(R.id.edtxtSearch)

        editTextViewSearch.onRightDrawableClicked { it.text.clear() }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                editTextViewSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.search_icon,
                    0,
                    R.drawable.clear_icon,
                    0
                )
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // empty
            }

            override fun afterTextChanged(s: Editable?) {
                if (editTextViewSearch.text.isNullOrEmpty()) {
                    editTextViewSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.search_icon,
                        0,
                        0,
                        0
                    )
                } else {
                    editTextViewSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.search_icon,
                        0,
                        R.drawable.clear_icon,
                        0
                    )
                }
            }
        }

        editTextViewSearch.addTextChangedListener(simpleTextWatcher)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        init_search();

    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
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

}