package com.yachint.twitterdrift.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yachint.twitterdrift.data.common.DatabaseListener
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.model.BaseModel
import com.yachint.twitterdrift.data.model.response.trends.TrendsMainResponse
import com.yachint.twitterdrift.data.model.response.woeid.PlaceModel
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.data.retrofit.repository.RetroTrendsRepository

class TrendsViewModel(
    private val retroTrendsRepository: RetroTrendsRepository
): ViewModel(), RepositoryListener, DatabaseListener {
//    private lateinit var listOfTrends: LiveData<List<Trend>>
    private lateinit var globalTrends: LiveData<List<Trend>>
    private lateinit var regionalTrends: LiveData<List<Trend>>
    private var place: MutableLiveData<PlaceModel> = MutableLiveData()
    private var requestsPending = MutableLiveData(0)
    private var error: MutableLiveData<Boolean> = MutableLiveData(false)

//    fun trendsList(): LiveData<List<Trend>> = listOfTrends
    fun globalTrendsList(): LiveData<List<Trend>> = globalTrends
    fun regionalTrendsList(): LiveData<List<Trend>> = regionalTrends
    fun getPlace(): LiveData<PlaceModel> = place
    fun getPendingRequests(): LiveData<Int> = requestsPending

    private fun recordRequest(){
        Log.d("OBSERVER", "recordRequest: +1")
        requestsPending.value = requestsPending.value?.plus(1)
    }

    private fun removeRequest(){
        Log.d("OBSERVER", "recordRequest: -1")
        requestsPending.value = requestsPending.value?.minus(1)
    }

//    private fun getTrendsList(){
//        listOfTrends = retroTrendsRepository.roomGetTrends()
//    }

    fun getGlobalTrends(){
        globalTrends = retroTrendsRepository.roomGetGlobalTrends()
    }

    fun getRegionalTrends(){
        regionalTrends = retroTrendsRepository.roomGetRegionalTrends()
    }

    fun deleteTrendsList(){
        retroTrendsRepository.roomDeleteTrends()
    }

    private fun insertTrends(trends: List<Trend>){
        retroTrendsRepository.roomInsertTrends(trends, this)
    }

    fun fetchRegionalTrends(woeid: Int, limit: Int){
        retroTrendsRepository.fetchRegionalTrends(woeid, limit, this)
        recordRequest()
    }

    fun fetchGlobalTrends(limit: Int){
        retroTrendsRepository.fetchGlobalTrends(limit,this)
        recordRequest()
    }

    fun fetchPlaceId(lat: Double, long: Double){
        retroTrendsRepository.fetchPlaceId(lat, long, this)
        recordRequest()
    }

    override fun onSuccess(dataModel: BaseModel) {
        removeRequest()
        when(dataModel){
            is TrendsMainResponse -> {
                Log.d("OBSERVER", "API Fetch Complete for ${dataModel.data[0].type}, Putting in DB...")
                insertTrends(dataModel.data)
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
                getGlobalTrends()
                getRegionalTrends()
            }
        }
    }
}