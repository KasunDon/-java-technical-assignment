package kata.supermarket.promotion.rules;

import kata.supermarket.Item;
import kata.supermarket.ItemByUnit;
import kata.supermarket.promotion.PromotionRuleProcessor;
import kata.supermarket.promotion.model.PromotionCriteria;
import kata.supermarket.promotion.model.PromotionType;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static kata.supermarket.promotion.model.PromotionType.QUANTITY;

public class BuyOneGetOneFreePromotionRuleProcessor implements PromotionRuleProcessor {

    @Override
    public boolean isApplicable(PromotionType promotionType) {
        return QUANTITY == promotionType;
    }

    @Override
    public BigDecimal process(List<Item> items, BigDecimal grossTotal, PromotionCriteria promotionCriteria) {
        requireNonNull(items);
        requireNonNull(grossTotal);
        requireNonNull(promotionCriteria);

        long qty = promotionCriteria.getValue();

        List<BigDecimal> prices = items.stream()
            .map(Item::price)
            .distinct()
            .collect(toList());

        BigDecimal discount = ZERO;

        for (BigDecimal price : prices) {
            int count = 0;

            for (Item item : items) {
                BigDecimal itemPrice = item.price();

                if (item instanceof ItemByUnit && itemPrice.equals(price)) {
                    count++;
                    if (count % qty == 0) {
                        discount = discount.add(itemPrice);
                    }
                }
            }
        }

        return discount.setScale(2, HALF_UP);
    }
}
