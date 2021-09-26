package com.moonz.firstproject;

import com.moonz.firstproject.domain.Person;
import com.moonz.firstproject.domain.PersonRepository;
import com.moonz.firstproject.domain.PersonRequestDto;
import com.moonz.firstproject.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

/* Controller 역할과 달리, 당장 JVM 실행 시, main에서 데이터 관련 테스트 해보는 것 */
@EnableJpaAuditing	// JPA Auditing을 활성화하여 수정&생성 일자가 spring에 반영될 수 있도록 함
@SpringBootApplication
public class FirstprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstprojectApplication.class, args);
	}
	@Bean
	public CommandLineRunner demo(PersonRepository personRepository, PersonService personService) {
		return (args) -> {
			//==================================== 데이터 추가 ==========================================
			Person person = new Person("Moon", "Student", "Hwaseongsi");
			personRepository.save(person);

			//==================================== 데이터 조회 ==========================================
			System.out.println("=======전체 데이터 조회=======");
			List<Person> personList = personRepository.findAll();

			for (int i=0; i<personList.size(); i++) {
				Person p = personList.get(i);
				System.out.println("ID: "+ p.getId());
				System.out.println("이름: "+ p.getName());
				System.out.println("직업: "+ p.getJob());
				System.out.println("주소: "+ p.getAddress());
			}

			//==================================== 데이터 업데이트 ==========================================
			PersonRequestDto requestDto = new PersonRequestDto("Moonz", "Influencer", "Suwonsi");
			personService.update(1L, requestDto);	//위에서 생성한 id=1인 데이터를 수정
			System.out.println("=======데이터 업데이트 완료=======");

			//==================================== 데이터 1개 조회 ==========================================
			System.out.println("=======데이터 1개 조회=======");
			Person one = personRepository.findById(1L).orElseThrow(
					() -> new NullPointerException("아이디가 존재하지 않습니다.")	//객체가 생성되지 않은 것
			);
			System.out.println("ID: "+ one.getId());
			System.out.println("이름: "+ one.getName());
			System.out.println("직업: "+ one.getJob());
			System.out.println("주소: "+ one.getAddress());

		};
	}

}
