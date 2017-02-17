package ro.tethys.facebook.questor.api.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.glassfish.jersey.server.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps java exceptions to response object.
 */
@Provider
public class RestExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger LOG = LoggerFactory.getLogger(RestExceptionMapper.class);

	public RestExceptionMapper() {
		super();
	}

	public Response toResponse(Exception exception) {
		if (exception instanceof NullPointerException) {
			LOG.error("GOT NullPointerException ", exception);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).build();
		} else if (exception instanceof NumberFormatException) {
			LOG.error("GOT NumberFormatException ", exception);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).build();
		} else if (exception instanceof IllegalArgumentException && "Invalid identity key".equals(exception.getMessage())) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(exception.getMessage())
					.type(MediaType.APPLICATION_JSON)
					.build();
		} else if (exception instanceof JsonMappingException) {
			if (exception.getCause() instanceof IllegalArgumentException) {
				LOG.error("GOT IllegalArgumentException ", exception);
				return Response.status(Response.Status.BAD_REQUEST).entity(
						ErrorMessage.WRONG_TYPE)
						.type(MediaType.APPLICATION_JSON).build();
			}
		} else if (exception instanceof NotFoundException) {
			LOG.trace("GOT NotFoundException", exception);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).build();
		} else if (exception instanceof ForbiddenException) {
			LOG.trace("GOT ForbiddenException", exception);
			return Response.status(Response.Status.FORBIDDEN).type(MediaType.APPLICATION_JSON).build();
		} else if (exception instanceof NotAllowedException) {
			return Response.status(Response.Status.METHOD_NOT_ALLOWED).type(MediaType.APPLICATION_JSON).build();
		} else if (exception instanceof ParamException) {
			LOG.trace("GOT ParamException", exception);
			return Response.status(Response.Status.BAD_REQUEST).entity(
					ErrorMessage.WRONG_TYPE)
					.type(MediaType.APPLICATION_JSON).build();
		} else if (exception instanceof WebApplicationException) {
			int status = ((WebApplicationException) exception).getResponse().getStatus();
			if (status == Response.Status.UNAUTHORIZED.getStatusCode()) {
				LOG.trace("GOT NotAuthorizedException", exception);
				return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).build();
			}
		}
		LOG.error("GOT unknown exception", exception);
		return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage())
				.type(MediaType.APPLICATION_JSON).build();
	}

}
