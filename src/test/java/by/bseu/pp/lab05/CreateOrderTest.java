package by.bseu.pp.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CreateOrderTest {
    @Test
    void createsOrderWithRequestedData() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 1200);
        assertEquals("O01", order.getId());
        assertEquals(1200, order.getTotalAmount());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(1, service.getOrderCount());
    }

    @Test
    void storesCreatedOrder() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 1200);
        assertSame(order, service.findOrder("O01"));
    }

    @Test
    void rejectsDuplicateWithoutReplacingOriginal() {
        OrderService service = new OrderService();
        Order original = service.createOrder("O01", 1200);
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.createOrder("O01", 500));
        assertTrue(ex.getMessage().contains("O01"));
        assertEquals(1, service.getOrderCount());
        assertSame(original, service.findOrder("O01"));
        assertEquals(1200, original.getTotalAmount());
    }

    @Test
    void rejectsInvalidIdWithoutAddingOrder() {
        OrderService service = new OrderService();
        assertThrows(ValidationException.class, () -> service.createOrder(null, 1200));
        assertThrows(ValidationException.class, () -> service.createOrder(" ", 1200));
        assertEquals(0, service.getOrderCount());
    }

    @Test
    void rejectsInvalidAmountWithoutAddingOrder() {
        OrderService service = new OrderService();
        assertThrows(ValidationException.class, () -> service.createOrder("O01", 0));
        assertThrows(ValidationException.class, () -> service.createOrder("O01", Double.NaN));
        assertEquals(0, service.getOrderCount());
    }

    @Test
    void canCreateAfterRejectedAttempt() {
        OrderService service = new OrderService();
        assertThrows(ValidationException.class, () -> service.createOrder("O01", -100));
        assertEquals(OrderStatus.CREATED, service.createOrder("O01", 100).getStatus());
        assertEquals(1, service.getOrderCount());
    }

}
