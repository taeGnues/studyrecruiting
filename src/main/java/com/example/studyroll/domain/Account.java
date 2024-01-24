package com.example.studyroll.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id") // 무한히 서로 참조할 수 있기 때문에 id만 사용함.
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken; // 이메일 검증 시 사용하는 토큰 값.

    private LocalDateTime joinedAt; // 가입날짜

    private String bio; // 소개란

    private String url;

    private String occupation;

    private String location; // 사는 지역 String은 기본 varchar(255)임.

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;
    // profile image는 varchar(255)보다 커질 수 있으므로 TEXT인 @Lob을 사용하는 것이 좋음.
    // 그리고 기본적으로 프로필 가져올 시 이미지를 즉시 가져오므로 EAGER 타입 사용하기

    // 스터디가 만들어졌는지 받는 여부
    private boolean studyCreatedByEmail;
    private boolean studyCreatedByWeb;

    // 가입 신청 결과 이메일이나 웹으로 알림 받기
    private boolean studyEnrollmentResultByEmail;
    private boolean studyEnrollmentResultByWeb;

    // 업데이트 결과 이메일이나 웹으로 알림 받기
    private boolean studyUpdatedByEmail;
    private boolean studyUpdatedByWeb;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
