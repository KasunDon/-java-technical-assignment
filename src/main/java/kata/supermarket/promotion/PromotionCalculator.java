package kata.supermarket.promotion;


import kata.supermarket.Item;
import kata.supermarket.promotion.model.PromotionCriteria;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class PromotionCalculator {

    private Set<PromotionCriteria> criteria;
    private List<PromotionRuleProcessor> promotionRuleProcessors;

    public PromotionCalculator(Set<PromotionCriteria> criteria, List<PromotionRuleProcessor> promotionRuleProcessors) {
        this.criteria = criteria;
        this.promotionRuleProcessors = promotionRuleProcessors;
    }

    public BigDecimal calculate(List<Item> items, BigDecimal grossTotal) {
        requireNonNull(items);
        requireNonNull(grossTotal);

        BigDecimal total = BigDecimal.ZERO;

        for (PromotionCriteria promotionCriteria : this.criteria) {
            for (PromotionRuleProcessor promotionRuleProcessor : promotionRuleProcessors)
                if (promotionRuleProcessor.isApplicable(promotionCriteria.getPromotionType())) {
                    BigDecimal discount = promotionRuleProcessor.process(items, grossTotal, promotionCriteria);
                    total = total.add(discount);
                }
        }

        return total;
    }
}
