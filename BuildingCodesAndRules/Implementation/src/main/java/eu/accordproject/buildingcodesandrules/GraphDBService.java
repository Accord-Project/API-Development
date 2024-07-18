/*
Tom Beach
Copyright (c) Cardiff University
*/

package eu.accordproject.buildingcodesandrules;
import java.io.IOException;
import java.lang.System;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class GraphDBService {

	private String url;
	private String auth;
	private HttpClient client = HttpClient.newHttpClient();

	public GraphDBService(String _url, String _username, String _password) {
		url=_url;
		String credentials = _username + ":" + _password;
 		auth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
	}

	public String getJSONLD(String country, String document) {
		try{ 
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url+"/graphdb/repositories/aec3po/statements?context=%3C"+url+"/resource/aec3po/" + country + "/" + document + "/en-gb/v1%3E"))
    			.GET()
    			.setHeader("Accept", "application/ld+json;profile=http://www.w3.org/ns/json-ld#framed")
    			.setHeader("Link", "<https://ci.mines-stetienne.fr/aec3po/aec3po_frame.jsonld>; rel=\"http://www.w3.org/ns/json-ld#frame\"")
    			.setHeader("Authorization", auth)
    			.build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				return response.body();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
	}
}