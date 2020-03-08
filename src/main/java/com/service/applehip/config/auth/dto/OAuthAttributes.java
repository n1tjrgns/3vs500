package com.service.applehip.config.auth.dto;

import com.service.applehip.domain.users.GoogleUser;
import com.service.applehip.domain.users.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {  // 구글 사용자 정보가 업데이트 되었을 경우 update를 담당 하기 위한 클래스

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    //OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해야 한다.
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){

        //반환하는 사용자만 판단하여 분리
        if("naver".equals(registrationId)){
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    //네이버 인증 정보
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        //설정에서 알 수 있듯이 naver는 응답을 user_name_attribute: response 이렇게 줘서 구글과 약간 다름
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .picture((String)attributes.get("profile_image"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //구글 인증 정보
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){

        return OAuthAttributes.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .picture((String)attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public GoogleUser toEntity(){
        return GoogleUser.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
