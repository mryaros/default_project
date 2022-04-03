package com.psu.econom.aspirant.default_project.user_auth.util;

import com.psu.econom.aspirant.default_project.user_auth.entity.User;
import com.psu.econom.aspirant.sso_server.sso_consumer_api.user.pojo.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAuthMapper {
    UserAuthMapper INSTANCE = Mappers.getMapper(UserAuthMapper.class);

    User platformUserToUser(UserResponse userResponse);
}
