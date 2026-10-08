package by.bseu.pp.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FindOrderTest {
    @Test
    void findsSameStoredObject() {
        OrderService service = new OrderService();
        Order original = service.createOrder("O01", 100);
        assertSame(original, service.findOrder("O01"));
    }

    @Test
    void rejectsUnknownIdWithItsCode() {
        OrderService service = new OrderService();
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.findOrder("O99"));
        assertTrue(ex.getMessage().contains("O99"));
        assertEquals(0, service.getOrderCount());
    }

    @Test
    void validatesIdBeforeLookup() {
        OrderService service = new OrderService();
        assertThrows(ValidationException.class, () -> service.findOrder(null));
        assertThrows(ValidationException.class, () -> service.findOrder(" "));
    }

    @Test
    void doesNotTrimOrChangeCase() {
        OrderService service = new OrderService();
        Order original = service.createOrder(" O01 ", 100);
        assertSame(original, service.findOrder(" O01 "));
        assertThrows(OrderOperationException.class, () -> service.findOrder("O01"));
        assertThrows(OrderOperationException.class, () -> service.findOrder(" o01 "));
    }

}
