package antigravity.model.request;

import antigravity.exception.ParameterValidateException;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfoRequest {
    private Integer productId;
    private Integer[] promotionIds;

    public void checkValidateParam() {
        if (this.productId == null) {
            throw new ParameterValidateException("상품 아이디는 필수입니다.");
        }
        if (this.productId < 1) {
            throw new ParameterValidateException("상품 아이디는 정수 값 입니다.");
        }
        if (this.promotionIds == null) {
            throw new ParameterValidateException("프로모션 아이디는 필수입니다.");
        }
        if (this.promotionIds.length != 2) {
            throw new ParameterValidateException("프로모션 아이디는 두개여야 입니다.");
        }
        for (Integer promotionId : this.promotionIds) {
            if (promotionId < 1) {
                throw new ParameterValidateException("프로모션 아이디는 정수 값 입니다.");
            }
        }
    }

}
