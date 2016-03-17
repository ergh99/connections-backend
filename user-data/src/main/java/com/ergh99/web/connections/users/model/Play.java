package com.ergh99.web.connections.users.model;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by syn227 on 3/1/16.
 * Model class representing a single play of a specific game
 */
@lombok.extern.slf4j.XSlf4j
public class Play extends VertexWrapper {

    public static final String V_CLASS = "Play";

    public Play(Vertex vertex) {
        super(vertex);
    }

    public Object getProperty(properties property) {
        return super.getProperty(property);
    }

    public LocalDate getDate() {
        return (LocalDate) getProperty(properties.date);
    }

    public List<User> getPlayers() {
        List<User> result = new ArrayList<>();
        v().vertices(Direction.IN, "played").forEachRemaining(v -> result.add(new User(v)));
        return result;
    }

    public enum properties {
        date
    }

    @Override
    protected Enum[] properties() {
        Enum[] superProperties = super.properties();
        Enum[] myProperties = properties.values();
        Enum[] result = new Enum[superProperties.length + myProperties.length];
        System.arraycopy(superProperties, 0, result, 0, superProperties.length);
        System.arraycopy(myProperties, 0, result, superProperties.length, myProperties.length);
        return result;
    }
}
