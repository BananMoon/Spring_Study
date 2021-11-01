package com.moonz.bookspringboot.domain.users;

import com.moonz.bookspringboot.domain.advertisement.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/* setter 생성 X : 클래스의 인스턴스값들이 코드상 언제 어디서 변하는지 구분 불가하기 때문에 차후 기능 변경시 복잡해짐
* 단, 해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타내는 메소드를 추가해야 한다.
* 예) 주문서비스 취소 이벤트에 대해서 setStatus(boolean status)가 아닌, cancleOrder()
* */
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Users {
    @Id
    @Column(name="user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name="now_emotion")
    private Integer nowEmotion;

    private Integer rating;
    // DB에서 not null 지정이 안되어 있는 컬럼(null값이 들어갈 수 있는 컬럼)의 속성 타입이 자바에서 Primitive Type으로 되어 있으면 안된다.
    // 자바에서 Primitive Type(boolean, byte, short, int, long, float, double, char)은 null값을 담을 수 없다.

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name="ad_id");
//    private Advertisement adId;

    @OneToOne
    @JoinColumn(name="ad_id")
    private Advertisement adId;

// builder : 생성 시점에 값을 채워주는 역할. 생성자보다 더 명확히 어느 필드에 어떤 값을 채우는지 명확히 인지가능함.
//    @Builder
//    public Users(int nowEmotion, int rating) {
//        this.nowEmotion = nowEmotion;
//        this.rating = rating;
//    }

}
