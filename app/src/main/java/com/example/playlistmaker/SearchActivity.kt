package com.example.playlistmaker

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private companion object {
        const val SAVED_SEARCH_STRING = "SAVED_SEARCH_STRING"
        const val LAST_SEARCH_STRING = "LAST_SEARCH_STRING"
    }

    private lateinit var binding: ActivitySearchBinding

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesSearchService = retrofit.create(ItunesSearchApiService::class.java)

    private val trackList: ArrayList<Track> = arrayListOf()
    private val adapter = TracksAdapter(trackList)

    private var searchString = ""
    private var lastSearchString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener() {
            finish()
        }

        binding.searchList.adapter = adapter

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
                        trackList.clear()
                        adapter.notifyDataSetChanged()
                        hideKeyboard(v)
                        searchString=""
                    }
                }
            }
            result
        }

        binding.searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                lastSearchString = searchString
                findTracks(searchString)
                true
            }
            false
        }

        binding.btnRefresh.setOnClickListener{
            binding.searchBox.setText(lastSearchString)
            binding.searchBox.setSelection(lastSearchString.length)
            findTracks(lastSearchString)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SEARCH_STRING, searchString)
        outState.putString(LAST_SEARCH_STRING, lastSearchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SAVED_SEARCH_STRING,"").toString()
        lastSearchString = savedInstanceState.getString(LAST_SEARCH_STRING,"").toString()
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

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun findTracks(text: String) {
        itunesSearchService
            .searchSongs(text)
            .enqueue(object :Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            val results = response.body()?.results
                            if (results?.isNotEmpty() == true) {
                                trackList.clear()
                                trackList.addAll(results)
                                adapter.notifyDataSetChanged()
                                showResult(SearchResultType.OK)
                            } else {
                                showResult(SearchResultType.NO_RESULTS)
                            }
                        }
                        else -> showResult(SearchResultType.ERROR)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showResult(SearchResultType.ERROR)
                }

            })
    }

    fun showResult(type: SearchResultType){
        binding.searchList.isVisible = (type == SearchResultType.OK)
        binding.noResults.isVisible = (type == SearchResultType.NO_RESULTS)
        binding.noConnection.isVisible = (type == SearchResultType.ERROR)
    }
}
