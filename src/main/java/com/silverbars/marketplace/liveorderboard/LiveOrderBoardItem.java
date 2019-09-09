package com.silverbars.marketplace.liveorderboard;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

public class LiveOrderBoardItem {

    private double quantity;
    private int pricePerKilo;

    public LiveOrderBoardItem(double quantity, int pricePerKilo) {
        checkArgument(quantity > 0, "Quantity was %s but expected greater than zero", quantity);
        checkArgument(pricePerKilo > 0, "PricePerKilo was %s but expected greater than zero", pricePerKilo);
        this.quantity = quantity;
        this.pricePerKilo = pricePerKilo;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getPricePerKilo() {
        return pricePerKilo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiveOrderBoardItem that = (LiveOrderBoardItem) o;
        return Double.compare(that.quantity, quantity) == 0 &&
                pricePerKilo == that.pricePerKilo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, pricePerKilo);
    }
}
