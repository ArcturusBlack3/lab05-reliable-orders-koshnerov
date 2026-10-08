package by.bseu.pp.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

class ApplicationScenarioTest {
    private String outputOf(Runnable action) {
        PrintStream previous = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream temporary = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(temporary);
            action.run();
        } finally {
            System.setOut(previous);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void executesSuccessfulActionAndReportsSuccess() {
        boolean[] executed = {false};
        String output = outputOf(() -> Application.runScenario("Проверка", () -> executed[0] = true));
        assertTrue(executed[0]);
        assertTrue(output.contains("Сценарий: Проверка"));
        assertTrue(output.contains("Операция выполнена"));
    }

    @Test
    void handlesValidationExceptionWithMessage() {
        String output = outputOf(() -> Application.runScenario("Данные", () -> {
            throw new ValidationException("Некорректный id");
        }));
        assertTrue(output.contains("Ошибка данных: Некорректный id"));
        assertFalse(output.contains("Операция выполнена"));
    }

    @Test
    void handlesBusinessExceptionWithMessage() {
        String output = outputOf(() -> Application.runScenario("Правило", () -> {
            throw new OrderOperationException("Заказ O01 уже оплачен");
        }));
        assertTrue(output.contains("Операция отклонена: Заказ O01 уже оплачен"));
        assertFalse(output.contains("Операция выполнена"));
    }

    @Test
    void continuesWithNextScenario() {
        boolean[] executed = {false};
        String output = outputOf(() -> {
            Application.runScenario("Ошибка", () -> { throw new ValidationException("id"); });
            Application.runScenario("Следующий", () -> executed[0] = true);
        });
        assertTrue(executed[0]);
        assertTrue(output.contains("Сценарий: Следующий"));
    }

    @Test
    void doesNotHideUnexpectedProgrammingError() {
        assertThrows(NullPointerException.class, () -> outputOf(() ->
                Application.runScenario("Неожиданная ошибка", () -> { throw new NullPointerException("bug"); })));
    }

}
