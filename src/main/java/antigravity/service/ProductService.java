package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.exception.ParameterValidateException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        // 비지니스 로직을 실행하기 전, 파라미터 검증하기
        checkValidateParam(request);
        Product product = repository.getProduct(request.getProductId());
        return ProductAmountResponse.builder().build();
    }

    private void checkValidateParam(ProductInfoRequest request) {
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
}
