package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.NotFoundDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product getProduct(int productId) {
        String query = "SELECT * FROM `product` WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", productId);

        try {
            Product product =  namedParameterJdbcTemplate.queryForObject(query, params, productRowMapper());
            return product;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundDomainException("존재하지 않는 상품의 아이디 입니다.");
        }
    }

    public List<Promotion> getPromotions(Integer[] promotionIds) {
        String query = "SELECT * FROM `promotion` WHERE id in (:id)";
        List<Integer> promotionIdList = List.of(promotionIds);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", promotionIdList);

        List<Promotion> promotions = namedParameterJdbcTemplate.query(query, params, promotionRowMapper());
        if (promotions.size() != 2) {
            throw new NotFoundDomainException("존재하지 않는 프로모션의 아이디 입니다.");
        }

        return promotions;
    }

    public boolean existProductPromotion(int productId, Integer[] promotionIds) {
        String query = "SELECT COUNT(*) AS count FROM `promotion_products` WHERE product_id = :productId AND promotion_id in (:promotionId)";
        List<Integer> promotionIdList = List.of(promotionIds);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("promotionId", promotionIdList);

        int count = namedParameterJdbcTemplate.queryForObject(query, params, (rs, rowNum) -> rs.getInt("count"));
        if (count != 2) {
            throw new NotFoundDomainException("적용되지 않는 상품 프로모션 정보입니다.");
        }
        return true;
    }

    private RowMapper<Product> productRowMapper() {
        return (rs, rowNum) -> Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .price(rs.getInt("price"))
                .build();
    }

    private RowMapper<Promotion> promotionRowMapper() {
        return (rs, rowNum) -> Promotion.builder()
                .id(rs.getInt("id"))
                .promotion_type(rs.getString("promotion_type"))
                .name(rs.getString("name"))
                .discount_type(rs.getString("discount_type"))
                .discount_value(rs.getInt("discount_value"))
                .build();
    }

}
