package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged

class SearchActivity : AppCompatActivity() {
    private companion object {
        const val SAVED_SEARCH_STRING = "SAVED_SEARCH_STRING"
    }

    private lateinit var etSearchBox: EditText
    private var searchString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearchBox = findViewById(R.id.search_box)

        val btnBack = findViewById<ImageView>(R.id.back_button)
        btnBack.setOnClickListener() {
            finish()
        }

        etSearchBox.doOnTextChanged { text, _, _, _ ->
            setEndDrawableVisibility(!text.isNullOrEmpty())
            searchString = text.toString()
        }

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