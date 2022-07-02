package com.artsy.myapplication.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.artsy.myapplication.R
import com.artsy.myapplication.databinding.FragmentArtistBinding

// Passed artist ID (from Activity - Adapter - Fragment)
class ArtistFragment(
    private val artistId: String
) : Fragment() {

    // Binding object instance with access to the views in the .xml layout
    private lateinit var binding: FragmentArtistBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same ViewModel instance created by the first fragment.
    private val viewModel: ArtistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // . View Model: func
        // *Artsy API
        viewModel.getArtsyArtistDetails(artistId)

        // . Data Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false)
        // Set the viewModel for data binding - this allows the bound layout access
        // to all the data in the VieWModel
        binding.viewModel = viewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner
        // *Adapter ??
        // ...
        // Setup a click listener for the Submit and Skip buttons.
        //binding.{id}.setOnClickListener { onSubmitWord() }
        //binding.{id}.setOnClickListener { onSkipWord() }

        return binding.root
    }

}