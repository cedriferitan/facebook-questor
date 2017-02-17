package ro.tethys.facebook.questor.api.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BadRequestException extends WebApplicationException {

    public BadRequestException() {
        super(Response.Status.BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message)
                .type(MediaType.APPLICATION_JSON).build());
    }

    public BadRequestException(ErrorMessage message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message)
                .type(MediaType.APPLICATION_JSON).build());
    }

}
