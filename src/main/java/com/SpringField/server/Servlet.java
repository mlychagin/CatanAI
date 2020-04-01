package com.SpringField.server;

import com.SpringField.engine.BoardState;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Get the Universal Resource Identifier
        String requestUrl = request.getRequestURI();
        //String name = requestUrl.substring("/people/".length());

        //BoardState board = new BoardState();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String name = request.getParameter("name");
        String about = request.getParameter("about");
        int birthYear = Integer.parseInt(request.getParameter("birthYear"));

        //DataStore.getInstance().putPerson(new Person(name, about, birthYear, password));
    }
}
