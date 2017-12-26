package com.teste.twitterlike.api;

import com.google.gson.Gson;
import com.teste.twitterlike.api.dao.UserRepository;
import com.teste.twitterlike.api.model.Role;
import com.teste.twitterlike.api.model.Tweet;
import com.teste.twitterlike.api.model.User;
import com.teste.twitterlike.api.service.UserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ExceptionTypeFilter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ruhandosreis on 26/12/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ApplicationTests {

    @Autowired
    UserRepository repository;

    @Autowired
    UserService userService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        repository.deleteAll();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();

        final List<Role> roles = Arrays.asList(new Role("USER"), new Role("ACTUATOR"));
        final User user = new User("teste", "teste", roles);
        user.addTweet( new Tweet( "Teste tweet" )  );

        userService.save( user );
    }

    @Test
    public void testUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tweets"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testTweets() throws Exception {
        final String accessToken = obtainAccessToken("teste", "teste");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tweets")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tweet", is("Teste tweet")));
    }

    @Test
    public void testTweetV1() throws Exception {
        final String accessToken = obtainAccessToken("teste", "teste");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tweet")
                .content("Tweet V1")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testTweetV2() throws Exception {
        final String accessToken = obtainAccessToken("teste", "teste");

        final Map content = new HashMap<>();
        content.put("tweet", "Tweet V2");

        final Gson gson = new Gson();
        final String json = gson.toJson(content);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/tweet")
                .contentType( MediaType.APPLICATION_JSON )
                .content( json )
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/tweets")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testTweetLimit() throws Exception {
        final String accessToken = obtainAccessToken("teste", "teste");

        String tweet = "";
        for( int i = 0; i < 150; i++ ) {
            tweet += "a";
        }

        final Map content = new HashMap<>();
        content.put("tweet", tweet);

        final Gson gson = new Gson();
        final String json = gson.toJson(content);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/tweet")
                .contentType( MediaType.APPLICATION_JSON )
                .content( json )
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "api-twitter-like");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("api-twitter-like","secret"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
