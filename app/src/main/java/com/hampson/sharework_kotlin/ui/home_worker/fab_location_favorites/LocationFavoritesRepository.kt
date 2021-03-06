package com.hampson.sharework_kotlin.ui.home_worker.fab_location_favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hampson.sharework_kotlin.data.api.DBInterface
import com.hampson.sharework_kotlin.data.repository.LocationFavoritesNetworkDataSource
import com.hampson.sharework_kotlin.data.repository.NetworkState
import com.hampson.sharework_kotlin.data.vo.LocationFavorites
import io.reactivex.disposables.CompositeDisposable

class LocationFavoritesRepository (private val apiService : DBInterface) {
    lateinit var locationFavoritesDataSource: LocationFavoritesNetworkDataSource

    fun fetchSingleLocationFavorites (compositeDisposable: CompositeDisposable, userId: Int) : MutableLiveData<List<LocationFavorites>> {
        locationFavoritesDataSource = LocationFavoritesNetworkDataSource(apiService, compositeDisposable)
        locationFavoritesDataSource.fetchLocationFavorites(userId)

        return locationFavoritesDataSource.downlodedLocationFavoritesResponse
    }

    fun createLocationFavorites (compositeDisposable: CompositeDisposable, locationFavorites: LocationFavorites) : MutableLiveData<LocationFavorites> {
        locationFavoritesDataSource = LocationFavoritesNetworkDataSource(apiService, compositeDisposable)
        locationFavoritesDataSource.createLocationFavorites(locationFavorites)

        return locationFavoritesDataSource.downlodedLocationFavoriteResponse
    }

    fun deleteLocationFavorites (compositeDisposable: CompositeDisposable, id: Int) : MutableLiveData<LocationFavorites> {
        locationFavoritesDataSource = LocationFavoritesNetworkDataSource(apiService, compositeDisposable)
        locationFavoritesDataSource.deleteLocationFavorites(id)

        return locationFavoritesDataSource.downlodedLocationFavoriteResponse
    }

    fun getLocationFavoritesNetworkState(): LiveData<NetworkState> {
        return locationFavoritesDataSource.networkState
    }
}