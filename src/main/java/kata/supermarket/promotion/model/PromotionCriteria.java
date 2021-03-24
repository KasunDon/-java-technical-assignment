package kata.supermarket.promotion.model;

public final class PromotionCriteria {

    private final long value;
    private final PromotionType promotionType;

    private PromotionCriteria(
        long value,
        PromotionType promotionType
    ) {
        this.value = value;
        this.promotionType = promotionType;
    }

    public static PromotionCriteria of(long value, PromotionType promotionType) {
        return new PromotionCriteria(value, promotionType);
    }

    public long getValue() {
        return value;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }
}
