package com.yachint.twitterdrift.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yachint.twitterdrift.data.retrofit.repository.RetroTrendsRepository
import com.yachint.twitterdrift.ui.viewmodel.TrendsViewModel

class TrendsViewModelFactory constructor(
    private val retroTrendsRepository: RetroTrendsRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrendsViewModel(retroTrendsRepository) as T
    }
}