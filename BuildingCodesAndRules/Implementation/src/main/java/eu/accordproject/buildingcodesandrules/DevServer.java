/*
Tom Beach
Copyright (c) Cardiff University
*/

package eu.accordproject.buildingcodesandrules;

import org.testcontainers.containers.FixedHostPortGenericContainer;
import io.github.cdimascio.dotenv.Dotenv;
import org.testcontainers.utility.MountableFile;
import java.nio.file.Paths;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;

public class DevServer {

        public static int tomcatPort = 8080;
        private FixedHostPortGenericContainer<?> tomcat;
        
        public DevServer() {
                Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

                MountableFile warFile = MountableFile.forHostPath(Paths.get("target/buildingcodesandrules.war").toAbsolutePath(),511);
                String graphDBUURL = dotenv.get("GRAPHDB_URL");
                String graphDBUsername = dotenv.get("GRAPHDB_USERNAME");
                String graphDBPassword = dotenv.get("GRAPHDB_PASSWORD");

                tomcat = new FixedHostPortGenericContainer<>("tomcat:10.1-jdk17").withEnv("GRAPHDB_URL",graphDBUURL).withEnv("GRAPHDB_USERNAME",graphDBUsername).withEnv("GRAPHDB_PASSWORD",graphDBPassword)
                        .withCopyFileToContainer(warFile, "/usr/local/tomcat/webapps/ROOT.war")
                        .withFixedExposedPort(tomcatPort,8080);
                tomcat.start();
                Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LoggerFactory.getLogger(DevServer.class));
                tomcat.followOutput(logConsumer);


               Runtime.getRuntime().addShutdownHook(new Thread()
                        {
                        public void run() {
                                System.out.println("----Shutting Down Dev Server------");
                        }
                });
        }

        public void stop() {
                tomcat.stop();
        }

	public static void main(String[] args) {
        DevServer server = new DevServer();
        System.out.println("-----------");
        System.out.println("\u001b[1;32mDev Server deployed to http://localhost:"+DevServer.tomcatPort+"\33[0m");
        while (true) {
        	try {
        		Thread.sleep(10000);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }

	}

}