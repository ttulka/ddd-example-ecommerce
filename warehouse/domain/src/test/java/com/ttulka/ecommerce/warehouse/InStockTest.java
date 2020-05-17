package com.ttulka.ecommerce.warehouse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InStockTest {

    @Test
    void in_stock_value() {
        InStock inStock = new InStock(123);
        assertThat(inStock.amount()).isEqualTo(123);
    }

    @Test
    void in_stock_zero_value() {
        InStock inStock = new InStock(0);
        assertThat(inStock.amount()).isEqualTo(0);
    }

    @Test
    void in_stock_fails_for_a_value_less_than_zero() {
        assertThrows(IllegalArgumentException.class, () -> new InStock(-1));
    }

    @Test
    void in_stock_is_added() {
        InStock sum = new InStock(1).add(new Amount(2));
        assertThat(sum).isEqualTo(new InStock(3));
    }

    @Test
    void in_stock_is_removed() {
        InStock remaining = new InStock(1).remove(new Amount(1));
        assertThat(remaining).isEqualTo(new InStock(0));
    }

    @Test
    void only_present_in_stock_is_removed() {
        InStock remaining = new InStock(1).remove(new Amount(2));
        assertThat(remaining).isEqualTo(new InStock(0));
    }

    @Test
    void in_stock_is_sold_out() {
        InStock inStock = new InStock(0);
        assertThat(inStock.isSoldOut()).isTrue();
    }

    @Test
    void in_stock_is_not_sold_out() {
        InStock inStock = new InStock(1);
        assertThat(inStock.isSoldOut()).isFalse();
    }

    @Test
    void has_enough_in_stock() {
        InStock inStock = new InStock(1);
        assertThat(inStock.hasEnough(new Amount(1))).isTrue();
    }

    @Test
    void has_enough_not_in_stock() {
        InStock inStock = new InStock(1);
        assertThat(inStock.hasEnough(new Amount(2))).isFalse();
    }

    @Test
    void needs_yet_some() {
        InStock inStock = new InStock(1);
        assertThat(inStock.needsYet(new Amount(2))).isEqualTo(new Amount(1));
    }

    @Test
    void needs_yet_no_more() {
        InStock inStock = new InStock(1);
        assertThat(inStock.needsYet(new Amount(1))).isEqualTo(new Amount(0));
    }
}
