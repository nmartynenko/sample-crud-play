package com.aimprosoft.play.glossaries.services

import com.aimprosoft.play.glossaries.models.impl.User

trait UserService {

    def addUser(user: User)

    def getUserByEmail(username: String): User
}