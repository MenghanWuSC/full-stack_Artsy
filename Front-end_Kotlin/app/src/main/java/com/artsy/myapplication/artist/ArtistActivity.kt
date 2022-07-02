package com.artsy.myapplication.artist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.artsy.myapplication.R
import com.artsy.myapplication.data.SettingsDataStore
import com.artsy.myapplication.data.dataStore
import com.artsy.myapplication.search.SearchActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class ArtistActivity : AppCompatActivity() {

    /**
     * Provides global access to these variables from anywhere in the app via [SearchActivity].<variable>
     * without needing to create a DetailActivity instance.
     */
    companion object {
        const val ARTIST = "artistId"
    }

    // DataStore
    private lateinit var settingsDataStore: SettingsDataStore

    // Intent from other Activity
    private var titleAndId = setOf<String>()

    // Keeps track of whether a artist is starred as favorite
    private var favoriteArtist = setOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        // Should be 'default' Action Bar
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // . Handle given Intent (explicit)
        // --intent.extras.getString returns String? (String or null)
        // --so toString() guarantees that the value will be a String
        val queryDetail = intent?.extras?.getString(ARTIST).toString()
        titleAndId = setOf<String>(queryDetail.split("_")[0], queryDetail.split("_")[1])
        title = titleAndId.elementAt(0)
        // *Also ready to pass queryDetail[1] to Adapter

        // [Pager Adapter] [ViewPager] [TabLayout]
        val artistPagerAdapter = ArtistPagerAdapter(
            this,
            titleAndId.elementAt(1),
            supportFragmentManager
        )
        val viewPager: ViewPager = findViewById(R.id.view_artist_pager)
        val tabs: TabLayout = findViewById(R.id.tab_artist)
        // Assign & Bind those components
        viewPager.adapter = artistPagerAdapter
        tabs.setupWithViewPager(viewPager)

        // Initialize DataStore
        settingsDataStore = SettingsDataStore(applicationContext.dataStore)
        settingsDataStore.preferenceFlow.asLiveData().observe(this) { value ->
            favoriteArtist = value
        }
    }

    /**
     * Initializes the [Menu] to be used with the current [ArtistActivity]
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_artist, menu)
        // Shall initialize the icon based on DataStore
        val menuItem = menu?.findItem(R.id.action_star_border)
        if (favoriteArtist.isEmpty()) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_24)
        }
        else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_24)
        }
        return true
    }

    /**
     * Determines how to handle interactions with the selected [MenuItem]
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_star_border -> {
                // Set [isFavoriteStarred] to the opposite boolean
                //isFavoriteStarred = !isFavoriteStarred

                // Adjust the icon accordingly
                setIcon(item)

                if (favoriteArtist.isEmpty()) {
                    // Launch a coroutine and write the layout setting in the preference Datastore
                    lifecycleScope.launch {
                        settingsDataStore.saveFavoriteToPreferencesStore(
                            setOf(titleAndId.elementAt(0), titleAndId.elementAt(1)),
                            applicationContext)
                    }
                }
                else {
                    favoriteArtist = setOf<String>()
                    // Launch a coroutine and write the layout setting in the preference Datastore
                    lifecycleScope.launch {
                        settingsDataStore.saveFavoriteToPreferencesStore(
                            setOf<String>(),
                            applicationContext)
                    }
                }

                return true
            }
            // Otherwise, do nothing and use the core event handling.
            else -> super.onOptionsItemSelected(item)
        }
    }

    // As the bottom/hardware back pressed
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    /*
    override fun onBackPressed() {
        Toast.makeText(this, "BOTTOM-BACK clicked", Toast.LENGTH_SHORT).show()
        super.onBackPressed()
    }
    */

    /**
     * Empty --> Set; Otherwise --> Remove
     */
    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        if (favoriteArtist.isEmpty()) {
            menuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_24)
            Toast.makeText(this, "${this.title} is added to favorites", Toast.LENGTH_SHORT).show()
        }
        else {
            menuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_24)
            Toast.makeText(this, "${this.title} is removed from favorites", Toast.LENGTH_SHORT).show()
        }
    }

    // Click listener to clicked image
    fun clickArtworkGenes (view: View) {
        // Parse the target ID of artist
        try {
            val artworkId: String = view.contentDescription.toString()
            //Toast.makeText(this, "clickArtworkGenes: $artworkId", Toast.LENGTH_SHORT).show()
            GeneDialogFragment(artworkId).show(supportFragmentManager, "GeneDialogFragment")
        }
        catch (e: Exception) {
            Toast.makeText(this, "clickArtworkGenes: ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

}