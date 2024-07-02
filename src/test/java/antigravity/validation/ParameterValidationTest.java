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
        int[] couponIds = {1, 2};
        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(0, couponIds));
        });
    }

    @Test
    @DisplayName("가격을 측정 할 쿠폰 아이디는 필수 값 이며 항상 두개")
    void requiredTwoPromotionIds() {
        int[] zeroCouponIds = {};
        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(1, zeroCouponIds));
        });

        int[] threeCouponIds = {1, 2, 3};
        Assertions.assertThrows(ParameterValidateException.class, () -> {
            productService.getProductAmount(createParam(1, threeCouponIds));
        });
    }

    private ProductInfoRequest createParam(int productId, int[] couponIds) {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(productId)
                .couponIds(couponIds)
                .build();
        return request;
    }


}
