package com.service.applehip.util

import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.json.JSONObject
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*

class GraphqlTester {
    companion object {
        /**
         * graphql Test
         * Header 에 JSON_UTF8 세팅
         * HTTP 통신 테스트
         * response 의 body 가 null 인지 테스트이후 return body
         * companion object 는 java의 static과 비슷함, 하지만 java에서 kotlin의 companion object를 사용하려면
         * JvmStatic 을 붙여줘야함.
         */
        @JvmStatic fun graphqlTest(query : String, restTemplate : TestRestTemplate, url : String) : String {
            println("query : $query")
            //요청 데이터를 JSONObject에 담기
            val queryJson = JSONObject().also { it.put("query", query) }

            //헤더 셋팅
            val httpHeaders = HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON_UTF8 }  // json 형식으로 던져야함
            val httpEntity : HttpEntity<String> = HttpEntity(queryJson.toString(), httpHeaders) //header와 함께 보내기 위함.


            val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url, httpEntity, String::class.java)

            //응답코드 테스트
            Assertions.assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)   // http 통신 OK
            val body = responseEntity.body
            println(body)
            //응답바디 데이터 유무 확인
            MatcherAssert.assertThat(body, IsNull.notNullValue()) // return value가 null이면 안됨
            return body!!
        }
    }
}