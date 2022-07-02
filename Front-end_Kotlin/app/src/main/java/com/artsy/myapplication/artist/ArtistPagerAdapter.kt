package com.artsy.myapplication.artist

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.artsy.myapplication.R

private val TAB_TITLES = arrayOf(
    R.string.tab_details,
    R.string.tab_artwork
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class ArtistPagerAdapter(private val context: Context, given: String, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    // Passed given artist ID
    private val artistId: String = given

    override fun getItem(position: Int): Fragment {
        // 'getItem' is called to instantiate the fragment for the given page.
        // Return each Fragment as needed.
        return when (position) {
            0 -> {
                println("#MENG# ArtistPagerAdapter:getItem $position")
                ArtistFragment(artistId)
            }
            1 -> {
                println("#MENG# ArtistPagerAdapter:getItem $position")
                ArtworkFragment(artistId)
            }
            else -> {
                // Undefined behavior: default fragment [0]
                // *TEST
                println("#MENG# ArtistPagerAdapter:getItem *else* $position")
                ArtistFragment(artistId)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }

}