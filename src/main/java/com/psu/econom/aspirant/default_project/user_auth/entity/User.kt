package com.psu.econom.aspirant.default_project.user_auth.entity

import org.hibernate.annotations.DynamicUpdate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity(name = "t_user")
@DynamicUpdate
class User(
        @Id
        @Column(nullable = false, unique = true)
        var id: String,
        @Column(nullable = false)
        var fullName: String,
        @Column(nullable = false, unique = true)
        private var username: String,
        private var role: String? = null,

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], targetEntity = Token::class)
        var tokens: Collection<Token>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<GrantedAuthority> {
        return if (this.role.isNullOrEmpty()) mutableListOf() else mutableListOf(SimpleGrantedAuthority(this.role))
    }

    override fun getPassword(): String = ""

    override fun getUsername(): String = username
    fun setUsername(username: String) {
        this.username = username
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}