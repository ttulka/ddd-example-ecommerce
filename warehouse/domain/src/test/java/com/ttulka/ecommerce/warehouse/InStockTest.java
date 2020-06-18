package com.ttulka.ecommerce.warehouse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InStockTest {

    @Test
    void in_stock_value() {
        InStock inStock = new InStock(new Amount(123));
        assertThat(inStock.amount()).isEqualTo(new Amount(123));
    }

    @Test
    void in_stock_zero_value() {
        InStock inStock = new InStock(Amount.ZERO);
        assertThat(inStock.amount()).isEqualTo(Amount.ZERO);
    }

    @Test
    void in_stock_is_added() {
        InStock sum = new InStock(new Amount(1)).add(new Amount(2));
        assertThat(sum).isEqualTo(new InStock(new Amount(3)));
    }

    @Test
    void in_stock_is_removed() {
        InStock remaining = new InStock(new Amount(1)).remove(new Amount(1));
        assertThat(remaining).isEqualTo(new InStock(Amount.ZERO));
    }

    @Test
    void in_stock_is_sold_out() {
        InStock inStock = new InStock(Amount.ZERO);
        assertThat(inStock.isSoldOut()).isTrue();
    }

    @Test
    void in_stock_is_not_sold_out() {
        InStock inStock = new InStock(new Amount(1));
        assertThat(inStock.isSoldOut()).isFalse();
    }

    @Test
    void has_enough_in_stock() {
        InStock inStock = new InStock(new Amount(1));
        assertThat(inStock.hasEnough(new Amount(1))).isTrue();
    }

    @Test
    void has_enough_not_in_stock() {
        InStock inStock = new InStock(new Amount(1));
        assertThat(inStock.hasEnough(new Amount(2))).isFalse();
    }

    @Test
    void needs_yet_some() {
        InStock inStock = new InStock(new Amount(1));
        assertThat(inStock.needsYet(new Amount(2))).isEqualTo(new Amount(1));
    }

    @Test
    void needs_yet_no_more() {
        InStock inStock = new InStock(new Amount(1));
        assertThat(inStock.needsYet(new Amount(1))).isEqualTo(new Amount(0));
    }
}
