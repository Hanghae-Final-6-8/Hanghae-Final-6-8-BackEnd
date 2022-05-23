package com.hanghae.coffee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe {

    @Id
    @Column(name = "cafe_id")
    private Long id;

    @Column(nullable = false)
    private String cafeName;

    private String cafeLogoImage;

    private String cafeBackGroundImage;

    @JsonIgnore
    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Beans> beans = new ArrayList<>();

    @Builder
    public Cafe(Long id, String cafeName, String cafeLogoImage, String cafeBackGroundImage) {
        this.id = id;
        this.cafeName = cafeName;
        this.cafeLogoImage = cafeLogoImage;
        this.cafeBackGroundImage = cafeBackGroundImage;
    }

}
