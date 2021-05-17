package com.root.filter;

import com.root.entity.User;
import com.root.service.UserService;
import org.glassfish.jersey.server.ContainerRequest;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;

public class SecurityFilter implements ContainerResponseFilter {

    @Inject
    private UserService userService;


    @Override
    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response) {
        boolean shouldIntercept = !((ContainerRequest) request).getRequestUri().toString().contains("/user/");
        if(shouldIntercept && !isPreflightRequest(request)){
            final String token = ((ContainerRequest) request).getRequestHeaders().get("token").get(0);
            final User user = userService.findByToken(token);
            if(user == null)
                request.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }

    }
    private static boolean isPreflightRequest(ContainerRequestContext request) {
        return request.getHeaderString("Origin") != null
                && request.getMethod().equalsIgnoreCase("OPTIONS");
    }
}