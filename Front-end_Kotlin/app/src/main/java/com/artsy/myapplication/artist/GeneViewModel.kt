package com.artsy.myapplication.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artsy.myapplication.network.ArtsyApi
import com.artsy.myapplication.network.ArtsyGene
import kotlinx.coroutines.launch

class GeneViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<String>()
    private val _artsyGeneStart = MutableLiveData<ArtsyGene>()

    // The external immutable LiveData for the request status
    val status: LiveData<String> = _status
    val artsyGeneStart: LiveData<ArtsyGene> = _artsyGeneStart

    /**
     * Get Artsy data from the API Retrofit service and updates the
     * [ArtsyGene] [LiveData].
     */
    fun getArtsyArtworkGenes(given: String) {
        viewModelScope.launch {
            try {
                _artsyGeneStart.value = ArtsyApi.retrofitService.getGenes(given)[0]
                _status.value = "Success: artwork $given retrieved"
            }
            catch (e: Exception) {
                println("#MENG# getArtsyArtworkGenes ${e.message}")
                _status.value = "Failure: ${e.message}"
            }
        }
    }

}