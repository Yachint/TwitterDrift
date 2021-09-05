package com.yachint.twitterdrift.data.repository

import com.yachint.twitterdrift.data.common.RepositoryListener

interface TweetsRepository {
    fun fetchTrends(term: String, repositoryListener: RepositoryListener)
}