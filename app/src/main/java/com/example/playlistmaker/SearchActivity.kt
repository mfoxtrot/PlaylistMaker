package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private companion object {
        const val SAVED_SEARCH_STRING = "SAVED_SEARCH_STRING"
    }

    private lateinit var binding: ActivitySearchBinding

    private var searchString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener() {
            finish()
        }

        binding.searchBox.doOnTextChanged { text, _, _, _ ->
            setEndDrawableVisibility(!text.isNullOrEmpty())
            searchString = text.toString()
        }

        binding.searchBox.setOnTouchListener { v, event ->
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
        binding.searchBox.setText(searchString)
    }
    private fun setEndDrawableVisibility(isVisible: Boolean) {
        val leftDrawable = getDrawable(R.drawable.ic_search)
        val rightDrawable = if (isVisible) getDrawable(R.drawable.ic_close) else null
        binding.searchBox.setCompoundDrawablesWithIntrinsicBounds(
            leftDrawable,
            null,
            rightDrawable,
            null)
    }

}