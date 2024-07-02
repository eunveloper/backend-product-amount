package antigravity.service;

import antigravity.config.PromotionType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.InvalidBusinessDataException;
import antigravity.exception.ParameterValidateException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        checkValidateParam(request);

        Product product = repository.getProduct(request.getProductId());
        checkProductPrice(product.getPrice());

        int discountPrice = 0;
        List<Promotion> promotions = repository.getPromotionProducts(product.getId(), request.getPromotionIds());
        for (Promotion promotion : promotions) {
            discountPrice += calcDiscountPrice(product.getPrice(), promotion);
        }

        int finalPrice = calcFinalPrice(product.getPrice(), discountPrice);
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
    }

    public void checkValidateParam(ProductInfoRequest request) {
        Integer productId = request.getProductId();
        Integer[] promotionIds = request.getPromotionIds();
        if (productId == null) {
            throw new ParameterValidateException("상품 아이디는 필수입니다.");
        }
        if (productId < 1) {
            throw new ParameterValidateException("상품 아이디는 정수 값 입니다.");
        }
        if (promotionIds == null) {
            throw new ParameterValidateException("프로모션 아이디는 필수입니다.");
        }
        if (promotionIds.length != 2) {
            throw new ParameterValidateException("프로모션 아이디는 두개여야 입니다.");
        }
        for (Integer promotionId : promotionIds) {
            if (promotionId < 1) {
                throw new ParameterValidateException("프로모션 아이디는 정수 값 입니다.");
            }
        }
    }

    public void checkProductPrice(int price) {
        if (10000 >= price || price >= 10000000) {
            throw new InvalidBusinessDataException("상품 금액은 10,000원 이상 10,000,000원 이하여야 합니디.");
        }
    }

    public int calcDiscountPrice(int price, Promotion promotion) {
        String promotionType = promotion.getPromotion_type();

        if (promotionType.equals(PromotionType.COUPON.name())) {
            if (price < promotion.getDiscount_value()) {
                throw new InvalidBusinessDataException("상품 금액보다 상품 할인가가 클 수 없습니다.");
            }
            return promotion.getDiscount_value();
        }
        if (promotionType.equals(PromotionType.CODE.name())) {
            return (int) (price * (promotion.getDiscount_value() / 100.0));
        }

        throw new InvalidBusinessDataException("존재하지 않는 프로모션 타입입니다.");
    }

    public int calcFinalPrice(int originPrice, int discountPrice) {
        int finalPrice = originPrice - discountPrice;
        if (finalPrice < 0) {
            throw new InvalidBusinessDataException("상품 금액보다 상품 할인가가 클 수 없습니다.");
        }

        return (finalPrice / 10000) * 10000;
    }

}
