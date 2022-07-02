package com.artsy.myapplication.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artsy.myapplication.network.ArtsyApi
import com.artsy.myapplication.network.ArtsyArtist
import com.artsy.myapplication.network.ArtsyData
import kotlinx.coroutines.launch
import java.net.URI

class SearchViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<String>()
    private val _artistData = MutableLiveData<MutableList<ArtsyArtist>>()

    // The external immutable LiveData for the request status
    val status: LiveData<String> = _status
    val artistData: LiveData<MutableList<ArtsyArtist>> = _artistData

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    /*
    init {
        getArtsyData()
    }
    */

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [ArtsyData] [List] [LiveData].
     */
    fun getArtsyArtists(given: String) {
        viewModelScope.launch {
            try {
                val resList = ArtsyApi.retrofitService.getArtists(given)
                val artistList = mutableListOf<ArtsyArtist>()
                // Process the results: filter 'type=artist' only
                for (item:ArtsyData in resList) {
                    if (item.type != "artist"){
                        continue
                    }
                    // 'artist' shall be left
                    // Process ID from self.href
                    val artistId = URI(
                        item.otherLinks.self.href
                    ).path.split("/")[3]
                    artistList.add(ArtsyArtist(
                        title = item.title,
                        id = artistId,
                        thumbnail = item.otherLinks.thumbnail.href
                    ))
                }
                _artistData.value = artistList
                _status.value = "Success: ${artistList.size} Artsy data retrieved"
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }

}