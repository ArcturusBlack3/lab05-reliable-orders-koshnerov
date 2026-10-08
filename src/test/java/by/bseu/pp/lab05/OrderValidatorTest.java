package by.bseu.pp.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {
    @Test
    void acceptsNonBlankIds() {
        assertDoesNotThrow(() -> OrderValidator.validateId("O01"));
        assertDoesNotThrow(() -> OrderValidator.validateId(" O01 "));
    }

    @Test
    void rejectsNullId() {
        ValidationException ex = assertThrows(ValidationException.class, () -> OrderValidator.validateId(null));
        assertTrue(ex.getMessage().contains("id"));
    }

    @Test
    void rejectsEmptyId() {
        assertThrows(ValidationException.class, () -> OrderValidator.validateId(""));
    }

    @Test
    void rejectsWhitespaceId() {
        assertThrows(ValidationException.class, () -> OrderValidator.validateId(" \t\n"));
    }

    @Test
    void acceptsPositiveFiniteAmounts() {
        assertDoesNotThrow(() -> OrderValidator.validateAmount(1200));
        assertDoesNotThrow(() -> OrderValidator.validateAmount(0.01));
    }

    @Test
    void rejectsZero() {
        assertThrows(ValidationException.class, () -> OrderValidator.validateAmount(0));
    }

    @Test
    void rejectsNegativeAndNamesValue() {
        ValidationException ex = assertThrows(ValidationException.class, () -> OrderValidator.validateAmount(-100));
        assertTrue(ex.getMessage().contains("amount"));
        assertTrue(ex.getMessage().contains("-100.0"));
    }

    @Test
    void rejectsNaN() {
        ValidationException ex = assertThrows(ValidationException.class, () -> OrderValidator.validateAmount(Double.NaN));
        assertTrue(ex.getMessage().contains("amount"));
        assertTrue(ex.getMessage().contains("NaN"));
    }

    @Test
    void rejectsPositiveInfinity() {
        assertThrows(ValidationException.class, () -> OrderValidator.validateAmount(Double.POSITIVE_INFINITY));
    }

    @Test
    void rejectsNegativeInfinity() {
        assertThrows(ValidationException.class, () -> OrderValidator.validateAmount(Double.NEGATIVE_INFINITY));
    }

}
