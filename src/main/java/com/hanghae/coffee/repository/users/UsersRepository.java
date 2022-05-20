package com.hanghae.coffee.service.users.users;

import com.hanghae.coffee.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Long>,UsersCustomRepository {

    Optional<Users> findAllByAuthId(String authId);

}
