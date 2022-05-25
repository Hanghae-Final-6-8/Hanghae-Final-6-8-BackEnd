package com.hanghae.coffee.repository.cafe;

import com.hanghae.coffee.model.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {


}
