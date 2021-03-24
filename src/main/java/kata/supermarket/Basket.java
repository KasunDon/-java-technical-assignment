package kata.supermarket;

import kata.supermarket.promotion.PromotionCalculator;
import kata.supermarket.promotion.PromotionRuleProcessor;
import kata.supermarket.promotion.model.PromotionCriteria;
import kata.supermarket.promotion.rules.BuyOneGetOneFreePromotionRuleProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static kata.supermarket.promotion.model.PromotionType.QUANTITY;

public class Basket {
    private final List<Item> items;


    public Basket() {
        this.items = new ArrayList<>();
    }

    public void add(final Item item) {
        this.items.add(item);
    }

    List<Item> items() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal total() {
        return new TotalCalculator().calculate();
    }

    private class TotalCalculator {
        private final List<Item> items;
        private final PromotionCalculator promotionCalculator;

        TotalCalculator() {
            this.items = items();
            Set<PromotionCriteria> promoCriteria =
                Collections.singleton(PromotionCriteria.of(2, QUANTITY));

            List<PromotionRuleProcessor> promoRules =
                asList(new BuyOneGetOneFreePromotionRuleProcessor());

            promotionCalculator = new PromotionCalculator(promoCriteria, promoRules);
        }

        private BigDecimal subtotal() {
            return items.stream().map(Item::price)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        }

        /**
         * TODO: This could be a good place to apply the results of
         * the discount calculations.
         * It is not likely to be the best place to do those calculations.
         * Think about how Basket could interact with something
         * which provides that functionality.
         */
        private BigDecimal discounts() {
            return promotionCalculator.calculate(items, subtotal());
        }

        private BigDecimal calculate() {
            return subtotal().subtract(discounts());
        }
    }
}
