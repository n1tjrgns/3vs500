package com.service.applehip.config.auth;

import com.service.applehip.config.auth.dto.OAuthAttributes;
import com.service.applehip.config.auth.dto.SessionUser;
import com.service.applehip.domain.users.GoogleUser;
import com.service.applehip.domain.users.GoogleUserRepository;
import com.service.applehip.domain.users.Users;
import com.service.applehip.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    //구글 로그인 이후 가져온 사용자의 정보를 기반으로 가입 및 정보수정, 세션 저장 등의 기능 담당 클래스

    private final GoogleUserRepository googleUserRepository;
    private final UsersRepository usersRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //로그인 진행 중인 서비스를 구분하는 코드, 추후 다른 SNS연동 시 어떤 로그인인지 구분하기 위해 사용됨
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //OAUTH2 로그인 진행 시 키가 되는 필드값, PK 라고 생각하면됨
        //구글의 경우 기본적으로 코드(="sub")를 지원하지만, 네이버 카카오 등은 지원하지 않음
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                    .getUserInfoEndpoint().getUserNameAttributeName();

        System.out.println("registrationId : " + registrationId);
        System.out.println("userNameAttributeName : " + userNameAttributeName);

        //OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        System.out.println("attributesEmail : " + attributes.getEmail());

        //구글 사용자 정보가 업데이트 되었을 때를 대비한 update 기능
        //사용자의 이름, 프로필 사진이 변경되면 GoogleUser Entity에도 반영
        // TODO users 엔티티에 저장되도록
        GoogleUser googleUser = null;
        Users oriUser =  null;
        if("sub".equals(userNameAttributeName)){ //구글로그인일 경우
            googleUser = saveOrUpdate(attributes);
            httpSession.setAttribute("user", new SessionUser(googleUser));
            return getoAuth2User(attributes, googleUser);
        }else{ //네이버 로그인일경우
            oriUser = saveOrUpdates(attributes);
            httpSession.setAttribute("user", new SessionUser(oriUser));
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(oriUser.getRoleKey())),
                    attributes.getAttributes(),
                    attributes.getNameAttributeKey()
            );
        }



        // 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        /*   기존 GoogleUser 클래스를 사용하지 않고 새로운 SessionUser라는 클래스를 만들어서 사용 하는 이유*/
        //  만약 기존의 GoogleUser 클래스를 그대로 사용시 발생 될 에러
        //  -> Failed to conver from type [java.lang.Object] to type [byte[]] for value 'com.service.applehip.domain.users.GooglerUser

        //  GoogleUser 클래스를 세션에 저장하기 위한 목적인데 직렬화를 구현하지 않았다는 에러
        //  -> GoogleUser 클래스에 구현 하면되잖아??

        //  No No, GoogleUser는 엔티티임. 그래서 언제 다른 엔티티와 또 관계가 설정 될 수 있음 (ex : oneToMany, ManyToMany)
        //  그럼 직렬화 대상에 자식들까지 포함 될 수 있어, 성능 이슈, 부수 효과가 발생 할 확률이 높음
        //  그래서 새로 만들어야 함




    }

    @NotNull
    private OAuth2User getoAuth2User(OAuthAttributes attributes, GoogleUser googleUser) {
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(googleUser.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private Users saveOrUpdates(OAuthAttributes attributes) {
        Users users = usersRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.socialInfoUpdate(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntityUser());

        return usersRepository.save(users);
    }


    private GoogleUser saveOrUpdate(OAuthAttributes attributes){
        GoogleUser googleUser = googleUserRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        System.out.println(googleUser.getEmail());
        System.out.println(googleUser.getPicture());
        return googleUserRepository.save(googleUser);
    }


    /*private Users saveOrUpdate(OAuthAttributes attributes){

        System.out.println("saveOrUpdate 메소드");
        System.out.println(attributes.getPicture());
        System.out.println(attributes.getPicture());

        //람다식 안쓴 버전
        *//*Users googleUser = usersRepository.findByEmail(attributes.getEmail());
        System.out.println("googleUser 객체 : "+ googleUser);
        System.out.println("googleUser 객체 : "+ googleUser.getEmail());
        if(googleUser != null){
            googleUser.socialInfoUpdate(attributes.getName(), attributes.getPicture());
        }

        attributes.toEntity();

        return usersRepository.save(googleUser);*//*
        *//*Users googleUser = usersRepository.findByEmail(attributes.getEmail())
                //여길 왜 안탈까??
                .map(entity -> entity.socialInfoUpdate(attributes.getName(), attributes.getPicture()))
                //orElse가 Optional 메소드 중 하나이기 때문에 repository에서 Optional로 감싸줘야됨.
                //객체의 null 여부와 상관없이 안의 메소드를 실행시킨다.
                .orElse(attributes.toEntity());

        System.out.println("attribute picture : " + attributes.getPicture());
        System.out.println("googleuser picture : " + googleUser.getEmail());
        System.out.println("googleuser picture : " + googleUser.getPicture());

        return usersRepository.save(googleUser);*//*

        *//*Users googleUser = usersRepository.findByEmail(attributes.getEmail());
        System.out.println("googleUser.getEmail() : "+ googleUser.getEmail());
        if(googleUser.getEmail() == null){
            attributes.toEntity();
        }else{
            googleUser.socialInfoUpdate(attributes.getName(), attributes.getPicture());
        }*//*
        *//*Users googleUser = usersRepository.findByEmail(attributes.getEmail())
                .map(entity ->{
                    System.out.println("entity : " + entity);
                    return entity.socialInfoUpdate(attributes.getName(), attributes.getPicture());
                })
                .orElse(attributes.toEntity());
        return usersRepository.save(googleUser);*//*
    }*/
}
