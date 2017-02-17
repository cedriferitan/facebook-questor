package ro.tethys.facebook.questor.api.request;

import com.wordnik.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.tethys.facebook.questor.api.BaseResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/alarm")
@Api(value = "/alarm", description = "Alarm management")
public class AlarmResource extends BaseResource {

	private static final Logger LOG = LoggerFactory.getLogger(AlarmResource.class);

	@GET
//	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get last alarm within group", response = String.class)
	@ApiResponses(value = {@ApiResponse(code = 404, message = "The entity doesn't exist in the system.")})
	public Response getAlarm(@ApiParam(value = "alarm series identifier")
							 @PathParam("id") final String encodedEvSid) throws Exception {
//		Assert.assertNotNull(encodedEvSid, new ParametrizedErrorMessage(ErrorMessage.MISSING_PARAMETER, "alarm id"));
		LOG.debug("Get alarm with id: {}", "abc");

		return Response.status(Response.Status.OK).build();
	}
}
