package com.yachint.twitterdrift.ui.modules

import com.yachint.twitterdrift.data.retrofit.repository.RetroTweetsRepository
import com.yachint.twitterdrift.ui.viewmodelfactory.TweetsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class VMFTweetsModule {
    @Provides
    fun provideTweetsRepository():
            RetroTweetsRepository = RetroTweetsRepository()

    @Provides
    fun provideTweetsViewModelFactory(retroTweetsRepository: RetroTweetsRepository):
            TweetsViewModelFactory = TweetsViewModelFactory(retroTweetsRepository)
}