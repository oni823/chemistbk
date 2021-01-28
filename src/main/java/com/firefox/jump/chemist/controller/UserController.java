package com.firefox.jump.chemist.controller;

import com.alibaba.fastjson.JSONObject;
import com.firefox.jump.chemist.config.Oauth2Properties;
import com.firefox.jump.chemist.model.User;
import com.firefox.jump.chemist.repo.UserRepo;
import com.firefox.jump.chemist.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Controller
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Oauth2Properties oauth2Properties;

    @GetMapping("/authorize")
    public String authorize() {
        String url = oauth2Properties.getAuthorizeUrl() +
                "?client_id=" + oauth2Properties.getClientId() +
                "&redirect_uri=" + oauth2Properties.getRedirectUrl();
        log.info("授权url:{}", url);
        return "redirect:" + url;
    }

    @GetMapping("/oauth2/callback")
    public String callback(@RequestParam("code") String code) {
        log.info("code={}", code);
        String accessToken = getAccessToken(code);
        if(accessToken != null){
            getOrCreateUser(accessToken);
        }
        String token = "";
        User user = this.getOrCreateUser(accessToken);
        if(user != null){
            token = JwtTokenProvider.generateToken(user.getId());
        }
        //TODO URL to be changed
//        return "redirect:http://localhost:8080/home/map?jwt=" + token;
        return "redirect:http://www.dadenglish.com/home/map?jwt=" + token;
    }

    private User getOrCreateUser(String accessToken){
        String url = oauth2Properties.getUserInfoUrl();
        log.info("getUserInfo url:{}", url);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("accept", "application/json");
        requestHeaders.add("Authorization", "token " + accessToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String userInfo = response.getBody();
        JSONObject jsonObject = JSONObject.parseObject(userInfo);
        String name = jsonObject.getString("login");
        String githubId = "" + jsonObject.getString("id");
        User user = null;
        if(!userRepo.existsByGithubId(githubId)){
            user =  userRepo.saveAndFlush(User.builder().user_name(name).githubId(githubId).build());
        }
        Optional<User> opt = userRepo.findByGithubId(githubId);
        if(opt.isPresent()){
            user = opt.get();
        }
        return user;
    }


    private String getAccessToken(String code) {
        String url = oauth2Properties.getAccessTokenUrl() +
                "?client_id=" + oauth2Properties.getClientId() +
                "&client_secret=" + oauth2Properties.getClientSecret() +
                "&code=" + code +
                "&grant_type=authorization_code";
        log.info("getAccessToken url:{}", url);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("accept", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        String responseStr = response.getBody();
        log.info("responseStr={}", responseStr);

        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        String accessToken = jsonObject.getString("access_token");
        log.info("accessToken={}", accessToken);
        return accessToken;
    }

}
