package by.bseu.pp.lab05;

/** Готовая модель заказа. Изменения состояния выполняет OrderService. */
public final class Order {
    private final String id;
    private final double totalAmount;
    private OrderStatus status = OrderStatus.CREATED;

    Order(String id, double totalAmount) {
        this.id = id;
        this.totalAmount = totalAmount;
    }

    public String getId() { return id; }
    public double getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }

    void markPaid() { status = OrderStatus.PAID; }
    void markCancelled() { status = OrderStatus.CANCELLED; }
}
