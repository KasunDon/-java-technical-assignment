package kata.supermarket.promotion.rules;

import kata.supermarket.Item;
import kata.supermarket.ItemByUnit;
import kata.supermarket.ItemByWeight;
import kata.supermarket.promotion.model.PromotionCriteria;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.FLOOR;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kata.supermarket.promotion.model.PromotionType.QUANTITY;
import static kata.supermarket.promotion.model.PromotionType.WEIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BuyOneGetOneFreePromotionPromotionTypeProcessorTest {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, FLOOR);
    private final BuyOneGetOneFreePromotionRuleProcessor buyOneGetOneFreePromoRule = new BuyOneGetOneFreePromotionRuleProcessor();

    @Test
    public void only_supports_quantity_based_promo_types() {
        assertEquals(true, buyOneGetOneFreePromoRule.isApplicable(QUANTITY));
        assertEquals(false, buyOneGetOneFreePromoRule.isApplicable(WEIGHT));
    }

    @Test
    public void throw_an_exception_given_null_items() {
        assertThrows(NullPointerException.class, () -> {
            buyOneGetOneFreePromoRule.process(null, TEN, mock(PromotionCriteria.class));
        });
    }

    @Test
    public void throw_an_exception_given_null_gross_total() {
        assertThrows(NullPointerException.class, () -> {
            buyOneGetOneFreePromoRule.process(emptyList(), null, mock(PromotionCriteria.class));
        });
    }

    @Test
    public void throw_an_exception_given_null_promo_criteria() {
        assertThrows(NullPointerException.class, () -> {
            buyOneGetOneFreePromoRule.process(emptyList(), TEN, null);
        });
    }

    @Test
    public void return_zero_when_item_size_smaller_than_qty() {
        assertEquals(ZERO, buyOneGetOneFreePromoRule.process(emptyList(), TEN, PromotionCriteria.of(2, QUANTITY)));
        assertEquals(ZERO, buyOneGetOneFreePromoRule.process(singletonList(mock(Item.class)), TEN, PromotionCriteria.of(2, QUANTITY)));
    }

    @Test
    public void promotion_not_applicable_for_item_by_weight() {
        Item item = mock(ItemByWeight.class);
        when(item.price()).thenReturn(valueOf(5));

        List<Item> items = singletonList(item);

        assertEquals(ZERO, buyOneGetOneFreePromoRule.process(items, TEN, PromotionCriteria.of(2, QUANTITY)));
    }

    @Test
    public void not_eligible_for_buy_one_get_one_free_when_single_item_in_basket() {
        Item item = mock(ItemByUnit.class);
        when(item.price()).thenReturn(valueOf(5));

        List<Item> items = singletonList(item);

        assertEquals(ZERO, buyOneGetOneFreePromoRule.process(items, TEN, PromotionCriteria.of(2, QUANTITY)));
    }

    @Test
    public void buy_one_get_one_free_criteria_matches() {
        Item item = mock(ItemByUnit.class);
        when(item.price()).thenReturn(valueOf(5));

        List<Item> items = asList(item, item);
        BigDecimal expected =
            valueOf(5)
                .setScale(2, HALF_UP);

        assertEquals(expected, buyOneGetOneFreePromoRule.process(items, TEN, PromotionCriteria.of(2, QUANTITY)));
    }

    @Test
    public void buy_one_get_one_free_every_two_items() {
        Item item = mock(ItemByUnit.class);
        when(item.price()).thenReturn(valueOf(5));

        List<Item> items = asList(item, item, item);
        BigDecimal expected =
            valueOf(5)
                .setScale(2, HALF_UP);

        assertEquals(expected, buyOneGetOneFreePromoRule.process(items, TEN, PromotionCriteria.of(2, QUANTITY)));
    }
}