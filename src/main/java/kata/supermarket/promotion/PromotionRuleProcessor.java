package kata.supermarket.promotion;

import kata.supermarket.Item;
import kata.supermarket.promotion.model.PromotionCriteria;
import kata.supermarket.promotion.model.PromotionType;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionRuleProcessor {

    boolean isApplicable(PromotionType promotionType);

    BigDecimal process(List<Item> items, BigDecimal grossTotal, PromotionCriteria promotionCriteria);
}
