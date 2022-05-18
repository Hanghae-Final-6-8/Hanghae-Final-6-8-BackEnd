package com.hanghae.coffee.repository.beans;

import com.hanghae.coffee.model.Beans;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeansRepository extends JpaRepository<Beans, Long>, BeansCustomRepository {

    Optional<Beans> findById(Long id);

    List<Beans> findAllByCafeId(Long cafeId);



}
