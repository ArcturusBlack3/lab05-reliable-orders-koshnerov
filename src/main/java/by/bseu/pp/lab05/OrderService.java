package by.bseu.pp.lab05;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class OrderService {
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    private final Map<String, Order> orders = new HashMap<>();

    /** Создать и сохранить новый заказ; повторный id запрещён. */
    public Order createOrder(String id, double totalAmount) {
        // TODO 3: реализуйте правила из методички и логирование.
        throw new UnsupportedOperationException("TODO: createOrder");
    }

    /** Найти существующий заказ; успешный поиск записать на уровне FINE. */
    public Order findOrder(String id) {
        // TODO 4: реализуйте правила из методички и логирование.
        throw new UnsupportedOperationException("TODO: findOrder");
    }

    /** Оплатить CREATED-заказ полностью. Проверки выполняются до изменения состояния. */
    public void payOrder(String id, double paymentAmount) {
        // TODO 5: реализуйте правила из методички и логирование.
        throw new UnsupportedOperationException("TODO: payOrder");
    }

    /** Отменить только CREATED-заказ; PAID и CANCELLED запрещены. */
    public void cancelOrder(String id) {
        // TODO 6: реализуйте правила из методички и логирование.
        throw new UnsupportedOperationException("TODO: cancelOrder");
    }

    /** Готовый метод для проверки количества сохранённых заказов. */
    public int getOrderCount() { return orders.size(); }

    /** Готовый поиск без логирования — используйте его внутри операций. */
    private Order getOrderOrThrow(String id) {
        Order order = orders.get(id);
        if (order == null) {
            throw new OrderOperationException("Заказ " + id + " не найден");
        }
        return order;
    }
}
