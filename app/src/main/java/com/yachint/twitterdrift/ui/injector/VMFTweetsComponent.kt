package com.yachint.twitterdrift.ui.injector

import com.yachint.twitterdrift.ui.modules.VMFTweetsModule
import com.yachint.twitterdrift.ui.viewmodelfactory.TweetsViewModelFactory
import dagger.Component

@Component (modules = [VMFTweetsModule::class])
interface VMFTweetsComponent {
    fun getViewModelFactory(): TweetsViewModelFactory
}