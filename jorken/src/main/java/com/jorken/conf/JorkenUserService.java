package com.jorken.conf;

import com.jorken.service.betreuer.BetreuerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JorkenUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();
    private final BetreuerRepository repository;

    public JorkenUserService(BetreuerRepository betreuerRepository) {
        this.repository = betreuerRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User original = defaultService.loadUser(userRequest);

        String gitHubName = original.getAttribute("login");

        Set<GrantedAuthority> authorities = new HashSet<>(original.getAuthorities());

        if("Chettoe".equals(original.getAttribute("login"))){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        repository.findByGitHubName(gitHubName).ifPresent(betreuer -> authorities.add(new SimpleGrantedAuthority("ROLE_BETREUER")));

        return new DefaultOAuth2User(authorities, original.getAttributes(), "login");
    }
}
