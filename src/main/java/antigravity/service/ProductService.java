package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    @Transactional
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        request.checkValidateParam();

        Product product = repository.getProduct(request.getProductId());
        product.checkProductPrice();

        List<Promotion> promotions = repository.getPromotionProducts(product.getId(), request.getPromotionIds());

        ProductAmountResponse response = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .build();
        for (Promotion promotion : promotions) {
            response.calcDiscountPrice(promotion);
        }
        response.calcFinalPrice();
        return response;
    }

}
