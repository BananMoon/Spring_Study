package com.moonz.myShopSelect;

import com.moonz.myShopSelect.utils.NaverShopSearch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing	// 시간 자동 변경이 가능하도록
@SpringBootApplication
public class MyShopSelectApplication {

	public static void main(String[] args) {SpringApplication.run(MyShopSelectApplication.class, args);}

}
