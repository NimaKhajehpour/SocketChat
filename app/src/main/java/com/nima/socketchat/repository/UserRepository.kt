package com.nima.socketchat.repository

import com.nima.socketchat.database.UserDao
import com.nima.socketchat.model.user.LocalUser
import com.nima.socketchat.model.user.UserEntity
import com.nima.socketchat.utils.UserIdentityManager
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class UserRepository(
    private val userDao: UserDao,
    private val identityManager: UserIdentityManager
) {
    suspend fun getCurrentUser(): LocalUser = identityManager.getUser()

    suspend fun setUsername(username: String) = identityManager.setUsername(username)

    suspend fun saveUser(user: UserEntity) = userDao.insertUser(user)

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun getUserById(id: UUID): UserEntity? = userDao.getUserById(id)
}
