package by.bseu.pp.lab05;

/** Некорректные входные данные. Класс уже готов; изучите передачу сообщения в super. */
public class ValidationException extends IllegalArgumentException {
    public ValidationException(String message) { super(message); }
}
