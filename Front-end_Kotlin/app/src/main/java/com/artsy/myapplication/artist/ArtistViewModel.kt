package com.artsy.myapplication.artist

import androidx.lifecycle.*
import com.artsy.myapplication.network.ArtsyApi
import com.artsy.myapplication.network.ArtsyArtwork
import com.artsy.myapplication.network.ArtsyDetail
import kotlinx.coroutines.launch

class ArtistViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<String>()
    private val _artistDetail = MutableLiveData<ArtsyDetail>()
    private val _artistArtworks = MutableLiveData<List<ArtsyArtwork>>()

    // The external immutable LiveData for the request status
    val status: LiveData<String> = _status
    val artistDetail: LiveData<ArtsyDetail> = _artistDetail
    val artistArtworks: LiveData<List<ArtsyArtwork>> = _artistArtworks

    /**
     * Get Artsy artist's details from the API Retrofit service and updates the
     * [ArtsyDetail] [LiveData].
     */
    fun getArtsyArtistDetails(given: String) {
        viewModelScope.launch {
            try {
                _artistDetail.value = ArtsyApi.retrofitService.getDetails(given)
                _status.value = "Success: artist $given retrieved"
            }
            catch (e: Exception) {
                println("#MENG# getArtsyArtistDetails ${e.message}")
                _status.value = "Failure: ${e.message}"
            }
        }
    }

    /**
     * Get Artsy artist's artworks from the API Retrofit service and updates the
     * [ArtsyArtwork] [LiveData].
     */
    fun getArtsyArtistArtworks(given: String) {
        viewModelScope.launch {
            try {
                _artistArtworks.value = ArtsyApi.retrofitService.getArtworks(given)
                _status.value = "Success: artworks $given retrieved"
            }
            catch (e: Exception) {
                println("#MENG# getArtsyArtistArtworks ${e.message}")
                _status.value = "Failure: ${e.message}"
            }
        }
    }

}