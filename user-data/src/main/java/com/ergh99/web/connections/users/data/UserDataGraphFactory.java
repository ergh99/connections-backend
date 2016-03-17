package com.ergh99.web.connections.users.data;

import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by syn227 on 3/1/16.
 * Generates transactional graphs to access the user database
 */
@Component
public class UserDataGraphFactory {

    private OrientGraph graph;

    @Autowired
    public UserDataGraphFactory(OrientGraph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public GraphTraversalSource newGraphTraversalSource() {
        return GraphTraversalSource.build().create(graph);
    }
}
