package com.hanghae.coffee.service;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.users.UserInfoResponseDto;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.UsersRepository;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UsersRepository usersRepository;


    public UserInfoResponseDto getUserInfo(String authId) {
        HashMap<String, String> result = new HashMap<>();
        Users user = usersRepository.findAllByAuthId(authId).orElseThrow(
            () -> new RestException(HttpStatus.UNAUTHORIZED,"Unauthorized")
        );
        result.put("nickname", user.getNickname());

        return UserInfoResponseDto
            .builder()
            .status(200)
            .msg("success")
            .data(result)
            .build();
    }
}
