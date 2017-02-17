package ro.tethys.facebook.questor.api;

import org.glassfish.grizzly.http.server.Request;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Constantin HORIA (constantin.horia@1and1.ro)
 */
public abstract class BaseResource {

    protected static final String X_RESULT_COUNT_HEADER = "X-Result-Count";

    @Inject
    protected Provider<Request> grizzlyRequestProvider;

    protected Response createResponse(List responseEntity, long count) {
        return Response.status(Response.Status.OK)
                .entity(responseEntity)
                .header(X_RESULT_COUNT_HEADER, count)
                .build();
    }

//    protected Alert getAlert(Session session, long serverId) {
//        Alert alert = (Alert) session.get(Alert.class, serverId);
//        Assert.assertNotNull(alert, ErrorMessage.PROXY_NOT_EXISTS);
//        return alert;
//    }
}