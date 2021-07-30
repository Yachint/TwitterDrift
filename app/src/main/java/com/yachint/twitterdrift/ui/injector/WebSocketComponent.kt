package com.yachint.twitterdrift.ui.injector

import com.yachint.twitterdrift.ui.modules.WebSocketModule
import com.yachint.twitterdrift.ui.viewmodelfactory.SocketViewModelFactory
import dagger.Component

@Component (modules = [WebSocketModule::class])
interface WebSocketComponent {
    fun getViewModelFactory(): SocketViewModelFactory
}