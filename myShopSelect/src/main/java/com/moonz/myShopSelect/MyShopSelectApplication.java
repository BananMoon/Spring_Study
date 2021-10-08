package com.moonz.myShopSelect;

import com.moonz.myShopSelect.utils.NaverShopSearch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling	//스프링 부트에서 스케줄러가 작동하려면 권한을 획득해야하므로 알려주는 것.
@EnableJpaAuditing	// 시간 자동 변경이 가능하도록
@SpringBootApplication
public class MyShopSelectApplication {

	public static void main(String[] args) {SpringApplication.run(MyShopSelectApplication.class, args);}

}
