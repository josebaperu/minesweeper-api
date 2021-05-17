package com.root.resource;

import com.google.gson.Gson;
import com.root.entity.User;
import com.root.model.UserResponse;
import com.root.repository.UserRepository;
import com.root.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/user")
public class UserResource {
    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @Inject
    Gson gson;

    @GET
    @Path("/exist/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyUserExists(@PathParam("username") final String username) {
    final List<User> users = userService.findByUsername(username);
    final UserResponse userResponse = new UserResponse();
    userResponse.setUserExist(users.size() > 0);
        return Response
                .status(Response.Status.OK)
                .entity(userResponse)
                .build();
    }
    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUsernameAndPassword(final String body) {
        final User user = gson.fromJson(body, User.class);
        user.setToken(userService.generateToken());
        User newUser = userRepository.save(user);
        return Response
                .status(Response.Status.OK)
                .entity(newUser)
                .build();
    }
    @POST
    @Path("/signin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(final String body) {
        final User user = gson.fromJson(body, User.class);
        User newUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        final UserResponse userResponse = new UserResponse();
        userResponse.setUserExist(false);
        return Response
                .status(Response.Status.OK)
                .entity(newUser != null? newUser : userResponse)
                .build();
    }
}
