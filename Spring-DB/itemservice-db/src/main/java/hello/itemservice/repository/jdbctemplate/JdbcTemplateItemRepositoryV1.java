package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate
 * - 커넥션 생성하는 역할도 하기 때문에 생성 시 DataSource가 필요하다.
 * - JDBC는 db에서 자동 생성해준 id 값을 가져오기 위해 KeyHolder 클래스를 사용한다. (생성된 id를 추가 쿼리를 통해 조회)
 */
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values (?, ?, ?)";
        // db에서 생성해주는 id값을 가져오는 방식
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            // 자동 증가 키
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=? and price=? and quantity=? where id=?";
        jdbcTemplate.update(sql,
                updateParam.getItemName(),
                updateParam.getPrice(),
                updateParam.getQuantity(),
                itemId);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id=?";
        try{
            Item item = jdbcTemplate.queryForObject(sql, itemRowMapper(), id);
            return Optional.of(item);   // of()는 Null이면 안된다. ofNullable()은 Null 허용
        } catch (EmptyResultDataAccessException e) {    /* queryForObject(): 조회값이 null일 경우 해당 예외 발생함 */
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        String sql = "select id, item_name, price, quantity from item";
        // 동적 쿼리 : 상황에 따라 적절한 쿼리를 생성해야 한다. (추후 다른 방식으로 접근해볼 예정)
        // ~~
        
        List<Item> query = jdbcTemplate.query(sql, itemRowMapper());    /* 그냥 select하여 리스트로 값 조회 시, query() */
        return query;
    }

    // sql 실행 후 결과 ResultSet을 Item으로 변환한다.
    private RowMapper<Item> itemRowMapper() {
        return (((rs, rowNum) -> {
            Item item = new Item();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setPrice(rs.getInt("price"));
            item.setQuantity(rs.getInt("quantity"));

            return item;
        }));
    }

}
