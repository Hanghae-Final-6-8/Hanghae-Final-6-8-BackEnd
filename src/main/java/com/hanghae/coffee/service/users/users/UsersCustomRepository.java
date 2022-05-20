package com.hanghae.coffee.service.users.users;

import com.hanghae.coffee.dto.users.CountInfoByUserDto;
import com.hanghae.coffee.dto.users.UserAuthDto;

public interface UsersCustomRepository {

    CountInfoByUserDto getCountInfoByUser(Long userId);

    UserAuthDto getUserAuth(Long userId);

}
