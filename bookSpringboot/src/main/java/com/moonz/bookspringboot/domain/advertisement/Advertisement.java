package com.moonz.bookspringboot.domain.advertisement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Builder
@Table(name="advertisement")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Advertisement {
    @Id
    @Column(name="ad_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adId;

    @Column(nullable = false)
    private int emotion;

    @Column(nullable = false)
    private int season;

    @Column(name="service_name", length=50, nullable = false)
    private String serviceName;

    @Column(length=25)
    private String address;

    @Column(name="detail_long", length=1000, nullable = false)
    private String detailLong;

    @Column(length=20)
    private String tel;

    @Column(name="kakaomap_url", length = 200, nullable = false)
    private String kakaoMapUrl;

    @Column(length=200, nullable = false)
    private String src;
}
