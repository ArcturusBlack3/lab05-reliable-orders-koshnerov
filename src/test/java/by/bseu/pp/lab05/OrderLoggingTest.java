package by.bseu.pp.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class OrderLoggingTest {
    private void assertSingleEvent(LogCapture capture, Level level, String... fragments) {
        assertEquals(1, capture.records().size(), "Для операции нужна одна запись журнала");
        LogRecord record = capture.records().get(0);
        assertEquals(level, record.getLevel());
        assertNotNull(record.getMessage());
        assertFalse(record.getMessage().isBlank());
        for (String fragment : fragments) {
            assertTrue(record.getMessage().contains(fragment), "В журнале нет: " + fragment);
        }
    }

    @Test
    void logsSuccessfulCreationAtInfo() {
        try (LogCapture capture = new LogCapture()) {
            new OrderService().createOrder("O01", 1200);
            assertSingleEvent(capture, Level.INFO, "O01", "1200.0");
        }
    }

    @Test
    void logsSuccessfulPaymentAtInfo() {
        try (LogCapture capture = new LogCapture()) {
            OrderService service = new OrderService();
            service.createOrder("O01", 1200);
            capture.clear();
            service.payOrder("O01", 1200);
            assertSingleEvent(capture, Level.INFO, "O01", "1200.0");
        }
    }

    @Test
    void logsSuccessfulCancellationAtInfo() {
        try (LogCapture capture = new LogCapture()) {
            OrderService service = new OrderService();
            service.createOrder("O01", 100);
            capture.clear();
            service.cancelOrder("O01");
            assertSingleEvent(capture, Level.INFO, "O01");
        }
    }

    @Test
    void logsSuccessfulLookupAtFine() {
        try (LogCapture capture = new LogCapture()) {
            OrderService service = new OrderService();
            service.createOrder("O01", 100);
            capture.clear();
            service.findOrder("O01");
            assertSingleEvent(capture, Level.FINE, "O01", "CREATED");
        }
    }

    @Test
    void logsInvalidInputAndStillThrows() {
        try (LogCapture capture = new LogCapture()) {
            OrderService service = new OrderService();
            ValidationException ex = assertThrows(ValidationException.class, () -> service.createOrder("O01", -100));
            assertSingleEvent(capture, Level.WARNING, "O01", ex.getMessage());
            assertEquals(0, service.getOrderCount());
        }
    }

    @Test
    void logsDuplicateAndStillThrows() {
        try (LogCapture capture = new LogCapture()) {
            OrderService service = new OrderService();
            service.createOrder("O01", 100);
            capture.clear();
            OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.createOrder("O01", 200));
            assertSingleEvent(capture, Level.WARNING, "O01", ex.getMessage());
        }
    }

    @Test
    void logsMissingOrderAndStillThrows() {
        try (LogCapture capture = new LogCapture()) {
            OrderOperationException ex = assertThrows(OrderOperationException.class, () -> new OrderService().findOrder("O99"));
            assertSingleEvent(capture, Level.WARNING, "O99", ex.getMessage());
        }
    }

    @Test
    void logsRejectedPaymentWithoutSuccessRecord() {
        try (LogCapture capture = new LogCapture()) {
            OrderService service = new OrderService();
            service.createOrder("O02", 500);
            capture.clear();
            OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.payOrder("O02", 400));
            assertSingleEvent(capture, Level.WARNING, "O02", ex.getMessage());
        }
    }

    @Test
    void logsRejectedCancellationWithoutSuccessRecord() {
        try (LogCapture capture = new LogCapture()) {
            OrderService service = new OrderService();
            service.createOrder("O01", 100);
            service.payOrder("O01", 100);
            capture.clear();
            OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.cancelOrder("O01"));
            assertSingleEvent(capture, Level.WARNING, "O01", ex.getMessage());
        }
    }

}
