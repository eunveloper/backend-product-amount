package antigravity.validation;

import antigravity.exception.NotFoundDomainException;
import antigravity.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class DomainValidationTest {

    private ProductRepository productRepository;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("존재하는 상품 아이디로 조회")
    void existProductId() {
        int notExistProductId = 5;

        Assertions.assertThrows(NotFoundDomainException.class, () -> {
            productRepository.getProduct(notExistProductId);
        });
    }

    @Test
    @DisplayName("존재하는 상품 아이디로 조회")
    void existPromotionIds() {
        Integer[] notExistPromotionIds = {3, 5};

        Assertions.assertThrows(NotFoundDomainException.class, () -> {
            productRepository.getPromotions(notExistPromotionIds);
        });
    }

}
