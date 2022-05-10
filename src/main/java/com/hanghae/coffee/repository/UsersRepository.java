package com.hanghae.coffee.repository;

import com.hanghae.coffee.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Long> {

    Optional<Users> findAllByAuthId(String authId);

	Optional<Object> findByname(String name);

	Optional<Users> findAllByRequestToken(String accessToken);
}
