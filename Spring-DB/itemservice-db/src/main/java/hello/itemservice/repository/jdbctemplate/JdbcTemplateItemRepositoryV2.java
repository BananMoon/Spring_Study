package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 1. NamedParameterJdbcTemplate 이름 기반 파라미터 바인딩
 * 2. 이름 지정 지원하는 파라미터 바인딩 인터페이스 적용
 * SqlParameterSource : entity와 SQL 파라미터를 바인딩해주는 인터페이스
 * - BeanPropertySqlParameterSource  : 가장 간단하게 파라미터 바인딩 가능한 클래스BeanPropertySqlParameterSource
 * - MapSqlParameterSource : 위 클래스를 사용할 수 없는 경우, 해당 클래스를 사용한다.
 * Map
 * 3. BeanPropertyRowMapper : 객체 조회 시, 조회한 ResultSet을 객체로 변환해주는 매퍼 클래스
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) "
                + "values (:itemName, :price, :quantity)";
        // db에서 생성해주는 id값을 가져오는 방식
        /* 방법1. */
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);    // 인자로 받은 객체 내 필드이름으로 파라미터 생성
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";

        /* 방법2. */
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);

        jdbcTemplate.update(sql, param);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id=:id";

        /* 방법3. */
        Map<String, Object> param = Map.of("id", id);
        try{
            Item item = jdbcTemplate.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);   // of()는 N ll이면 안된다. ofNullable()은 Null 허용
        } catch (EmptyResultDataAccessException e) {    /* queryForObject(): 조회값이 null일 경우 해당 예외 발생함 */
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        String sql = "select id, item_name, price, quantity from item";

        /* 방법1. */
        // cond 내 필드들이 자동으로 파라미터에 바인딩되므로 따로 add할 필요 없음.
        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        // 동적 쿼리 : 상황에 따라 적절한 쿼리를 생성해야 한다. (추후 다른 방식으로 접근해볼 예정)
        // 모든 상황에 대해 계산해서 동적 처리를 해주는 코드는 기하급수적으로 복잡해지기만 한다.
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {

            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }
        log.info("sql={}", sql);
        return jdbcTemplate.query(sql, param, itemRowMapper());  /* 그냥 select하여 리스트로 값 조회 시, query() */
    }

    // BeanPropertyRowMapper가 sql 실행 후 결과 ResultSet을 받으면 자바빈 규약에 맞춰 데이터를 변환한다. 내부적으로 리플렉션 기능 사용한다.
    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class);  // camel 변환 지원해줌!!!
        /*return (((rs, rowNum) -> {
            Item item = new Item();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setPrice(rs.getInt("price"));
            item.setQuantity(rs.getInt("quantity"));

            return item;
        }));*/
    }

}
