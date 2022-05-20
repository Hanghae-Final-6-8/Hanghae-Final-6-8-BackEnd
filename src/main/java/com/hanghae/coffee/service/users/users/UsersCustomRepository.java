package com.hanghae.coffee.service.users.users;

import com.hanghae.coffee.dto.users.CountInfoByUserDto;

public interface UsersCustomRepository {

    CountInfoByUserDto getCountInfoByUser(Long userId);

}
