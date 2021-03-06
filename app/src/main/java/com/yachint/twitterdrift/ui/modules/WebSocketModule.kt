package com.yachint.twitterdrift.ui.modules

import android.content.Context
import com.yachint.twitterdrift.data.DriftDatabase
import com.yachint.twitterdrift.data.dao.TrendsDao
import com.yachint.twitterdrift.data.retrofit.repository.RetroTrendsRepository
import com.yachint.twitterdrift.ui.viewmodelfactory.SocketViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class WebSocketModule(
    private val context: Context
) {
    @Provides
    fun provideTrendsDao(): TrendsDao = DriftDatabase.getInstance(context).trendsDao()

    @Provides
    fun provideSearchRepository(trendsDao: TrendsDao):
            RetroTrendsRepository = RetroTrendsRepository.getInstance(trendsDao)

    @Provides
    fun provideSocketViewModelFactory(retroTrendsRepository: RetroTrendsRepository):
            SocketViewModelFactory = SocketViewModelFactory(retroTrendsRepository)
}