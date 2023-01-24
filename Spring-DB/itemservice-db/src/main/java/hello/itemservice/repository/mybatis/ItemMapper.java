package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 마이바이트 매핑 XML을 호출하는 매퍼 인터페이스
 */
@Mapper // 마이바티스에서 인식하여 구현체를 자동으로 만들어 낸다. 구현체는 xml과 연결하는 로직이 아닐까?
public interface ItemMapper {

    void save(Item item);
    void update(@Param("id") Long id, @Param("updateParam")ItemUpdateDto updateDto);   // 파라미터 2개 이상인 경우 @Param 필요
    List<Item> findAll(ItemSearchCond itemSearch);
    Optional<Item> findById(Long id);
}
