package com.hanghae.coffee.repository.users;

import com.hanghae.coffee.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Long> {

    Optional<Users> findAllByAuthId(String authId);

}
