package by.bseu.pp.lab05;

public final class OrderValidator {
    private OrderValidator() { }

    /** null и пустой/пробельный id: ValidationException; сообщение содержит id. */
    public static void validateId(String id) {
        // TODO 1: проверьте id; при корректном значении метод завершается без исключения.
        throw new UnsupportedOperationException("TODO: validateId");
    }

    /** Только конечное число > 0; ошибка содержит amount и переданное значение. */
    public static void validateAmount(double amount) {
        // TODO 2: проверьте amount, включая NaN и бесконечность.
        throw new UnsupportedOperationException("TODO: validateAmount");
    }
}
