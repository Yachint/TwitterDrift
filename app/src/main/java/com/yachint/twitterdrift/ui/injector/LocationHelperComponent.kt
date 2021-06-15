package com.yachint.twitterdrift.ui.injector

import com.yachint.twitterdrift.ui.modules.LocationHelperModule
import com.yachint.twitterdrift.utils.LocationHelper
import dagger.Component

@Component (modules = [LocationHelperModule::class])
interface LocationHelperComponent {
    fun getLocationHelper(): LocationHelper
}