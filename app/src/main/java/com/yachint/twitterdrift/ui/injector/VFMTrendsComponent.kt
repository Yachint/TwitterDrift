package com.yachint.twitterdrift.ui.injector

import com.yachint.twitterdrift.ui.modules.VMFTrendsModule
import com.yachint.twitterdrift.ui.viewmodelfactory.TrendsViewModelFactory
import dagger.Component

@Component (modules = [VMFTrendsModule::class])
interface VFMTrendsComponent {
    fun getViewModelFactory(): TrendsViewModelFactory
}