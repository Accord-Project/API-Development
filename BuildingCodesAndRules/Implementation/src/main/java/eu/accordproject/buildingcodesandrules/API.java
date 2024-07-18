/*
Tom Beach
Copyright (c) Cardiff University
*/

package eu.accordproject.buildingcodesandrules;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.catalina.filters.CorsFilter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/*") // set the path to REST web services
public class API extends ResourceConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(API.class);

  public API() {

    LOGGER.info("Checking Environment Variables");
    Dotenv dotenv = Dotenv.load();

    String graphDBUURL = dotenv.get("GRAPHDB_URL");
    String graphDBUsername = dotenv.get("GRAPHDB_USERNAME");
    String graphDBPassword = dotenv.get("GRAPHDB_PASSWORD");

    if (graphDBUURL == null || graphDBPassword == null || graphDBUsername == null) {
        LOGGER.error("Environment variables not set");
        System.exit(1);
    }

    LOGGER.info("Starting.....");

    GraphDBService graphDBService = new GraphDBService(graphDBUURL,graphDBUsername,graphDBPassword);

    register(Endpoints.class);
    register(CorsFilter.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(graphDBService).to(GraphDBService.class);
      }
    });
  }
}
