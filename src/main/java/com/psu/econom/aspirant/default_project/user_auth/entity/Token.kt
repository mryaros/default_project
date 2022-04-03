package com.psu.econom.aspirant.default_project.user_auth.entity

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity(name = "t_token")
data class Token(
        @Id
        @Column
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        var id: String,
        @ManyToOne
        @JoinColumn(name = "userId", nullable = false, referencedColumnName = "id")
        var user: User,
        @Column(nullable = false)
        var token: String,
        @Column(nullable = false)
        var refreshToken: String,
        @Column(nullable = false)
        var platformTokenObject: String
)