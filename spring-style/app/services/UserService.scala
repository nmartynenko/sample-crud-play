package services

import models.impl.User

trait UserService {

    def addUser(user: User)

    def getUserByEmail(username: String): User
}