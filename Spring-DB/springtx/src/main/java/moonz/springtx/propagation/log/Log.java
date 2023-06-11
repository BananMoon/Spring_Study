package moonz.springtx.propagation.log;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue
    private long id;
    private String message;

    public Log (String message) {
       this.message = message;
    }
}
