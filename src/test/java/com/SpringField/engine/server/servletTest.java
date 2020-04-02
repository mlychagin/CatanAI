package com.SpringField.engine.server;

import com.SpringField.engine.BoardState;
import com.SpringField.server.Servlet;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

public class servletTest {
    @Test
    public void servletTester() throws Exception {
        BoardState b = new BoardState(4);
        Servlet servlet = new Servlet();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1000);
        }
    }
}
