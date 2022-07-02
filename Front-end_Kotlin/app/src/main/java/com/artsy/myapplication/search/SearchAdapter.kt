package com.artsy.myapplication.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artsy.myapplication.databinding.SearchItemBinding
import com.artsy.myapplication.network.ArtsyArtist

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class SearchAdapter :
    ListAdapter<ArtsyArtist, SearchAdapter.ArtistViewHolder>(DiffCallback) {

    /**
     * The MarsPhotosViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [ArtsyArtist] information.
     */
    class ArtistViewHolder(
        private var binding: SearchItemBinding
        ): RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: ArtsyArtist) {
            binding.artist = artist
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * [ArtsyArtist] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ArtsyArtist>(){
        override fun areItemsTheSame(oldItem: ArtsyArtist, newItem: ArtsyArtist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArtsyArtist, newItem: ArtsyArtist): Boolean {
            return oldItem.title == newItem.title
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return ArtistViewHolder(
            SearchItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artistData = getItem(position)
        holder.bind(artistData)
    }

}