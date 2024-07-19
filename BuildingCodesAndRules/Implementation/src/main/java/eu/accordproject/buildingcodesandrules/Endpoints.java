/*
Tom Beach
Copyright (c) Cardiff University
*/

package eu.accordproject.buildingcodesandrules;

import jakarta.ws.rs.Path;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.PathParam;



@Path("/")
public class Endpoints {

	@Inject
    public GraphDBService graphDB;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rootJSON()  
    {
       return Response.status(501).build();
    }

    @GET
    @Path("/")
    @Produces("application/yaml")
    public Response rootYAML()  
    {
       return Response.status(501).build();
    }


    @GET
    @Path("/{classification}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON(
        @PathParam("classification") String classification,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}")
    @Produces("application/yaml")
    public Response latestYAML(
        @PathParam("classification") String classification,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @POST
    @Path("/{classification}/graphQL")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response graphQL(
        @PathParam("classification") String classification,
    	@QueryParam("language") String language,
    	String body) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}/ids")
    @Produces(MediaType.APPLICATION_XML)
    public Response latestYAML(
        @PathParam("classification") String classification) 
    {
        return Response.status(501).build();
    }



    @GET
    @Path("/{classification}/{documentNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}/{documentNumber}")
    @Produces("application/yaml")
    public Response latestYAML(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @POST
    @Path("/{classification}/{documentNumber}/graphQL")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response graphQL(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
    	@QueryParam("language") String language,
    	String body) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}/{documentNumber}/ids")
    @Produces(MediaType.APPLICATION_XML)
    public Response latestYAML(
        @PathParam("classification") String classification,
    	@PathParam("documentNumber") Integer documentNumber) 
    {
        return Response.status(501).build();
    }


	@GET
    @Path("/{classification}/{documentNumber}/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("version") String version,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}/{documentNumber}/{version}")
    @Produces("application/yaml")
    public Response latestYAML(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("version") String version,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @POST
    @Path("/{classification}/{documentNumber}/{version}/graphQL")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response graphQL(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("version") String version,
    	@QueryParam("language") String language,
    	String body) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}/{documentNumber}/{version}/ids")
    @Produces(MediaType.APPLICATION_XML)
    public Response latestYAML(
        @PathParam("classification") String classification,
    	@PathParam("documentNumber") Integer documentNumber,
        @PathParam("version") String version) 
    {
        return Response.status(501).build();
    }

    /*@GET
    @Path("/{classification}/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON2(
        @PathParam("classification") String classification,
        @PathParam("version") String version,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}/{version}")
    @Produces("application/yaml")
    public Response latestYAML2(
        @PathParam("classification") String classification,
        @PathParam("version") String version,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @POST
    @Path("/{classification}/{version}/graphQL")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response graphQL2(
        @PathParam("classification") String classification,
        @PathParam("version") String version,
    	@QueryParam("language") String language,
    	String body) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}/{version}/ids")
    @Produces(MediaType.APPLICATION_XML)
    public Response latestYAML2(
        @PathParam("classification") String classification,
        @PathParam("version") String version) 
    {
        return Response.status(501).build();
    }*/

    @GET
    @Path("/{classification}/{documentNumber}/{version}/{path:.+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON3(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("clause") String clause,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{classification}/{documentNumber}/{version}/{path:.+}")
    @Produces("application/yaml")
    public Response latestYAML3(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("version") String version,
        @PathParam("clause") String clause,
        @QueryParam("timespan") String timespan,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @PUT
    @Path("/{classification}/{documentNumber}/{version}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putJSON(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("version") String version,
       	String body) 
    {
        return Response.status(501).build();
    }

    @PUT
    @Path("/{classification}/{documentNumber}/{version}")
    @Consumes("application/yaml")
    public Response putYAML(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("version") String version,
        String body) 
    {
        return Response.status(501).build();
    }

}