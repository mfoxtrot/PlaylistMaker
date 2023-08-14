package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SAVED_SEARCH_STRING = "SAVED_SEARCH_STRING"
    }

    lateinit var etSearchBox: EditText
    private var searchString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearchBox = findViewById(R.id.search_box)

        val btnBack = findViewById<ImageView>(R.id.back_button)
        btnBack.setOnClickListener() {
            finish()
        }

        val textWatcher = object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setEndDrawableVisibility(!s.isNullOrEmpty())
                searchString = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //Empty
            }
        }

        etSearchBox.addTextChangedListener(textWatcher)

        etSearchBox.setOnTouchListener { v, event ->
            var result = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        result = true
                        v.text.clear()
                        searchString=""
                    }
                }
            }
            result
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SEARCH_STRING, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SAVED_SEARCH_STRING,"").toString()
        etSearchBox.setText(searchString)
    }
    private fun setEndDrawableVisibility(isVisible: Boolean) {
        val leftDrawable = getDrawable(R.drawable.ic_search)
        val rightDrawable = if (isVisible) getDrawable(R.drawable.ic_close) else null
        etSearchBox.setCompoundDrawablesWithIntrinsicBounds(
            leftDrawable,
            null,
            rightDrawable,
            null)
    }

}