package by.bseu.pp.lab05;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PayOrderTest {
    @Test
    void paysCreatedOrder() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 1200);
        service.payOrder("O01", 1200);
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(1200, order.getTotalAmount());
        assertEquals(1, service.getOrderCount());
    }

    @Test
    void rejectsTooSmallPaymentAndKeepsState() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O02", 500);
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.payOrder("O02", 400));
        assertTrue(ex.getMessage().contains("O02"));
        assertTrue(ex.getMessage().contains("400.0"));
        assertTrue(ex.getMessage().contains("500.0"));
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void rejectsTooLargePaymentAndKeepsState() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 100);
        assertThrows(OrderOperationException.class, () -> service.payOrder("O01", 101));
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void rejectsInvalidAmountAndKeepsState() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 100);
        for (double amount : new double[]{0, -1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(ValidationException.class, () -> service.payOrder("O01", amount));
            assertEquals(OrderStatus.CREATED, order.getStatus());
        }
    }

    @Test
    void rejectsPaidOrderAndNamesStatus() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 100);
        service.payOrder("O01", 100);
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.payOrder("O01", 100));
        assertTrue(ex.getMessage().contains("O01"));
        assertTrue(ex.getMessage().contains("PAID"));
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    void rejectsCancelledOrderAndNamesStatus() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 100);
        service.cancelOrder("O01");
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.payOrder("O01", 100));
        assertTrue(ex.getMessage().contains("O01"));
        assertTrue(ex.getMessage().contains("CANCELLED"));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void rejectsUnknownOrder() {
        OrderService service = new OrderService();
        OrderOperationException ex = assertThrows(OrderOperationException.class, () -> service.payOrder("O99", 100));
        assertTrue(ex.getMessage().contains("O99"));
        assertEquals(0, service.getOrderCount());
    }

    @Test
    void validatesIdAndAmountBeforeBusinessRules() {
        OrderService service = new OrderService();
        assertThrows(ValidationException.class, () -> service.payOrder(null, 100));
        assertThrows(ValidationException.class, () -> service.payOrder("O99", -1));
    }

    @Test
    void allowsCorrectPaymentAfterRejectedOne() {
        OrderService service = new OrderService();
        Order order = service.createOrder("O01", 100);
        assertThrows(OrderOperationException.class, () -> service.payOrder("O01", 90));
        service.payOrder("O01", 100);
        assertEquals(OrderStatus.PAID, order.getStatus());
    }

}
