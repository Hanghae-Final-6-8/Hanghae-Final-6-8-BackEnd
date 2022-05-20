package com.hanghae.coffee.repository.posts;

import com.hanghae.coffee.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tags,Long> {

	Tags findByTagName(String name);
}
