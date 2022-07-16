package com.moonz.securitypractice.entity;

import com.moonz.securitypractice.common.Role;
import lombok.*;
import javax.persistence.*;
@Entity
@Table(name="MEMBER")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Member extends BaseTime  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD", length = 60)
    private String password;

    @Column(name="ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
