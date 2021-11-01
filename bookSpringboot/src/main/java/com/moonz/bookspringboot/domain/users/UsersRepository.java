package com.moonz.bookspringboot.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    /* DB Layer 접근자. 인터페이스로 생성하여 JpaRepository를 상속하여 기본적인 CRUD 메소드가 자동 생성됨.
       Entity 클래스와 기본 Repositoryy 는 매우 밀접한 관계,
       Entity 클래스는 기본 Repository없이 제대로 역할을 할 수가 없음.
     */

}
