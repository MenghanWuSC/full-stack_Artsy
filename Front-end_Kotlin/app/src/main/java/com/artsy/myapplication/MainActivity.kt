package com.artsy.myapplication

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.asLiveData
import com.artsy.myapplication.artist.ArtistActivity
import com.artsy.myapplication.data.SettingsDataStore
import com.artsy.myapplication.data.dataStore
import com.artsy.myapplication.search.SearchActivity

// Global & const access
const val URL_ARTSY = "https://www.artsy.net/"

class MainActivity : AppCompatActivity() {

    // DataStore
    private lateinit var settingsDataStore: SettingsDataStore

    // Keeps track of whether a artist is starred as favorite
    private var favoriteArtist = setOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash Screen
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the toolbar view inside the activity layout.
        // Set the Toolbar to act as the ActionBar for this Activity window.
        //setSupportActionBar(findViewById(R.id.toolbar_main))

        // Intent (implicit) with URI parsing
        findViewById<TextView>(R.id.credit_artsy).setOnClickListener{
            val queryUrl: Uri = Uri.parse(URL_ARTSY)
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            startActivity(intent)
        }

        // Initialize SettingsDataStore & Set text by view
        val textView: TextView = findViewById(R.id.favorite_00)
        settingsDataStore = SettingsDataStore(applicationContext.dataStore)
        settingsDataStore.preferenceFlow.asLiveData().observe(this) { value ->
            if (value.isNotEmpty()) {
                favoriteArtist = value
                textView.setText(value.elementAt(0))
            }
            else {
                //textView.setText("Default: none")
                textView.setBackgroundColor(Color.parseColor("#ffffff"))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu)

        // Configure the search info and add any event listeners
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                // Intent (explicit) with a destination of [SearchActivity]
                val intent = Intent(applicationContext, SearchActivity::class.java)
                intent.putExtra(SearchActivity.QUERY, query)
                startActivity(intent)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            // User chose the "Search" item, show the app settings UI...
            //Toast.makeText(applicationContext, "Search", Toast.LENGTH_SHORT).show()
            true
        }
        R.id.action_star_border -> {
            // User chose the "Star" item, mark the current item
            // as a favorite...
            Toast.makeText(applicationContext, "Star_border", Toast.LENGTH_SHORT).show()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    // Click listener to clicked image
    fun clickArtistDetails(view: View) {
        try {
            if (favoriteArtist.isNotEmpty()) {
                // Intent (explicit) with a destination of [ArtistActivity]
                val intent = Intent(applicationContext, ArtistActivity::class.java)
                intent.putExtra(
                    ArtistActivity.ARTIST,
                    "${favoriteArtist.elementAt(0)}_${favoriteArtist.elementAt(1)}"
                )
                startActivity(intent)
            }
        }
        catch (e: Exception) {
            Toast.makeText(this, "clickArtistDetails: ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

}