package com.moonz.memoProject.service;

import com.moonz.memoProject.domain.Memo;
import com.moonz.memoProject.domain.MemoRepository;
import com.moonz.memoProject.domain.MemoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

//update 메소드를 위해!!

@RequiredArgsConstructor    //final로 생성된 클래스의 생성자를 무조건 생성해줄게.
@Service
public class MemoService {
    // MemoService를 이용할 때 memoRepository를 무조건 이용하게 만드려면 "final" 키워드!
    private final MemoRepository memoRepository;

    /* Transaction 이란?
    DB의 상태를 변경하는 작업 or 한번에 수행되어야하는 연산들
    begin, commit을 자동으로 수행해줌
    4가지 성질
      1. 원자성 (한 트랜잭션 내에서 실행한 작업들은 하나의 단위로 처리. 모두 성공 or 모두 실패)
      2. 일관성 (트랜잭션은 일관성 있는 DB 상태를 유지)
      3. 격리성 (동시 실행되는 트랜잭션들이 서로 영향을 미치지않도록 격리)
      4. 영속성 (트랜잭션을 성공적으로 마치면 결과가 항상 저장)
    아래 annotation 방식을 선언적 트랜잭션이라 부르며,
    적용된 범위에서는 트랜잭션 기능이 포함된 프록시 객체가 생성되어 자동으로 commit 혹은 rollback을 진행.
    추가 설명 : https://velog.io/@kdhyo/JavaTransactional-Annotation-%EC%95%8C%EA%B3%A0-%EC%93%B0%EC%9E%90-26her30h
     */

    @Transactional  //db에 무조건 반영되야한다?
    public Long update(Long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                ()-> new NullPointerException("아이디가 존재하지 않습니다.")
                // NullPointerException : 내가 가리키는 대상이 없다.
                // IllegalArgumentException : Argument(인자)가 잘못됐다.
        );
        memo.update(requestDto);    //update될 정보가 인자 requestDto를 통해 넘어왔으니 이용!
        return memo.getId();
    }
}
