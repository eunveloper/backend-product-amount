package antigravity.function;

import antigravity.config.PromotionType;
import antigravity.domain.entity.Promotion;
import antigravity.exception.InvalidBusinessDataException;
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
public class ServiceFunctionTest {

    private final int originPrice = 50000;

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("상품의 금액은 최소 ₩10,000 최대 ₩10,000,000")
    void checkProductPrice() {
        int impossibleMinPrice = 9000;
        int impossibleMaxPrice = 11000000;
        int possiblePrice = 35000;

        Assertions.assertThrows(InvalidBusinessDataException.class, () -> {
            productService.checkProductPrice(impossibleMinPrice);
        });

        Assertions.assertThrows(InvalidBusinessDataException.class, () -> {
            productService.checkProductPrice(impossibleMaxPrice);
        });

        Assertions.assertDoesNotThrow(
                () -> productService.checkProductPrice(possiblePrice)
        );
    }

    @Test
    @DisplayName("최종 상품 금액은 천단위 절삭")
    void checkProductFinalPriceUnit() {
        int discountPrice = 3950;

        Assertions.assertEquals(productService.calcFinalPrice(originPrice, discountPrice), 40000);
    }

    @Test
    @DisplayName("최종 상품 금액은 정수")
    void checkProductFinalPrice() {
        int discountPrice = 53950;

        Assertions.assertThrows(InvalidBusinessDataException.class, () -> {
            productService.calcFinalPrice(originPrice, discountPrice);
        });
    }

    @Test
    @DisplayName("할인 금액을 계산하는 프로모션 타입은 쿠폰과 코드 타입 두개")
    void checkPromotionType() {
        Promotion promotion = Promotion.builder()
                .promotion_type("UNKNOWN")
                .discount_value(30000)
                .build();

        Assertions.assertThrows(InvalidBusinessDataException.class, () -> {
            productService.calcDiscountPrice(originPrice, promotion);
        });
    }

    @Test
    @DisplayName("쿠폰 타입은 금액할인 프로모션 적용")
    void calcCouponProductPrice() {
        Promotion promotion = Promotion.builder()
                .promotion_type(PromotionType.COUPON.name())
                .discount_value(30000)
                .build();

        Assertions.assertEquals(productService.calcDiscountPrice(originPrice, promotion), 30000);
    }

    @Test
    @DisplayName("쿠폰 타입 프로모션 할인 금액은 실제 상품 금액보다 작음")
    void checkCouponProductPrice() {
        Promotion promotion = Promotion.builder()
                .promotion_type(PromotionType.COUPON.name())
                .discount_value(60000)
                .build();

        Assertions.assertThrows(InvalidBusinessDataException.class, () -> {
            productService.calcDiscountPrice(originPrice, promotion);
        });
    }

    @Test
    @DisplayName("코드 타입은 퍼센트할인 프로모션 적용")
    void calcCodeProductPrice() {
        Promotion promotion = Promotion.builder()
                .promotion_type(PromotionType.CODE.name())
                .discount_value(15)
                .build();

        Assertions.assertEquals(productService.calcDiscountPrice(originPrice, promotion), 7500);
    }

}
