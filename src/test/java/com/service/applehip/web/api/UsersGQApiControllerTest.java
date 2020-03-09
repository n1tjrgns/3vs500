package com.service.applehip.web.api;

import com.service.applehip.domain.users.Users;
import com.service.applehip.domain.users.UsersRepository;
import com.service.applehip.util.GraphqlTester;
import com.service.applehip.web.dto.user.UsersSaveRequestDto;
import com.service.applehip.web.dto.user.UsersUpdateRequestDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                    properties = "spring.profiles.active=local")
public class UsersGQApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsersRepository usersRepository;

    //테스트가 끝난 후 작업을 나타내는 어노테이션
    @After
    public void tearDown() throws Exception{
        usersRepository.deleteAll();
    }

    @Test
    public void 회원정보_저장() throws Exception {
        System.out.println("port : "+ port);
        String name = "test";
        String email = "test@test.com";
        String password = "123";

        String url = "http://localhost:"+port+"/graphql";

        UsersSaveRequestDto requestDto = UsersSaveRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        String queryName = "saveUser";  //쿼리 이름

        //GraphQL 요청 데이터
        String query = "mutation {" +          // 데이터를 변경하는 작업은 mutation 이 들어감
                " "+queryName+" (" +                // query 명  @GraphQLMutation 참조
                " request : {" +               // 변수 명   @GraphQLArgument 참조
                " name:\""+requestDto.getName()+"\", " +
                " email:\""+requestDto.getEmail()+"\", " +
                " password:\""+requestDto.getPassword()+"\" })}";

        String body = GraphqlTester.graphqlTest(query, restTemplate, url);
        JSONObject dataJson = (JSONObject) new JSONObject(body).get("data");

        //insert 결과 data 값 테스트
        assertThat(dataJson.get("saveUser")).isNotNull();
        //assertThat(dataJson.get("saveUser")).isEqualTo(1); //auto-increment 문제로 아래처럼 테스트 변경
        assertThat(Integer.parseInt(String.valueOf(dataJson.get("saveUser")))).isGreaterThan(0);

        //DB 적재된 데이터 테스트
        List<Users> all = usersRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
        assertThat(all.get(0).getEmail()).isEqualTo(email);
        assertThat(all.get(0).getPassword()).isEqualTo(password);

    }

    @Test
    public void 회원정보_수정() throws JSONException {
        //given
        Users savedUsers = usersRepository.save(Users.builder()
                .name("test")
                .email("test@test.com")
                .password("123")
                .build());


        String modyfiyingPassword = "456";

        UsersUpdateRequestDto requestDto = UsersUpdateRequestDto.builder()
                .password(modyfiyingPassword)
                .build();

        String url = "http://localhost:"+port+"/graphql";

        Long updateId = savedUsers.getId();
        String queryName = "updateUser";

        //GraphQL 요청 데이터
        String query = "mutation {" +          // 데이터를 변경하는 작업은 mutation 이 들어감
                " "+queryName+" (" +                // query 명  @GraphQLMutation 참조
                " userId : "+updateId+" " +               // 변수 명   @GraphQLArgument 참조
                " request : {" +
                " password:\""+requestDto.getPassword()+"\" })}";

        String body = GraphqlTester.graphqlTest(query, restTemplate, url);

        JSONObject dataJson = (JSONObject) new JSONObject(body).get("data");

        //insert 결과 data 값 테스트
        assertThat(dataJson.get("updateUser")).isNotNull();
        assertThat(Integer.parseInt(String.valueOf(dataJson.get("updateUser")))).isGreaterThan(0);

        List<Users> all = usersRepository.findAll();
        assertThat(all.get(0).getPassword()).isEqualTo(modyfiyingPassword);
    }

    @Test
    public void 회원_이름_찾기() throws JSONException {
        Users savedUsers = usersRepository.save(Users.builder()
                .name("test")
                .email("test@test.com")
                .password("123")
                .build());
        String url = "http://localhost:"+port+"/graphql";

        String queryName = "findUser";  //쿼리 이름
        Long findId = savedUsers.getId();


        String query = "query {" +          //select 는 mutation 대신 query 사용
                " "+queryName+" (" +                // query 명  @GraphQLMutation 참조
                " userId : "+findId+" " +
                " ){ " +               // 변수 명   @GraphQLArgument 참조
                " name " +
                " }}";

        //select GrapQL
        /*query {
            findUser(userId : 1	) {
		        name
    	    }
        }*/

        String body = GraphqlTester.graphqlTest(query, restTemplate, url);

        //data
        JSONObject dataJson = (JSONObject) new JSONObject(body).get("data");
        assertThat(dataJson.get(queryName)).isNotNull();

        //findUser
        JSONObject resultJson = (JSONObject) dataJson.get(queryName);
        assertThat(resultJson.get("name")).isEqualTo("test");
        assertThat(resultJson.has("email")).isFalse();
    }
}