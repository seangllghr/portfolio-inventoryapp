package com.mobile2app.inventoryapp

class LoginRepository(private val userDao: UserDao) {

    enum class AuthResult {
        NO_LOGIN_ATTEMPT,
        OK,
        BAD_PASSWORD,
        NO_SUCH_USER,
        LOGIN_INCOMPLETE,
        USER_ADDED,
        PASSWORDS_DO_NOT_MATCH,
    }

    suspend fun addUser(username: String, password: String) {
        val userSalt = PasswordHasher.getNextSalt()
        val passwordHash = PasswordHasher.generateHash(password, userSalt)
        val newUser = User(username, passwordHash, userSalt)
        userDao.addUser(newUser)
    }

    suspend fun makeLoginAttempt(username: String, password: String): AuthResult {
        val userRecord = userDao.getUserByUsername(username)
        userRecord?.let {
            return if (PasswordHasher.generateHash(password, it.salt).equals(it.passwordHash))
                AuthResult.OK
            else
                AuthResult.BAD_PASSWORD
        } ?: return AuthResult.NO_SUCH_USER
    }

}