package com.silverbars.marketplace.liveorderboard;

import com.silverbars.marketplace.orders.Order;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static com.silverbars.marketplace.orders.OrderType.BUY;
import static com.silverbars.marketplace.orders.OrderType.SELL;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/*
 * All functional tests of the live order service have been left in this class as the delegation
 * of the creation of the Buy and Sell lists from the orders in the LiveOrderBoard class constructor
 * can be thought of as an implementation detail.  In a fuller implementation that had real back end
 * integration and consequently a mocking framework to test it I would add a test to ensure orders were being passed
 * to the LiveOrderBoard correctly and then move the tests to check its creation to that component.  With the current
 * implementation I have left the tests at this level to reduce the cost of change should the calculation
 * responsibilities be moved.
 */
public class LiveOrderBoardServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private LiveOrderBoardService orderService;

    @Before
    public void setUp() {
        orderService = new LiveOrderBoardService();
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringNullOrder() {
        expectedException.expect(NullPointerException.class);
        orderService.registerOrder(null);
    }

    @Test
    public void shouldReturnUUIDWhenValidOrderRegistered() {
        Order order = new Order("UserId", 1, 1, SELL);
        UUID uuid = orderService.registerOrder(order);
        assertThat(uuid, notNullValue());
    }

    @Test
    public void shouldAddNewlyRegisteredOrderToLiveOrdersMap() {
        Order order = new Order("UserId", 1, 1, SELL);
        UUID orderId = orderService.registerOrder(order);
        assertThat(orderService.getLiveOrders().get(orderId), is(order));
    }

    @Test
    public void shouldReturnEmptyMapWhenNoOrdersRegistered() {
        assertThat(orderService.getLiveOrders(), is(anEmptyMap()));
    }

    @Test
    public void shouldRemoveOrdersFromLiveOrdersMapWhenCancelled() {
        Order order = new Order("UserId", 1, 1, SELL);
        UUID uuid = orderService.registerOrder(order);
        assertThat(orderService.getLiveOrders(), hasKey(uuid)); //Check registration worked

        orderService.cancelOrder(uuid);
        assertThat(orderService.getLiveOrders(), is(anEmptyMap()));
    }

    @Test
    public void shouldBeAbleToCancelNoRegisteredOrderWithoutException() {
        // Included to document the assumption that the cancellation
        // should be idempotent.  If there are side effects to cancelling the order
        // this should be reviewed.
        orderService.cancelOrder(UUID.randomUUID());
    }

    @Test
    public void shouldReturnEmptyBoardWhenNoOrdersRegistered() {
        LiveOrderBoard liveOrderBoard = orderService.getLiveOrderBoard();
        assertThat(liveOrderBoard.getSellOrders(), is(empty()));
        assertThat(liveOrderBoard.getBuyOrders(), is(empty()));
    }

    @Test
    public void shouldCreateLiveOrderBoardWithSingleSellOrderWhenOneSellOrderRegistered() {
        orderService.registerOrder("User1", 3.5, 306, SELL);

        LiveOrderBoard liveOrderBoard = orderService.getLiveOrderBoard();

        assertThat(liveOrderBoard.getBuyOrders(), is(empty()));
        assertThat(liveOrderBoard.getSellOrders(), contains(
                new LiveOrderBoardItem(3.5, 306)));
    }

    @Test
    public void shouldCreateLiveOrderBoardWithSellOrdersSortedLowToHigh() {
        orderService.registerOrder("User2", 3.5, 310, SELL);
        orderService.registerOrder("User1", 3.5, 306, SELL);
        orderService.registerOrder("User3", 3.5, 200, SELL);

        LiveOrderBoard liveOrderBoard = orderService.getLiveOrderBoard();

        assertThat(liveOrderBoard.getBuyOrders(), is(empty()));
        assertThat(liveOrderBoard.getSellOrders(), contains(
                new LiveOrderBoardItem(3.5, 200),
                new LiveOrderBoardItem(3.5, 306),
                new LiveOrderBoardItem(3.5, 310)));
    }

    @Test
    public void shouldMergeOrdersOfTypeAndAmountInLiveOrderSummary() {
        orderService.registerOrder("User1", 3.5, 306, SELL);
        orderService.registerOrder("User2", 1.2, 310, SELL);
        orderService.registerOrder("User3", 1.5, 307, SELL);
        orderService.registerOrder("User4", 2.0, 306, SELL);

        LiveOrderBoard liveOrderBoard = orderService.getLiveOrderBoard();

        assertThat(liveOrderBoard.getBuyOrders(), is(empty()));
        assertThat(liveOrderBoard.getSellOrders(), contains(
                new LiveOrderBoardItem(5.5, 306),
                new LiveOrderBoardItem(1.5, 307),
                new LiveOrderBoardItem(1.2, 310)));
    }

    @Test
    public void shouldCreateLiveOrderBoardWithSingleBuyOrderWhenOneBuyRegistered() {
        orderService.registerOrder("User1", 3.5, 306, BUY);

        LiveOrderBoard liveOrderBoard = orderService.getLiveOrderBoard();

        assertThat(liveOrderBoard.getSellOrders(), is(empty()));
        assertThat(liveOrderBoard.getBuyOrders(), contains(
                new LiveOrderBoardItem(3.5, 306)));
    }

    @Test
    public void shouldCreateLiveOrderBoardWithBuyOrdersSortedHighToLow() {
        orderService.registerOrder("User2", 3.5, 310, BUY);
        orderService.registerOrder("User1", 3.5, 306, BUY);
        orderService.registerOrder("User3", 3.5, 200, BUY);

        LiveOrderBoard liveOrderBoard = orderService.getLiveOrderBoard();

        assertThat(liveOrderBoard.getSellOrders(), is(empty()));
        assertThat(liveOrderBoard.getBuyOrders(), contains(
                new LiveOrderBoardItem(3.5, 310),
                new LiveOrderBoardItem(3.5, 306),
                new LiveOrderBoardItem(3.5, 200)));
    }

    @Test
    public void shouldMergeBuyIOrdersWithSamePrice() {
        orderService.registerOrder("User2", 3.5, 310, BUY);
        orderService.registerOrder("User1", 3.5, 306, BUY);
        orderService.registerOrder("User3", 3.5, 310, BUY);

        LiveOrderBoard liveOrderBoard = orderService.getLiveOrderBoard();

        assertThat(liveOrderBoard.getSellOrders(), is(empty()));
        assertThat(liveOrderBoard.getBuyOrders(), contains(
                new LiveOrderBoardItem(7.0, 310),
                new LiveOrderBoardItem(3.5, 306)));
    }
}
