package by.bseu.pp.lab05;

public final class OrderValidator {
    private OrderValidator() { }

    /** null и пустой/пробельный id: ValidationException; сообщение содержит id. */
    public static void validateId(String id) {
        
        if(id==null||id.isBlank()){
            throw new ValidationException("null и пустой/пробельный id: "+ id);
        }
    }

    /** Только конечное число > 0; ошибка содержит amount и переданное значение. */
    public static void validateAmount(double amount) {
        
        if(amount<=0||!Double.isFinite(amount)){
            throw new ValidationException("Неверный amount:"+ amount);
        }
    }
}
