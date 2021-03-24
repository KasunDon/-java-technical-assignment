package kata.supermarket.promotion;

import kata.supermarket.Item;
import kata.supermarket.promotion.model.PromotionCriteria;
import kata.supermarket.promotion.model.PromotionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static kata.supermarket.promotion.model.PromotionType.QUANTITY;
import static kata.supermarket.promotion.model.PromotionType.WEIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PromotionCalculatorTest {

    @Test
    public void throw_an_exception_given_null_items() {
        assertThrows(NullPointerException.class, () -> {
            PromotionCalculator promotionCalculator = new PromotionCalculator(emptySet(), emptyList());
            promotionCalculator.calculate(null, ZERO);
        });
    }

    @Test
    public void throw_an_exception_given_null_gross_amount() {
        assertThrows(NullPointerException.class, () -> {
            PromotionCalculator promotionCalculator = new PromotionCalculator(emptySet(), emptyList());
            promotionCalculator.calculate(emptyList(), null);
        });
    }

    @Test
    public void return_zero_when_no_criteria_being_configured() {
        PromotionCalculator promotionCalculator = new PromotionCalculator(emptySet(), emptyList());
        assertEquals(ZERO, promotionCalculator.calculate(emptyList(), TEN));
    }

    @Test
    public void return_zero_when_no_promo_rules_being_configured() {
        Set<PromotionCriteria> promotionCriteria = singleton(PromotionCriteria.of(1, QUANTITY));
        PromotionCalculator promotionCalculator = new PromotionCalculator(promotionCriteria, emptyList());
        assertEquals(ZERO, promotionCalculator.calculate(emptyList(), TEN));
    }

    @Test
    public void no_execute_when_promo_rules_not_qualifying_for_configured_criteria() {
        Set<PromotionCriteria> promotionCriteria = singleton(PromotionCriteria.of(1, QUANTITY));
        List<PromotionRuleProcessor> promotionRuleProcessors = singletonList(new PromotionRuleProcessor() {
            @Override
            public boolean isApplicable(PromotionType qualification) {
                return qualification == WEIGHT;
            }

            @Override
            public BigDecimal process(List<Item> items, BigDecimal grossTotal, PromotionCriteria promoCriteria) {
                return TEN;
            }
        });

        PromotionCalculator promotionCalculator = new PromotionCalculator(promotionCriteria, promotionRuleProcessors);
        assertEquals(ZERO, promotionCalculator.calculate(emptyList(), valueOf(20)));
    }

    @Test
    public void executes_when_promo_rules_qualifying_for_configured_criteria() {
        Set<PromotionCriteria> promotionCriteria = singleton(PromotionCriteria.of(1, QUANTITY));
        List<PromotionRuleProcessor> promotionRuleProcessors = singletonList(new PromotionRuleProcessor() {
            @Override
            public boolean isApplicable(PromotionType qualification) {
                return qualification == QUANTITY;
            }

            @Override
            public BigDecimal process(List<Item> items, BigDecimal grossTotal, PromotionCriteria promoCriteria) {
                return TEN;
            }
        });

        PromotionCalculator promotionCalculator = new PromotionCalculator(promotionCriteria, promotionRuleProcessors);
        assertEquals(TEN, promotionCalculator.calculate(emptyList(), valueOf(20)));
    }
}