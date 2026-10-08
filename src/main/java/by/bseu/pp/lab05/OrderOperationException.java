package by.bseu.pp.lab05;

/** Нарушение бизнес-правила операции с заказом. Класс уже готов; изучите передачу сообщения в super. */
public class OrderOperationException extends IllegalStateException {
    public OrderOperationException(String message) { super(message); }
}
