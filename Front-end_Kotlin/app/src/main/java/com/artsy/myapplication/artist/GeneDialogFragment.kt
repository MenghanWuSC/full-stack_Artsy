package com.artsy.myapplication.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.artsy.myapplication.R
import com.artsy.myapplication.databinding.FragmentGeneDialogBinding

// Passed artwork ID (from Activity - Fragment)
class GeneDialogFragment(
    private val artworkId: String
) : DialogFragment() {

    // Binding object instance with access to the views in the .xml layout
    private lateinit var binding: FragmentGeneDialogBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same ViewModel instance created by the first fragment.
    private val viewModel: GeneViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // . View Model: func
        // *Artsy API
        viewModel.getArtsyArtworkGenes(artworkId)

        // . Data Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gene_dialog, container, false)
        // Set the viewModel for data binding - this allows the bound layout access
        // to all the data in the VieWModel
        binding.viewModel = viewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        // Leave it to 'wrap_content'
        //val height = (resources.displayMetrics.heightPixels * 0.6).toInt()
        //dialog!!.window?.setLayout(height, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}