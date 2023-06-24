package moonz.springtx.propagation.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor  // JPA 스펙 위해 존재해야함.
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String username;

    public Member (String username) {
        this.username = username;
    }
}
