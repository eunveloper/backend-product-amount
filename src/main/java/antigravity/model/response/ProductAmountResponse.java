package antigravity.model.response;

import antigravity.config.PromotionType;
import antigravity.domain.entity.Promotion;
import antigravity.exception.InvalidBusinessDataException;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class ProductAmountResponse {
    private String name; //상품명

    private int originPrice; //상품 기존 가격
    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격

    public void calcDiscountPrice(Promotion promotion) {
        String promotionType = promotion.getPromotion_type();

        if (promotionType.equals(PromotionType.COUPON.name())) {
            if (this.originPrice < promotion.getDiscount_value()) {
                throw new InvalidBusinessDataException("상품 금액보다 상품 할인가가 클 수 없습니다.");
            }
            this.discountPrice += promotion.getDiscount_value();
            return;
        }
        if (promotionType.equals(PromotionType.CODE.name())) {
            this.discountPrice += (int) (this.originPrice * (promotion.getDiscount_value() / 100.0));
            return;
        }

        throw new InvalidBusinessDataException("존재하지 않는 프로모션 타입입니다.");
    }

    public void calcFinalPrice() {
        int result = this.originPrice - this.discountPrice;
        if (result < 0) {
            throw new InvalidBusinessDataException("상품 금액보다 상품 할인가가 클 수 없습니다.");
        }

        this.finalPrice = (result / 10000) * 10000;
    }
}
