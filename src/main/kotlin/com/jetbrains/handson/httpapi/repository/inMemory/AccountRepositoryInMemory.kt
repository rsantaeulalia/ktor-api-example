package com.jetbrains.handson.httpapi.repository.inMemory

import com.jetbrains.handson.httpapi.model.AccountId
import com.jetbrains.handson.httpapi.model.AccountProfile
import com.jetbrains.handson.httpapi.repository.AccountRepository

class AccountRepositoryInMemory : AccountRepository {
    private val profileMap = mutableMapOf<AccountId, AccountProfile>()

    override fun putAccountProfile(accountId: AccountId, accountProfile: AccountProfile) {
        profileMap[accountId] = accountProfile
    }

    override fun getAccountProfile(accountId: AccountId): AccountProfile? {
        return profileMap[accountId]
    }
}