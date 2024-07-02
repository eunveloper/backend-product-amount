package antigravity;

import antigravity.model.request.ProductInfoRequest;

public class GenerateParam {

    public static ProductInfoRequest createProductInfoReq(Integer productId, Integer[] promotionIds) {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(productId)
                .promotionIds(promotionIds)
                .build();
        return request;
    }

}
