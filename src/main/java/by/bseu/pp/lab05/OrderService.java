package by.bseu.pp.lab05;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class OrderService {
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    private final Map<String, Order> orders = new HashMap<>();

    /** Создать и сохранить новый заказ; повторный id запрещён. */
    public Order createOrder(String id, double totalAmount) {
       
        
        try{

            OrderValidator.validateId(id);
            OrderValidator.validateAmount(totalAmount);

            if(orders.containsKey(id)){
                throw new OrderOperationException("Заказ" + id + " уже существует");
            }

            Order order=new Order(id, totalAmount);

            orders.put(id, order);
            LOGGER.info("Заказ создан: "+id+ " сумма = "+totalAmount);
            return order;

        } catch(ValidationException | OrderOperationException e){
            LOGGER.warning("В создании заказа "+id+" отклонено:  "+e.getMessage());
            throw e;
        }

    }

    /** Найти существующий заказ; успешный поиск записать на уровне FINE. */
    public Order findOrder(String id) {
        
        try {
            OrderValidator.validateId(id);
            Order order=getOrderOrThrow(id);

            LOGGER.fine("Заказ найден: "+ id+" статус = "+order.getStatus());
            return  order;

        } catch (ValidationException|OrderOperationException e) {
            LOGGER.warning("Поиск заказа "+id+" отклонён: "+e.getMessage());
            throw e;
        }
    }

    /** Оплатить CREATED-заказ полностью. Проверки выполняются до изменения состояния. */
    public void payOrder(String id, double paymentAmount) {
       
        try {
            OrderValidator.validateId(id);
            OrderValidator.validateAmount(paymentAmount);

            Order order=getOrderOrThrow(id);
            if(order.getStatus()!=OrderStatus.CREATED){
                throw new  OrderOperationException("Заказ "+id+" нельзя оплатить. Статус = "+order.getStatus());
            }

            if(Double.compare(paymentAmount, order.getTotalAmount())!=0){
                throw new OrderOperationException("Заказ "+id+": сумма оплаты "+paymentAmount+" должна равняться "+order.getTotalAmount());
            }

            order.markPaid();

            LOGGER.info("Заказ "+id+" оплачен. Сумма = "+paymentAmount);

        } catch (ValidationException|OrderOperationException e) {
            LOGGER.warning("Оплата заказа "+id+" отклонена: "+e.getMessage());
        throw e;
        }
    }

    /** Отменить только CREATED-заказ; PAID и CANCELLED запрещены. */
    public void cancelOrder(String id) {
       
        
        try {
            OrderValidator.validateId(id);

            Order order=getOrderOrThrow(id);

            if(order.getStatus()!=OrderStatus.CREATED){
                throw new OrderOperationException("Заказ "+ id+" нельзя отменить. Статус = "+ order.getStatus());
            }

            order.markCancelled();

            LOGGER.info("Отменён заказ "+id);

        } catch (ValidationException|OrderOperationException e) {
            LOGGER.warning("Отмена заказа "+id+" отклонена: "+e.getMessage());
            throw e;
            
        }

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
