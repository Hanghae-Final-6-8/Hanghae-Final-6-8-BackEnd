package com.hanghae.coffee.repository.taste;

import com.hanghae.coffee.model.Taste;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasteRepository extends JpaRepository<Taste,Long>, TasteCustomRepository {

    Optional<Taste> findByUsersId(Long userId);


}
