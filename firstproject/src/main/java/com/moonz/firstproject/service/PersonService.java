package com.moonz.firstproject.service;

import com.moonz.firstproject.domain.Person;
import com.moonz.firstproject.domain.PersonRepository;
import com.moonz.firstproject.domain.PersonRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PersonService {
    private final PersonRepository personRepository;

    //personRepository의 생성자를 lombok @RequiredArgsConstructor 가 대신 해줌
//    public PersonService(PersonRepository personRepository) {
//        this.personRepository = personRepository;
//    }

    @Transactional
    public Long update(Long id, PersonRequestDto requestDto){
        Person person1 = personRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );
        person1.update(requestDto);
        return person1.getId();
    }
}
