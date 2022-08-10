package com.entry;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class MiddleWare {

    public static final List<Filter> MIDDLE_WARE = List.of(new Filter() {
        @Override
        public void doFilter(HttpExchange exchange, Filter.Chain chain) throws IOException {
            System.out.println("I am some PRE processing");
            chain.doFilter(exchange);
            System.out.println("I am some POST processing");
        }

        @Override
        public String description() {
            return "One is the loneliness number that there ever was";
        }
    });
}
