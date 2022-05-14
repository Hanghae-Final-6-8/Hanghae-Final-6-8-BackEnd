package com.hanghae.coffee.security;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserDetailsServiceImpl(
        UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserDetails loadUserByUsername(String username){
        Users users = usersRepository.findAllByAuthId(username)
                .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, "해당 사용자를 찾을 수 없습니다."));

        return new UserDetailsImpl(users);
    }
}
