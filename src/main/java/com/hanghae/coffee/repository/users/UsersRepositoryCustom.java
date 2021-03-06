package com.hanghae.coffee.repository.users;

import com.hanghae.coffee.dto.users.CountInfoByUserDto;
import com.hanghae.coffee.dto.users.UserAuthDto;

public interface UsersRepositoryCustom {

    CountInfoByUserDto getCountInfoByUser(Long userId);

    UserAuthDto getUserAuth(Long userId);

}
