package com.example.playlistmaker.ui.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchResultType
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.FoundTracksInfo
import com.example.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val tracksInteractorImpl = Creator.provideTracksInteractorImpl()
    private lateinit var historyInteractorImpl: HistoryInteractor

    private lateinit var historyLocal: ArrayList<Track>
    private lateinit var historyAdapter: TracksAdapter

    private val trackList: ArrayList<Track> = arrayListOf()
    private val adapter = TracksAdapter(trackList) { track: Track -> clickOnTrack(track) }

    private var searchString = ""
    private var lastSearchString = ""

    private val handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null
    private val searchRunnable = Runnable { findTracks(searchString) }

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyInteractorImpl = Creator.provideHistoryInteractorImp(application as App)
        historyInteractorImpl.readHistory(
            consumer = object : HistoryInteractor.ReadHistoryConsumer{
                override fun consume(history: ArrayList<Track>) {
                    historyLocal = history
                    historyAdapter = TracksAdapter(historyLocal) {
                            track: Track -> clickOnTrack(track)
                    }
                }
            }
        )

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backButton.setOnClickListener() {
            finish()
        }

        binding.searchList.adapter = adapter
        binding.searchHistoryList.adapter = historyAdapter

        binding.searchBox.doOnTextChanged { text, _, _, _ ->
            setEndDrawableVisibility(!text.isNullOrEmpty())
            searchString = text.toString()
            binding.searchList.isVisible = false
            updateHistoryVisibility()
            searchDebounce()
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
                        binding.searchList.isVisible = false
                        historyAdapter.notifyDataSetChanged()
                        updateHistoryVisibility()
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

        binding.searchBox.setOnFocusChangeListener { _, _ ->
            updateHistoryVisibility()
        }

        binding.btnRefresh.setOnClickListener{
            binding.searchBox.setText(lastSearchString)
            binding.searchBox.setSelection(lastSearchString.length)
            findTracks(lastSearchString)
        }

        binding.btnClearHistory.setOnClickListener {
            historyInteractorImpl.clearHistory(
                consumer = object : HistoryInteractor.ClearHistoryConsumer{
                    override fun consume() {
                        historyAdapter.notifyDataSetChanged()
                        updateHistoryVisibility()
                    }
                }
            )
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
        if (text=="") return
        showResult(SearchResultType.IN_PROGRESS)
        tracksInteractorImpl.findTracks(
            searchString = text,
            consumer = object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: FoundTracksInfo) {
                    var currentRunnable = workRunnable
                    if (currentRunnable != null) {
                        handler.removeCallbacks(currentRunnable)
                    }
                    val newRunnable = Runnable {
                        if (foundTracks.isSuccess && foundTracks.data.isNotEmpty()) {
                            trackList.clear()
                            trackList.addAll(foundTracks.data)
                            adapter.notifyDataSetChanged()
                            showResult(SearchResultType.OK)
                        } else if (foundTracks.isSuccess && foundTracks.data.isEmpty()) {
                            showResult(SearchResultType.NO_RESULTS)
                        } else if (!foundTracks.isSuccess) {
                            showResult(SearchResultType.ERROR)
                        }
                    }
                    workRunnable = newRunnable
                    handler.post(newRunnable)
                }
            }
        )
    }

    private fun showResult(type: SearchResultType){
        with(binding) {
            searchList.isVisible = (type == SearchResultType.OK)
            noResults.isVisible = (type == SearchResultType.NO_RESULTS)
            noConnection.isVisible = (type == SearchResultType.ERROR)
            progressBar.isVisible = (type == SearchResultType.IN_PROGRESS)
        }
    }

    private fun updateHistoryVisibility() {
        binding.llSearchHistory.isVisible = isHistoryVisible()
    }

    private fun isHistoryVisible():Boolean {
        return binding.searchBox.hasFocus()
                && searchString.isNullOrEmpty()
                && historyLocal.isNotEmpty()
    }

    private fun clickOnTrack(track: Track) {
        if (clickDebounce()) {
            historyInteractorImpl.addTrack(
                track = track,
                consumer = object: HistoryInteractor.AddTrackConsumer{
                    override fun consume(history: ArrayList<Track>) {
                        historyLocal = history
                    }

                })
            historyAdapter.notifyDataSetChanged()
            intent = Intent(this, PlayerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchDebounce() {
        with(handler){
            removeCallbacks(searchRunnable)
            postDelayed(searchRunnable, DEBOUNCE_SEARCH_DELAY)
        }
    }

    private fun clickDebounce():Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, DEBOUNCE_CLICK_DELAY)
        }
        return current
    }

    private companion object {
        const val SAVED_SEARCH_STRING = "SAVED_SEARCH_STRING"
        const val LAST_SEARCH_STRING = "LAST_SEARCH_STRING"

        private const val DEBOUNCE_SEARCH_DELAY = 2000L
        private const val DEBOUNCE_CLICK_DELAY = 1000L
    }
}

