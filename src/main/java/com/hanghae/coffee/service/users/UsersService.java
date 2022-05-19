package com.hanghae.coffee.service.users;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.users.CountInfoByUserDto;
import com.hanghae.coffee.dto.users.CountInfoByUserResponseDto;
import com.hanghae.coffee.dto.users.UserInfoDto;
import com.hanghae.coffee.dto.users.UserInfoResponseDto;
import com.hanghae.coffee.model.Favorites;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.favorites.FavoritesRepository;
import com.hanghae.coffee.repository.users.UsersRepository;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;


    public UserInfoResponseDto getUserAuth(String authId) {

        Users user = usersRepository.findAllByAuthId(authId).orElseThrow(
            () -> new RestException(HttpStatus.UNAUTHORIZED, "Unauthorized")
        );
        UserInfoDto userInfoDto = new UserInfoDto();

        userInfoDto.setNickname(user.getNickname());
        userInfoDto.setProfile_url(user.getProfileUrl());

        return UserInfoResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(userInfoDto)
            .build();
    }

    public CountInfoByUserResponseDto getCountInfoByUser(Users users) {

        CountInfoByUserDto countInfoByUserDto = usersRepository.getCountInfoByUser(users.getId());

        return CountInfoByUserResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(countInfoByUserDto)
            .build();

    }

}
