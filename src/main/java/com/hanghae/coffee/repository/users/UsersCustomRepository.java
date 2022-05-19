package com.hanghae.coffee.repository.users;

import com.hanghae.coffee.dto.users.CountInfoByUserDto;

public interface UsersCustomRepository {

    CountInfoByUserDto getCountInfoByUser(Long userId);

}
