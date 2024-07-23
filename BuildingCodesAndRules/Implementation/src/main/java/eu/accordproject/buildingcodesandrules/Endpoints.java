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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.dcom.core.compliancedocument.ComplianceDocument;
import accord.regulationsimporter.OntologyComplianceDocumentDeserialiser;

@Path("/")
public class Endpoints {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	//various helper methods to actually perform the main tasks

	private String serializeYaml(Object object) {
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

	private BuildingCodeIndex getBuildingCodeIndex(ServerIdentity identity, String classification, String jurisdiction) {
        for (BuildingCodeIndex index : identity.getDocumentList()) {
            if (index.getClassification().equals(classification) && index.getJurisdiction().equals(jurisdiction)) return index;
        }
        return null;
    }

    private String getFullNameFromURL(String country, String classification, String version, String language) {
        String regText = getRegulationText(classification,version,null,null,language,country,false);
        ComplianceDocument document = OntologyComplianceDocumentDeserialiser.parseComplianceDocument(regText);
        if (document == null) {
            System.out.println("Failed to parse document:"+country+":"+classification+":"+version.trim()+":"+language);
            return "";
        }
        System.out.println("TITLE"+document.getMetaDataString("dcterms:title"));
        return document.getMetaDataString("dcterms:title");
    }

	private ServerIdentity getIdentity() {
		ServerIdentity identity = new ServerIdentity();
		identity.setName("Exemplar Regulations Server");
		identity.setDescription("Exemplar Regulations Server");
		identity.setOperator("ACCORD Project");

		String[] codes = graphDB.getGraphList().split("\n");

		for (String code: codes) {
			if (code.equals("contextID")) continue;
			String[] splitCode = code.split("/");
			// URL IS https://graphdb.accordproject.eu/resource/aec3po/${country}/${classifier}/${language}/${date}
			if (splitCode.length < 4) {
                System.out.println("[Error] Invalid Graph Path");
                continue;
            }
			String date = splitCode[splitCode.length-1];
			String language = splitCode[splitCode.length-2];
			String classification = splitCode[splitCode.length-3];
			String country = splitCode[splitCode.length-4];
            LocalDate parsedDate = null;
            try {
                parsedDate = LocalDate.parse(date.trim(),formatter);
            } catch (Exception e) {
                System.out.println("Invalid Date("+date.trim()+") time for Graph:"+code);
                continue;
            }
            BuildingCodeIndex index = getBuildingCodeIndex(identity,classification,country);
            if (index == null) {
                //create the index and add this as latest version
                index = new BuildingCodeIndex();
                identity.getDocumentList().add(index);
                index.setClassification(classification);
                index.setFullName(getFullNameFromURL(country, classification,date,language));
                index.setJurisdiction(country);
                index.setLanguage(language);

            }
            BuildingCodeVersion version = new BuildingCodeVersion();
           
            index.getVersions().add(version);
            version.setVersion(getFullNameFromURL(country, classification,date,language));
            version.setVersionDate(parsedDate);
            LocalDate latestVersion = parsedDate;
            String latestVersionText = version.getVersion();
            for (BuildingCodeVersion v : index.getVersions()) {
                if (v.getVersionDate().isAfter(latestVersion)) {
                    latestVersion = v.getVersionDate();
                    latestVersionText = v.getVersion();
                }
            }
            index.setLatestVersion(latestVersionText);
            index.setLatestVersionDate(latestVersion);
		}
		return identity;
	}

	private String jsonToYaml(String json) {
		try {
			return serializeYaml(new ObjectMapper().readTree(json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getRegulationText(String classification, String version, String purpose, String ruleFormat, String language, String jurisdiction, boolean yaml) {
		if (purpose == null || purpose.equals("")) purpose ="explicit";
        if (ruleFormat == null || ruleFormat.equals("")) ruleFormat ="explicit";
        if (language == null || language.equals("")) language = "en-gb";
        String regText=graphDB.getJSONLD(classification, version, jurisdiction, language);
        //implement reparsing for purpose and rule format

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
    @Path("/{jurisdiction}/{classification}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
         return Response.ok(getRegulationText(classification,null,purpose,ruleFormat,language,jurisdiction,false)).build();
    }

    @GET
    @Path("/{jurisdiction}/{classification}")
    @Produces("application/yaml")
    public Response latestYAML(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.ok(getRegulationText(classification,null,purpose,ruleFormat,language,jurisdiction,true)).build();
    }

    @POST
    @Path("/{jurisdiction}/{classification}/graphQL")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response graphQL(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
    	@QueryParam("language") String language,
    	String body) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{jurisdiction}/{classification}/ids")
    @Produces(MediaType.APPLICATION_XML)
    public Response latestYAML(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification) 
    {
        return Response.status(501).build();
    }

   
	@GET
    @Path("/{jurisdiction}/{classification}/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @PathParam("version") String version,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.ok(getRegulationText(classification,version,purpose,ruleFormat,language,jurisdiction,false)).build();
    }

    @GET
    @Path("/{jurisdiction}/{classification}/{version}")
    @Produces("application/yaml")
    public Response latestYAML(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @PathParam("version") String version,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.ok(getRegulationText(classification,version,purpose,ruleFormat,language,jurisdiction,true)).build();
    }

    @POST
    @Path("/{jurisdiction}/{classification}/{version}/graphQL")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response graphQL(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @PathParam("version") String version,
    	@QueryParam("language") String language,
    	String body) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{jurisdiction}/{classification}/{version}/ids")
    @Produces(MediaType.APPLICATION_XML)
    public Response latestYAML(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @PathParam("version") String version) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{jurisdiction}/{classification}/{version}/{path:.+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response latestJSON3(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @PathParam("clause") String clause,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @GET
    @Path("/{jurisdiction}/{classification}/{version}/{path:.+}")
    @Produces("application/yaml")
    public Response latestYAML3(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @PathParam("version") String version,
        @PathParam("clause") String clause,
        @QueryParam("purpose") String purpose,
    	@QueryParam("ruleFormat") String ruleFormat,
    	@QueryParam("language") String language) 
    {
        return Response.status(501).build();
    }

    @PUT
    @Path("/{jurisdiction}/{classification}/{version}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putJSON(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @PathParam("version") String version,
       	String body) 
    {
        return Response.status(501).build();
    }

    @PUT
    @Path("/{jurisdiction}/{classification}/{version}")
    @Consumes("application/yaml")
    public Response putYAML(
        @PathParam("jurisdiction") String jurisdiction,
        @PathParam("classification") String classification,
        @PathParam("version") String version,
        String body) 
    {
        return Response.status(501).build();
    }

}