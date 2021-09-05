package com.yachint.twitterdrift.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yachint.twitterdrift.data.retrofit.repository.RetroTweetsRepository
import com.yachint.twitterdrift.ui.viewmodel.TweetsViewModel

class TweetsViewModelFactory constructor(
    private val retroTweetsRepository: RetroTweetsRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TweetsViewModel(retroTweetsRepository) as T
    }
}