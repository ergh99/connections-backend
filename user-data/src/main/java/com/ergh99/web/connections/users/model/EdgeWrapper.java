package com.ergh99.web.connections.users.model;

import org.apache.tinkerpop.gremlin.structure.Edge;

/**
 * Created by syn227 on 3/16/16.
 * Base class for edge wrappers
 */
@lombok.extern.slf4j.XSlf4j
public abstract class EdgeWrapper extends ElementWrapper {

    public EdgeWrapper(Edge edge) {
        super(edge);
    }

    protected Edge e() {
        return (Edge) super.e();
    }
}
