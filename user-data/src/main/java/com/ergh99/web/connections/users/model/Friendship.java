package com.ergh99.web.connections.users.model;

import org.apache.tinkerpop.gremlin.structure.Edge;

/**
 * Created by syn227 on 3/16/16.
 * Wrapper class to represent friendsWith edges
 */
public class Friendship extends EdgeWrapper {

    public static final String E_CLASS = "friendsWith";

    public Friendship(Edge edge) {
        super(edge);
    }

    public Object getProperty(properties property) {
        return e().property(property.name()).orElse(null);
    }

    @Override
    protected Enum[] properties() {
        return properties.values();
    }

    public enum properties {
        createdDate
    }
}
