package com.yachint.twitterdrift.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yachint.twitterdrift.data.common.DatabaseListener
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.model.BaseModel
import com.yachint.twitterdrift.data.model.response.trends.TrendsObject
import com.yachint.twitterdrift.data.model.response.woeid.PlaceModel
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.data.retrofit.repository.RetroTrendsRepository

class TrendsViewModel(
    private val retroTrendsRepository: RetroTrendsRepository
): ViewModel(), RepositoryListener, DatabaseListener {
    private lateinit var listOfTrends: LiveData<List<Trend>>
    private var place: MutableLiveData<PlaceModel> = MutableLiveData()
    private var error: MutableLiveData<Boolean> = MutableLiveData(false)

    fun trendsList(): LiveData<List<Trend>> = listOfTrends
    fun getPlace(): LiveData<PlaceModel> = place

    fun getTrendsList(){
        listOfTrends = retroTrendsRepository.roomGetTrends()
    }

    fun deleteTrendsList(){
        retroTrendsRepository.roomDeleteTrends()
    }

    fun insertTrends(trends: List<Trend>){
        retroTrendsRepository.roomInsertTrends(trends, this)
    }

    fun fetchTrendsList(woeid: Int){
        retroTrendsRepository.fetchTrends(woeid, this)
    }

    fun fetchPlaceId(lat: Double, long: Double){
        retroTrendsRepository.fetchPlaceId(lat, long, this)
    }

    override fun onSuccess(dataModel: BaseModel) {
        when(dataModel){
            is TrendsObject -> {
                Log.d("OBSERVER", "API Fetch Complete, Putting in DB...")
                insertTrends(dataModel.trends)
            }

            is PlaceModel -> {
                place.value = dataModel
            }
        }
        error.value = false
    }

    override fun onFailure(t: Throwable) {
        error.value = true
    }

    fun getErrorIdentifier(): LiveData<Boolean> = error

    override fun onQueryComplete(type: String) {
        when(type){
            "INSERT" -> {
                Log.d("OBSERVER", "onQueryComplete: Insert, refreshing livedata form DB...")
                getTrendsList()
            }
        }
    }
}