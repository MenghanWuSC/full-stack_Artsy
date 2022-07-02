package com.artsy.myapplication.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.artsy.myapplication.R
import com.artsy.myapplication.artist.ArtistActivity
import com.artsy.myapplication.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    /**
     * Provides global access to these variables from anywhere in the app via [SearchActivity].<variable>
     * without needing to create a DetailActivity instance.
     */
    companion object {
        const val QUERY = "query"
    }

    /**
     * Local access
     */
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_search)

        // . Handle given Intent (explicit)
        // --intent.extras.getString returns String? (String or null)
        // --so toString() guarantees that the value will be a String
        val queryArtist = intent?.extras?.getString(QUERY).toString()
        this.title = queryArtist

        // . Link [SearchViewModel] with this activity
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        viewModel.getArtsyArtists(queryArtist)

        // . Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.recyclerArtistsView.adapter = SearchAdapter()

    }

    // Click listener to clicked image
    fun clickArtistDetails(view: View) {
        // Parse the target ID of artist
        try {
            val titleAndId: String = view.contentDescription.toString()
            //Toast.makeText(this, "clicked: $titleAndId", Toast.LENGTH_SHORT).show()

            // Intent (explicit) with a destination of [ArtistActivity]
            val intent = Intent(applicationContext, ArtistActivity::class.java)
            intent.putExtra(ArtistActivity.ARTIST, titleAndId)
            startActivity(intent)
        }
        catch (e: Exception) {
            Toast.makeText(this, "clickArtistDetails: ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

}