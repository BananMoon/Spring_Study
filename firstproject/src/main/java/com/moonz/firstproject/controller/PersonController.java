package com.moonz.firstproject.controller;

// [https://localhost:8080/api/persons] POST : 친구 생성
// [https://localhost:8080/api/persons] GET : 친구 목록 조회
// [https://localhost:8080/api/persons/{id}] PUT : 친구 정보 변경
// [https://localhost:8080/api/persons/{id}] DELETE : 친구 정보 삭제

import com.moonz.firstproject.domain.Person;
import com.moonz.firstproject.domain.PersonRepository;
import com.moonz.firstproject.domain.PersonRequestDto;
import com.moonz.firstproject.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PersonController {
    private final PersonRepository personRepository;
    private final PersonService personService;

    /* Controller는 API 요청 시 응답을 처리하는 역할!
       데이터를 조회, 추가, 삭제 시에는 PersonRepository를 이용(findById(), save(), deleteById())
       데이터를 수정 시에는 PersonService를 이용(update())
       PersonRequestDto를 이용해서 데이터 추가, 수정 진행!
       데이터 수정, 삭제 시에는 id 이용!
     */

    //추가
    @PostMapping("/api/persons")
    public Person creatPerson(@RequestBody PersonRequestDto requestDto) {
        Person person = new Person(requestDto);
        return personRepository.save(person);
    }
    //조회
    @GetMapping("/api/persons")
    public List<Person> getPersons() {
        return personRepository.findAll();
    }
    //수정
    @PutMapping("/api/persons/{id}")
    public Long updatePerson(@PathVariable Long id, @RequestBody PersonRequestDto requestDto) {
        return personService.update(id,requestDto);
    }
    //삭제
    @DeleteMapping("/api/persons/{id}")
    public Long deletePerson(@PathVariable Long id) {
        personRepository.deleteById(id);
        return id;
    }
}
