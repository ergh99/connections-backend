package com.ergh99.web.connections.users.config;

import com.ergh99.web.connections.users.data.UserDataGraphFactory;
import com.ergh99.web.connections.users.model.Friendship;
import com.ergh99.web.connections.users.model.Play;
import com.ergh99.web.connections.users.model.User;
import com.ergh99.web.connections.users.model.VertexWrapper;
import com.ergh99.web.connections.users.service.UserDataRepository;
import com.ergh99.web.connections.users.service.impl.UserDataRepositoryImpl;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
import org.apache.tinkerpop.gremlin.util.config.YamlConfiguration;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by syn227 on 3/1/16.
 * Spring context configuration
 */

@org.springframework.context.annotation.Configuration
@PropertySources({
    @PropertySource("classpath:application.properties"),
    @PropertySource("classpath:build-info.properties")
})
public class UserDataServiceConfiguration {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.config.name}")
    private String dbConfigName;

    @Bean
    public UserDataRepository userDataService() throws ConfigurationException, IOException, URISyntaxException {
        return new UserDataRepositoryImpl(userDataGraphFactory());
    }

    @Bean
    public Configuration userDataGraphConfiguration() throws URISyntaxException, IOException, ConfigurationException {
        YamlConfiguration config = new YamlConfiguration();
        URL dbConfigUrl = ClassLoader.getSystemResource(dbConfigName);
        config.load(Files.newBufferedReader(Paths.get(dbConfigUrl.toURI())));
        return config;
    }

    @Bean
    public UserDataGraphFactory userDataGraphFactory() throws ConfigurationException, IOException, URISyntaxException {
        OrientGraph graph = new OrientGraphFactory(dbUrl).setupPool(10).getTx();
//        GraphClasses.streamVertexClasses()
//            .map(GraphClasses::className)
//            .forEach(graph::createVertexClass);
//        GraphClasses.streamEdgeClasses()
//            .map(GraphClasses::className)
//            .forEach(graph::createEdgeClass);
        // TODO Create annotation driven scanning
        graph.createVertexClass("V_" + User.V_CLASS);
        graph.createVertexClass("V_" + Play.V_CLASS);
        graph.createEdgeClass("E_" + Friendship.E_CLASS);
        graph.createVertexIndex(VertexWrapper.properties.id.name(), User.V_CLASS, userDataGraphConfiguration());
        graph.createVertexIndex(VertexWrapper.properties.id.name(), Play.V_CLASS, userDataGraphConfiguration());
        return new UserDataGraphFactory(graph);
    }
}
