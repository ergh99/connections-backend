package com.ergh99.web.connections.users.service.impl;

import com.ergh99.web.connections.users.data.UserDataGraphFactory;
import com.ergh99.web.connections.users.model.Play;
import com.ergh99.web.connections.users.model.User;
import com.ergh99.web.connections.users.model.VertexWrapper;
import com.ergh99.web.connections.users.service.UserDataRepository;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by syn227 on 3/1/16.
 * Service class to access user database
 */
@Repository
@lombok.extern.slf4j.XSlf4j
public class UserDataRepositoryImpl implements UserDataRepository {

    private UserDataGraphFactory graphFactory;

    @Autowired
    public UserDataRepositoryImpl(UserDataGraphFactory graphFactory) {
        this.graphFactory = graphFactory;
    }

    @Override
    public User getUser(String userId) {
        log.entry(userId);
        Vertex userVertex = getOrCreateVertexById(userId, User.V_CLASS);
        User result = new User(userVertex);
        return log.exit(result);
    }

    @Override
    public Play getPlay(String playId) {
        log.entry(playId);
        Vertex playVertex = getOrCreateVertexById(playId, Play.V_CLASS);
        Play result = new Play(playVertex);
        return log.exit(result);
    }

    private Vertex getOrCreateVertexById(String id, String vertexClass) {
        log.entry(id);
        GraphTraversalSource source = graphFactory.newGraphTraversalSource();
        Vertex result =
            source.V().has(vertexClass, VertexWrapper.properties.id.name(), id).tryNext()
                .orElseGet(() -> {
                    Vertex newUser = source.getGraph().get().addVertex(vertexClass);
                    newUser.property(VertexWrapper.properties.id.name(), id);
                    return newUser;
                });
        return log.exit(result);
    }
}
