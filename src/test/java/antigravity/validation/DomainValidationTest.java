package antigravity.validation;

import antigravity.exception.NotFoundDomainException;
import antigravity.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest
public class DomainValidationTest {

    private ProductRepository productRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("존재하는 상품 아이디로 조회")
    void existProductId() {
        int notExistProductId = 5;
        int existProductId = 1;

        Assertions.assertThrows(NotFoundDomainException.class, () -> {
            productRepository.getProduct(notExistProductId);
        });

        Assertions.assertNotNull(
            productRepository.getProduct(existProductId)
        );
    }

    @Test
    @DisplayName("존재하는 상품 아이디로 조회")
    void existPromotionIds() {
        Integer[] notExistPromotionIds = {3, 4};
        Integer[] existPromotionsIds = {1, 2};

        Assertions.assertThrows(NotFoundDomainException.class, () -> {
            productRepository.getPromotions(notExistPromotionIds);
        });

        Assertions.assertNotNull(
            productRepository.getPromotions(existPromotionsIds)
        );
    }

    @Test
    @DisplayName("적용 가능한 상품 프로모션 정보로 조회")
    void existProductPromotion() {
        int productId = 1;
        Integer[] notExistPromotionIds = {3, 4};
        Integer[] existPromotionsIds = {1, 2};

        Assertions.assertThrows(NotFoundDomainException.class, () -> {
            productRepository.getPromotionProducts(productId, notExistPromotionIds);
        });

        Assertions.assertTrue(
            productRepository.getPromotionProducts(productId, existPromotionsIds)
        );
    }

}
