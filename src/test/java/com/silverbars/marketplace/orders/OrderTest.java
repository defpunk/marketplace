package com.silverbars.marketplace.orders;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.silverbars.marketplace.orders.OrderType.BUY;
import static com.silverbars.marketplace.orders.OrderType.SELL;

public class OrderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowExceptionWhenCreatingOrderWithNoUserId() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("UserId must be supplied but was null");
        new Order(null, 1, 1, BUY);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenQuantityIs0() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Quantity was 0.0 but expected greater than zero");
        new Order("UserId", 0, 1, SELL);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPricePerKiloIs0() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("PricePerKilo was 0 but expected greater than zero");
        new Order("UserId", 0.5f, 0, SELL);
    }

    @Test
    public void shouldThrowExceptionWhenCreatingOrderWithNoOrderType() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("OrderType must be supplied but was null");
        new Order("UserId", 1, 1, null);
    }
}
