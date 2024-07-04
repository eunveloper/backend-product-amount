package antigravity.domain.entity;

import antigravity.exception.InvalidBusinessDataException;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private int id;
    private String name;
    private int price;

    public void checkProductPrice() {
        if (10000 >= this.price || this.price >= 10000000) {
            throw new InvalidBusinessDataException("상품 금액은 10,000원 이상 10,000,000원 이하여야 합니디.");
        }
    }

}
