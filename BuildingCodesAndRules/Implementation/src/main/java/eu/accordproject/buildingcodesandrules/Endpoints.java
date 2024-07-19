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
import eu.accordproject.buildingcodesandrules.models.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
public class Endpoints {

	//various helper methods to actually perform the main tasks

	public String serializeYaml(Object object) {
		try {
			YAMLMapper mapper = new YAMLMapper();
    		mapper.findAndRegisterModules();
        	mapper.configure(YAMLGenerator.Feature.SPLIT_LINES,false);
        	mapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES,true);
        	mapper.configure(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS,true);
        	return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public ServerIdentity getIdentity() {
		ServerIdentity identity = new ServerIdentity();
		identity.setName("Exemplar Regulations Server");
		identity.setDescription("Exemplar Regulations Server");
		identity.setOperator("ACCORD Project");
		String[] codes = graphDB.getGraphList().split("\n");
		for (String code: codes) System.out.println(code);

		return identity;
	}

	public String jsonToYaml(String json) {
		try {
			return serializeYaml(new ObjectMapper().readTree(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getRegulationText(String classification, Integer documentNumber, String version, String purpose, String ruleFormat, String language, boolean yaml) {
		String regText="";

		if (yaml) return jsonToYaml(regText);
		return regText;
	}

	@Inject
    public GraphDBService graphDB;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rootJSON()  
    {
       return Response.ok(getIdentity()).build();
    }

    @GET
    @Path("/")
    @Produces("application/yaml")
    public Response rootYAML()  
    {
       return Response.ok(serializeYaml(getIdentity())).build();
    }

    @GET
    @Path("/{classification}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON(
        @PathParam("classification") String classification,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
         return Response.ok(getRegulationText(classification,1,null,purpose,ruleFormat,language,false)).build();
    }

    @GET
    @Path("/{classification}")
    @Produces("application/yaml")
    public Response latestYAML(
        @PathParam("classification") String classification,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.ok(getRegulationText(classification,1,null,purpose,ruleFormat,language,true)).build();
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
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.ok(getRegulationText(classification,documentNumber,null,purpose,ruleFormat,language,false)).build();
    }

    @GET
    @Path("/{classification}/{documentNumber}")
    @Produces("application/yaml")
    public Response latestYAML(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.ok(getRegulationText(classification,documentNumber,null,purpose,ruleFormat,language,true)).build();
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
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.ok(getRegulationText(classification,documentNumber,version,purpose,ruleFormat,language,false)).build();
    }

    @GET
    @Path("/{classification}/{documentNumber}/{version}")
    @Produces("application/yaml")
    public Response latestYAML(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("version") String version,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.ok(getRegulationText(classification,documentNumber,version,purpose,ruleFormat,language,true)).build();
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

    @GET
    @Path("/{classification}/{documentNumber}/{version}/{path:.+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON3(
        @PathParam("classification") String classification,
        @PathParam("documentNumber") Integer documentNumber,
        @PathParam("clause") String clause,
        @QueryParam("purpose") String purpose,
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
        @QueryParam("purpose") String purpose,
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