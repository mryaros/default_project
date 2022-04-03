package com.psu.econom.aspirant.default_project.user_auth.service

import com.psu.econom.aspirant.default_project.user_auth.dao.UserDao
import com.psu.econom.aspirant.default_project.user_auth.entity.User
import com.psu.econom.aspirant.default_project.user_auth.exception.UserException
import com.psu.econom.aspirant.default_project.user_auth.exception.error.EUserError
import com.psu.econom.aspirant.default_project.user_auth.util.UserAuthMapper
import com.psu.econom.aspirant.sso_server.sso_consumer_api.user.pojo.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService : UserDetailsService {
    @Autowired
    private lateinit var userDao: UserDao

    @Throws(UserException::class)
    override fun loadUserByUsername(username: String): User {
        return userDao.findByUsername(username) ?: throw UserException(EUserError.USER_NOT_FOUND_ERROR)
    }

    @Throws(UserException::class)
    fun getUser(userId: String): User = userDao.findById(userId).orElse(null)
            ?: throw UserException(EUserError.USER_NOT_FOUND_ERROR)

    @Throws(UserException::class)
    fun addUser(platformUser: UserResponse): User {
        if (userDao.findByUsername(platformUser.username!!) != null) {
            throw UserException(EUserError.USER_ALREADY_EXIST_ERROR)
        }

        val newUser = UserAuthMapper.INSTANCE.platformUserToUser(platformUser)

        return userDao.save(newUser)
    }
}