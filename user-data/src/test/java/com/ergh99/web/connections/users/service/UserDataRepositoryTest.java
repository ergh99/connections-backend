package com.ergh99.web.connections.users.service;

import com.ergh99.web.connections.users.config.UserDataServiceConfiguration;
import com.ergh99.web.connections.users.data.UserDataGraphFactory;
import com.ergh99.web.connections.users.model.Friendship;
import com.ergh99.web.connections.users.model.Play;
import com.ergh99.web.connections.users.model.PlayRecord;
import com.ergh99.web.connections.users.model.User;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

import static com.ergh99.web.connections.users.model.VertexWrapper.properties.id;
import static com.ergh99.web.connections.users.model.Play.properties.date;
import static com.ergh99.web.connections.users.model.User.properties.username;

/**
 * Created by syn227 on 3/1/16.
 * Tests for the {@link UserDataRepository}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserDataServiceConfiguration.class)
public class UserDataRepositoryTest {

    @Autowired
    private UserDataGraphFactory graphFactory;

    @Autowired
    private UserDataRepository userDataRepository;

    private Graph db;

    @Before
    public void setUp() throws Exception {
        db = graphFactory.newGraphTraversalSource().getGraph().get();
        db.edges().forEachRemaining(Edge::remove);
        db.vertices().forEachRemaining(Vertex::remove);
    }

    @Test
    public void testGetUserWithoutFriends() throws Exception {
        Vertex v = db.addVertex(User.V_CLASS);
        v.property(id.name(), "1");
        v.property(username.name(), "foo");
        User directCreatedUser = new User(v);
        assertThat(db.vertices())
            .hasSize(1)
            .extracting(Vertex::label).containsExactly(User.V_CLASS);
        User serviceLoadedUser = userDataRepository.getUser("1");
        assertThat(serviceLoadedUser)
            .isNotNull()
            .hasFieldOrPropertyWithValue(id.name(), "1")
            .isEqualToComparingFieldByField(directCreatedUser);
    }

    @Test
    public void testGetUserWithOneFriend() throws Exception {
        Vertex v = db.addVertex(User.V_CLASS);
        User directCreatedUser = new User(v);
        directCreatedUser.setProperty(id, "1");
        //v.property(id.name(), "1");
        User friendUser = userDataRepository.getUser("2");
        directCreatedUser.addFriend(friendUser);
        //Vertex friendVertex = db.traversal().V().has(User.V_CLASS, id.name(), "2").next();
        //v.addEdge(Friendship.E_CLASS, friendVertex);
        assertThat(v.edges(Direction.OUT, Friendship.E_CLASS))
            .hasSize(1)
            .extracting(Edge::inVertex)
            .isNotNull();
        assertThat(db.vertices())
            .hasSize(2)
            .extracting(Vertex::label)
            .containsExactly(User.V_CLASS, User.V_CLASS);
        User serviceLoadedUser = userDataRepository.getUser("1");
        assertThat(serviceLoadedUser)
            .isNotNull()
            .isEqualToComparingFieldByField(directCreatedUser);
        assertThat(serviceLoadedUser.getFriends())
            .isNotNull()
            .hasSize(1)
            .extracting(com.ergh99.web.connections.users.model.User::getId)
            .containsExactly("2");
    }

    @Test
    public void testAddFriend() {
        User user = userDataRepository.getUser("1");
        user.setProperty(username, "foo");
        User friend = userDataRepository.getUser("2");

        user.addFriend(friend);

        assertThat(user.getProperty(username))
            .isEqualTo("foo");
        assertThat(user.getFriends())
            .hasSize(1)
            .containsExactly(friend);

        user = userDataRepository.getUser("1");

        assertThat(user.getFriends())
            .hasSize(1)
            .containsExactly(friend);
    }

    @Test
    public void testGetUserWithPlay() throws Exception {
        Vertex userVertex = db.addVertex(User.V_CLASS);
        userVertex.property(id.name(), "1");
        userVertex.property(username.name(), "foo");
        User directCreatedUser = new User(userVertex);

        assertThat(db.vertices())
            .hasSize(1)
            .extracting(Vertex::label)
            .containsExactly(User.V_CLASS);

        LocalDate playDate = LocalDate.now();
        Vertex playVertex = db.addVertex(Play.V_CLASS);
        playVertex.property(date.name(), playDate);
        Play play = new Play(playVertex);
        play.setProperty(date, playDate);

        userVertex.addEdge(PlayRecord.E_CLASS, playVertex);
        directCreatedUser.getPlays().add(play);

        assertThat(db.vertices())
            .hasSize(2)
            .extracting(Vertex::label)
            .containsExactly(User.V_CLASS, Play.V_CLASS);

        User serviceLoadedUser = userDataRepository.getUser("1");

        assertThat(serviceLoadedUser)
            .isNotNull()
            .hasFieldOrPropertyWithValue(id.name(), "1")
            .isEqualToComparingFieldByField(directCreatedUser);
        assertThat(serviceLoadedUser.getPlays())
            .hasSize(1)
            .containsExactly(play);
    }

    @Test
    public void testAddPlay() throws Exception {
        User user = userDataRepository.getUser("1");
        user.setProperty(username, "foo");

        LocalDate playDate = LocalDate.now();

        Vertex playVertex = db.addVertex(Play.V_CLASS);
        playVertex.property(id.name(), "1");
        playVertex.property(date.name(), playDate);
        Play play = new Play(playVertex);

        user.recordPlay(play);

        assertThat(user.getProperty(username))
            .isEqualTo("foo");
        assertThat(user.getPlays())
            .hasSize(1)
            .containsExactly(play);

        user = userDataRepository.getUser("1");

        assertThat(user.getPlays())
            .hasSize(1)
            .containsExactly(play);
    }

    @Test
    public void testTwoFriendsOnePlay() {
        User user1 = userDataRepository.getUser("1");
        user1.setProperty(username, "foo");

        User user2 = userDataRepository.getUser("2");
        user2.setProperty(username, "bar");

        LocalDate playDate = LocalDate.now();

        Vertex playVertex = db.addVertex(Play.V_CLASS);
        Play play = new Play(playVertex);
        play.setProperty(id, "1");
        play.setProperty(date, playDate);

        user1.addFriend(user2);
        user1.recordPlay(play);
        user2.recordPlay(play);

        assertThat(user1.getProperty(username))
            .isEqualTo("foo");
        assertThat(user1.getPlays())
            .hasSize(1)
            .containsExactly(play);

        assertThat(user2.getProperty(username))
            .isEqualTo("bar");
        assertThat(user2.getPlays())
            .hasSize(1)
            .containsExactly(play);

        assertThat(play.getPlayers())
            .hasSize(2)
            .containsExactly(user1, user2);
    }
}
