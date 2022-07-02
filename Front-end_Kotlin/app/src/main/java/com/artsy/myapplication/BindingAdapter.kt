package com.artsy.myapplication

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.artsy.myapplication.network.ArtsyArtist
import com.artsy.myapplication.search.SearchAdapter

/**
 * Updates the data shown in the [RecyclerView].
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: MutableList<ArtsyArtist>?) {
    val adapter = recyclerView.adapter as SearchAdapter
    adapter.submitList(data)
}

/**
 * Uses the Coil library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        if (imgUrl == "/assets/shared/missing_image.png") {
            imgView.setImageResource(R.drawable.ic_artsy_logo)
        }
        else {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image_24)
            }
        }
    }
}