package com.silverbars.marketplace.liveorderboard;

import com.google.common.annotations.VisibleForTesting;
import com.silverbars.marketplace.orders.Order;
import com.silverbars.marketplace.orders.OrderType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.unmodifiableCollection;

/**
 * The LiveOrderBoardService is intended to be the interaction point between UI and backend components.
 * Orders {@link Order} can be registered and cancelled and the current view of the order board can be
 * retrieved using the {@link #getLiveOrderBoard()} method.
 */
public class LiveOrderBoardService {

    private HashMap<UUID, Order> registeredOrders = new HashMap<>();

    public UUID registerOrder(String userId, double quantity, int pricePerKilo, OrderType orderType) {
        return registerOrder(new Order(userId, quantity, pricePerKilo, orderType));
    }

    public UUID registerOrder(final Order order) {
        checkNotNull(order);
        UUID orderId = UUID.randomUUID();
        registeredOrders.put(orderId, order);
        return orderId;
    }

    public void cancelOrder(UUID uuid) {
        registeredOrders.remove(uuid);
    }

    /**
     * Gets the current view of the live order board.  In this implementation the view is created
     * when clients request it.  This approach was taken as the simplest implementation for the example as it
     * ensures that the board returned is always based on the latest order information.  In a production system the
     * balance between readers and writers should be considered which may lead to the board being created and cached
     * on each update.
     *
     * @return LiveOrderBoard instant created from registered orders
     */
    public LiveOrderBoard getLiveOrderBoard() {
        return new LiveOrderBoard(unmodifiableCollection(registeredOrders.values()));
    }

    @VisibleForTesting
    Map<UUID, Order> getLiveOrders() {
        return Collections.unmodifiableMap(registeredOrders);
    }
}
