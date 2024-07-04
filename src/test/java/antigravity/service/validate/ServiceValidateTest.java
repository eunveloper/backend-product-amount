package antigravity.service.validate;


import antigravity.exception.ParameterValidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static antigravity.GenerateParam.createProductInfoReq;

public class ServiceValidateTest {

    @Test
    @DisplayName("가격을 측정 할 상품 아이디는 필수 정수 값")
    void requiredProductId() {
        Integer[] promotionIds = {1, 2};

        Integer nullProductId = null;
        int minusProductId = -1;
        int zeroProductId = 0;
        int productId = 1;

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            createProductInfoReq(nullProductId, promotionIds).checkValidateParam();
        });

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            createProductInfoReq(minusProductId, promotionIds).checkValidateParam();
        });

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            createProductInfoReq(zeroProductId, promotionIds).checkValidateParam();
        });

        Assertions.assertDoesNotThrow(
            () -> createProductInfoReq(productId, promotionIds).checkValidateParam()
        );
    }

    @Test
    @DisplayName("가격을 측정 할 프로모션 아이디는 필수 값 이며 항상 두개 정수 값")
    void requiredPromotionIds() {
        Integer[] zeroPromotionIds = null;
        Integer[] threePromotionIds = {1, 2, 3};
        Integer[] minusPromotionIds = {1, -2};
        Integer[] promotionIds = {1, 2};

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            createProductInfoReq(1, zeroPromotionIds).checkValidateParam();
        });

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            createProductInfoReq(1, threePromotionIds).checkValidateParam();
        });

        Assertions.assertThrows(ParameterValidateException.class, () -> {
            createProductInfoReq(1, minusPromotionIds).checkValidateParam();
        });

        Assertions.assertDoesNotThrow(
            () ->  createProductInfoReq(1, promotionIds).checkValidateParam()
        );
    }

}
