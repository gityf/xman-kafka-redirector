package com.xman.message.pool;

import org.junit.Test;

import java.sql.Connection;

public class Main {

    @Test
    public void test1() {
        Pool<Connection> pool =
                new BoundedBlockingPool<Connection>(
                        10,
                        new JDBCConnectionValidator(),
                        new JDBCConnectionFactory("", "", "", "")
                );
        //do whatever you like
    }

    public void test2() {
        Pool<Connection> pool =
                PoolFactory.newBoundedBlockingPool(
                        10,
                        new JDBCConnectionFactory("", "", "", ""),
                        new JDBCConnectionValidator());
    }
}