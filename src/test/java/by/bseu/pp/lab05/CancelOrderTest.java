package by.bseu.pp.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CancelOrderTest {
    @Test
    void cancelsCreatedOrder() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 100);
        service.cancelOrder("O01");
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(100, order.getTotalAmount());
        assertEquals(1, service.getOrderCount());
    }

    @Test
    void rejectsPaidOrderAndKeepsState() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 100);
        service.payOrder("O01", 100);
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.cancelOrder("O01"));
        assertTrue(ex.getMessage().contains("O01"));
        assertTrue(ex.getMessage().contains("PAID"));
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void rejectsRepeatedCancellation() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 100);
        service.cancelOrder("O01");
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.cancelOrder("O01"));
        assertTrue(ex.getMessage().contains("O01"));
        assertTrue(ex.getMessage().contains("CANCELLED"));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void rejectsUnknownOrder() {
        OrderService service = new OrderService();
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.cancelOrder("O99"));
        assertTrue(ex.getMessage().contains("O99"));
        assertEquals(0, service.getOrderCount());
    }

    @Test
    void rejectsInvalidId() {
        OrderService service = new OrderService();
        assertThrows(ValidationException.class, () -> service.cancelOrder(null));
        assertThrows(ValidationException.class, () -> service.cancelOrder(" "));
    }

    @Test
    void doesNotAffectAnotherOrder() {
        OrderService service = new OrderService();
        Order first = service.createOrder("O01", 100);
        Order second = service.createOrder("O02", 200);
        service.cancelOrder("O02");
        assertEquals(OrderStatus.CREATED, first.getStatus());
        assertEquals(OrderStatus.CANCELLED, second.getStatus());
    }

}
