package org.knou.keyproject.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.knou.keyproject.domain.board.entity.Board;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.scrap.entity.Scrap;
import org.knou.keyproject.global.audit.BaseTimeEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// 2023.7.19(수) 18h25 -> 2023.7.20(목) 17h30 v1
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor/*(access = AccessLevel.PROTECTED)*/
@Entity
public class Member extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Plan> planList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Scrap> scrapList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberPlatform memberPlatform;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    private Boolean isEmailVerified;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private Integer age;
    private Integer yearOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "'ACTIVE'")
    private MemberStatus status;

    // 2023.7.21(금) 18h15 수정 = 도서관 references 책
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String profileImageUrl;

    //    @Column(nullable = false)
    private LocalDateTime lastLoginAt;

    // 2023.7.26(수) 23h20 회원 가입 구현 시 추가
    public void setPassword(String password) {
        this.password = password;
    }

    // 2023.7.27(목) 0h40 로그인 구현 시 추가
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    // 2023.10.1(일) 23h10 UserDetails 클래스(Spring Security에서 사용자의 인증 정보 담아두는 인터페이스) 상속 관련 overriding
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
