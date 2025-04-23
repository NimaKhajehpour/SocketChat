package com.nima.socketchat.model.user

import java.util.UUID

data class LocalUser(
    val uuid: UUID,
    val username: String
)
