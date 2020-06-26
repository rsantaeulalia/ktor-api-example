package com.jetbrains.handson.httpapi.repository.mongoDb

import com.jetbrains.handson.httpapi.model.AccountId
import com.jetbrains.handson.httpapi.model.AccountProfile
import com.jetbrains.handson.httpapi.repository.AccountRepository

class AccountRepositoryMongo : AccountRepository {
    override fun putAccountProfile(accountId: AccountId, accountProfile: AccountProfile) {
        TODO("Not yet implemented")
    }

    override fun getAccountProfile(accountId: AccountId): AccountProfile? {
        TODO("Not yet implemented")
    }
}