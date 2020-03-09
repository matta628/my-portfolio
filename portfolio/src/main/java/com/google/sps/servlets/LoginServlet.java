package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
    /*
    -   Add a servlet that returns the login status of the user.
    -   Test that this works by running a dev server and navigating to the
        servlet's URL.
    -   When you get this step working, create a pull request and send it to
        your advisor for review!
    */
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("text/html");
        UserService userService = UserServiceFactory.getUserService();
        boolean loggedIn = userService.isUserLoggedIn();
        Gson gson = new Gson();
        String json = gson.toJson(loggedIn);
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

}