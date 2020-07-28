package com.example.itunesapp2.navigation

import androidx.fragment.app.Fragment
import com.example.itunesapp2.model.entity.ResultCollectionResponse
import com.example.itunesapp2.view.DetailFragment
import com.example.itunesapp2.view.MainFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object MainScreen: SupportAppScreen() {
        override fun getFragment() = MainFragment()
    }

    data class DetailScreen(val collectionResponse: ResultCollectionResponse.CollectionResponse): SupportAppScreen() {
        override fun getFragment() = DetailFragment.newInstance(collectionResponse)
    }

}