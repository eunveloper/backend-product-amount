package antigravity.validation;


import antigravity.exception.ParameterValidateException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.repository.ProductRepository;
import antigravity.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParameterValidationTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("가격을 측정 할 상품 아이디는 필수 정수 값")
    void requiredProductId() {
        Integer[] promotionIds = {1, 2};

        Integer nullProductId = null;
        int minusProductId = -1;
        int zeroProductId = 0;

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(nullProductId, promotionIds));
        });

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(minusProductId, promotionIds));
        });

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(zeroProductId, promotionIds));
        });
    }

    @Test
    @DisplayName("가격을 측정 할 프로모션 아이디는 필수 값 이며 항상 두개 정수 값")
    void requiredPromotionIds() {
        Integer[] zeroPromotionIds = null;
        Integer[] threePromotionIds = {1, 2, 3};
        Integer[] minusPromotionIds = {1, -2};

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(1, zeroPromotionIds));
        });

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(1, threePromotionIds));
        });

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(1, minusPromotionIds));
        });
    }

    private ProductInfoRequest createParam(Integer productId, Integer[] promotionIds) {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(productId)
                .promotionIds(promotionIds)
                .build();
        return request;
    }


}
