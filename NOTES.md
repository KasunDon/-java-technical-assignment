# Notes

Please add here any notes, assumptions and design decisions that might help up understand your though process.

- Added org.mockito library for Mocking support.
- Assumed when deciding buy-1 get-1 free promotion that qualifying items has to match the same price.

Design decisions
===============

- Added `PromotionRuleProcessor` interface to support different implementations of Promotion criteria processing.
- Added `PromotionType` enum to classify promotion types we may need to run promotions.
- System will need to maintain set of `PromotionCriteria` and rules based on `PromotionRuleProcessor` interface.

** In order to apply promotion to specific product groups we should introduce `category` field to `Product` entity. then we can allow promotion criteria to be aware of category based criteria for promotion calculation.