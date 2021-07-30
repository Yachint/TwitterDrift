package com.yachint.twitterdrift.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TrendStateMaintainer private constructor(){
    companion object {
        const val STABLE = 0
        const val OLD = 1
        const val SYNCING = 2
        const val GLOBAL = 3
        const val REGIONAL = 4

        var stateRegional: MutableLiveData<Int> = MutableLiveData(STABLE)
        var stateGlobal: MutableLiveData<Int> = MutableLiveData(STABLE)

        @Volatile private var instance: TrendStateMaintainer? =null

        fun getInstance() =
            instance ?: synchronized(this){
                instance ?: TrendStateMaintainer()
            }
    }

    fun setState(type: Int, state: Int){
        if(type == GLOBAL){
            Log.d("MAINTAINER", "setState: GLOBAL - $state")
            stateGlobal.postValue(state)
        } else {
            Log.d("MAINTAINER", "setState: REGIONAL - $state")
            stateRegional.postValue(state)
        }
    }

    fun getState(type: Int): LiveData<Int> {
        return if(type == GLOBAL){
            stateGlobal
        } else {
            stateRegional
        }
    }
}