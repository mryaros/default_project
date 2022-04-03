package com.psu.econom.aspirant.default_project.user_auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

class TokenDto(
        @ApiModelProperty(example = "8f566b52-2602-11ec-9621-0242ac130002")
        @JsonProperty
        var userId: String,
        @ApiModelProperty(example = "<JWT access token string here>")
        @JsonProperty
        var accessToken: String,
        @ApiModelProperty(example = "<JWT refresh token string here>")
        @JsonProperty
        var refreshToken: String,
        @ApiModelProperty(value = "Время жизни access токена", example = "3600")
        @JsonProperty
        var expiresIn: Long,
        @ApiModelProperty(value = "Время жизни refresh токена", example = "3600")
        @JsonProperty
        var refreshExpiresIn: Long,
)