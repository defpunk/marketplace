package com.silverbars.marketplace.liveorderboard;

import com.silverbars.marketplace.orders.Order;
import com.silverbars.marketplace.orders.OrderType;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.silverbars.marketplace.orders.OrderType.BUY;
import static com.silverbars.marketplace.orders.OrderType.SELL;
import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Represents the view of the live order board at a point in time created from a
 * collection of live orders.  The consolidated buy and sell order lists are created in
 * this component to encapsulate view concerns and leave the {@link LiveOrderBoardService}
 * with the responsibility of managing the order data.
 */
public class LiveOrderBoard {

    private final List<LiveOrderBoardItem> sellOrders;
    private final List<LiveOrderBoardItem> buyOrders;

    public LiveOrderBoard(final Collection<Order> orders) {
        buyOrders = orderList(orders, BUY, orderPriceComparator().reversed());
        sellOrders = orderList(orders, SELL, orderPriceComparator());
    }

    //Merges orders of the specified type matching of the price per kilo and then sorting by the supplied comparator
    private static List<LiveOrderBoardItem> orderList(Collection<Order> orders, OrderType orderType, Comparator<LiveOrderBoardItem> comparator) {
        return orders.stream()
                .filter(o -> orderType == o.getOrderType())
                .collect(toMap(Order::getPricePerKilo, Order::getQuantity, Double::sum))
                .entrySet()
                .stream()
                .map(LiveOrderBoard::liveOrderItem)
                .sorted(comparator)
                .collect(toList());
    }

    private static Comparator<LiveOrderBoardItem> orderPriceComparator() {
        return comparingDouble(LiveOrderBoardItem::getPricePerKilo);
    }

    private static LiveOrderBoardItem liveOrderItem(Map.Entry<Integer, Double> e) {
        return new LiveOrderBoardItem(e.getValue(), e.getKey());
    }

    public List<LiveOrderBoardItem> getSellOrders() {
        return sellOrders;
    }

    public List<LiveOrderBoardItem> getBuyOrders() {
        return buyOrders;
    }

}
