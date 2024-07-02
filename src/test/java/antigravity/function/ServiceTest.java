package antigravity.function;

import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.transaction.Transactional;

import static antigravity.GenerateParam.createProductInfoReq;

@Transactional
@SpringBootTest
public class ServiceTest {

    private ProductService productService;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        productService = new ProductService(new ProductRepository(namedParameterJdbcTemplate));
    }

    @Test
    @DisplayName("상품 가격 산정 기능")
    void calcPromotionProduct() {
        ProductAmountResponse response = productService.getProductAmount(createProductInfoReq(1, new Integer[]{1, 2}));
        Assertions.assertEquals(response.getDiscountPrice(), 62250);
        Assertions.assertEquals(response.getFinalPrice(), 150000);
    }

}
