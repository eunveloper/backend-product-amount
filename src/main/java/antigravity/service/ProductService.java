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
        return null;
    }

    private void checkValidateParam(ProductInfoRequest request) {
        if (request.getProductId() < 1) {
            throw new ParameterValidateException("상품 아이디는 필수이며, 정수값 입니다.");
        }
        if (request.getCouponIds().length != 2) {
            throw new ParameterValidateException("쿠폰 아이디는 필수이며, 두개여야 입니다.");
        }
    }
}
