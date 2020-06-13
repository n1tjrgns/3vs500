package com.service.applehip.domain.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {//extends BaseTimeEntity //회원정보 클래스

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //회원 Pk

    @Column(length = 20)
    private String name;    //회원 이름

    @Column(length = 50)
    private String email;   //회원 이메일

    @Column(length = 128)
    private String password;    //회원 비밀번호

    @Column
    private String picture;     //소셜 로그인을 위한 사진 경로 칼럼

    @Enumerated(EnumType.STRING)
    private Role role;          //소셜 로그인 권한

    //일반 로그인 전용 빌더
    @Builder(builderClassName = "JoinUserBuilder", buildMethodName = "JoinUserBuilder")
    public Users(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //소셜 로그인 전용 빌더
    @Builder(builderClassName = "JoinSocialUserBuilder", builderMethodName = "JoinSocialUserBuilder")
    public Users(String name, String email,  String picture, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    //update 메소드
    public void update(String password){
        this.password = password;
    }

    public Users socialInfoUpdate(String name, String picture){
        System.out.println("socialinfo update start");
        this.name = name;
        this.picture = picture;
        System.out.println("picture : " + getEmail());
        System.out.println("picture : " + getPicture());

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
