package com.sismics.docs.rest.resource;

import com.sismics.docs.core.event.model.jpa.RegisterRequest;
import com.sismics.docs.core.service.RegisterRequestService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Resource for handling user registration requests.
 */
@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterRequestResource {
    private final RegisterRequestService service = new RegisterRequestService();

    /**
     * Submit a registration request.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(@FormParam("username") String username,
                             @FormParam("email") String email,
                             @FormParam("password") String password) {
        if (username == null || email == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"status\":\"error\",\"message\":\"Missing required fields.\"}")
                    .build();
        }

        service.submitRequest(username, email, password);
        return Response.ok().entity("{\"status\":\"ok\"}").build();
    }

    /**
     * Admin views all pending requests.
     */
    @GET
    @Path("/pending")
    public Response getPendingRequests() {
        List<RegisterRequest> requests = service.getPendingRequests();
        return Response.ok().entity(requests).build();
    }

    /**
     * Admin approves a registration request.
     */
    @POST
    @Path("/approve")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response approve(@FormParam("requestId") String requestId,
                            @FormParam("adminId") String adminId) {
        try {
            service.approveRequest(requestId, adminId);
            return Response.ok().entity("{\"status\":\"approved\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Admin rejects a registration request.
     */
    @POST
    @Path("/reject")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response reject(@FormParam("requestId") String requestId) {
        service.rejectRequest(requestId);
        return Response.ok().entity("{\"status\":\"rejected\"}").build();
    }
}
