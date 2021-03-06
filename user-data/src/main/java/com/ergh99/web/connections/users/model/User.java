package com.ergh99.web.connections.users.model;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syn227 on 3/1/16.
 * Model class for user interaction
 */
@lombok.extern.slf4j.XSlf4j
public class User extends VertexWrapper {

    public static final String V_CLASS = "User";

    public User(Vertex vertex) {
        super(vertex);
    }

    public Object getProperty(properties property) {
        return super.getProperty(property);
    }

    public void setProperty(properties property, Object value) {
        super.setProperty(property, value);
    }

    public List<User> getFriends() {
        log.entry();
        List<User> result = new ArrayList<>();
        v().vertices(Direction.OUT, Friendship.E_CLASS).forEachRemaining(v -> result.add(new User(v)));
        return log.exit(result);
    }

    public List<Play> getPlays() {
        log.entry();
        List<Play> result = new ArrayList<>();
        v().vertices(Direction.OUT, PlayRecord.E_CLASS).forEachRemaining(v -> result.add(new Play(v)));
        return log.exit(result);
    }

    public Friendship addFriend(User friend) {
        log.entry(friend);
        Edge edge = v().addEdge(Friendship.E_CLASS, friend.v());
        return log.exit(new Friendship(edge));
    }

    public PlayRecord recordPlay(Play play) {
        log.entry(play);
        Edge edge = v().addEdge(PlayRecord.E_CLASS, play.v());
        return log.exit(new PlayRecord(edge));
    }

    public enum properties {
        username, bggUsername
    }

    @Override
    protected Enum[] properties() {
        return ElementWrapper.joinEnums(super.properties(), properties.values());
    }
}
