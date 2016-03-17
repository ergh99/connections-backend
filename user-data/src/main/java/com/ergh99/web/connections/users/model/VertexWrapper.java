package com.ergh99.web.connections.users.model;

import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 * Created by syn227 on 3/16/16.
 * Base class for vertex wrapper classes
 */
@lombok.extern.slf4j.XSlf4j
public class VertexWrapper extends ElementWrapper {

    public VertexWrapper(Vertex vertex) {
        super(vertex);
    }

    protected Vertex v() {
        return (Vertex) e();
    }

    @Override
    protected Enum[] properties() {
        return properties.values();
    }

    public enum properties {
        id
    }

    public String getId() {
        return (String) getProperty(properties.id);
    }
}
