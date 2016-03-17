package com.ergh99.web.connections.users.model;

import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by syn227 on 3/17/16.
 * Test for {@link ElementWrapper}
 */
public class ElementWrapperTest {

    public static final String DB_URL = "MEMORY:ELEMENT-WRAPPER-TEST";

    private OrientGraphFactory graphFactory = new OrientGraphFactory(DB_URL);
    private Graph graph;

    @Before
    public void setUp() {
        graph = graphFactory.getTx();
    }

    @After
    public void tearDown() throws Exception {
        graph.edges().forEachRemaining(Edge::remove);
        graph.vertices().forEachRemaining(Vertex::remove);
        graph.close();
    }

    @Test
    public void testConstructor() {
        assertThatThrownBy(() -> new VertexWrapper(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testToString() throws Exception {
        ElementWrapper wrapper = new VertexWrapper(graph.addVertex("Sample"));
        wrapper.setProperty(VertexWrapper.properties.id, "1");
        String result = wrapper.toString();
        assertThat(result)
            .contains("id=1");
    }

    @Test
    public void testEquals() throws Exception {
        ElementWrapper wrapper1 = new VertexWrapper(graph.addVertex("Sample"));
        wrapper1.setProperty(VertexWrapper.properties.id, "1");

        ElementWrapper wrapper2 = new VertexWrapper(graph.addVertex("Sample"));
        wrapper2.setProperty(VertexWrapper.properties.id, "2");

        assertThat(wrapper1.equals(wrapper2)).isFalse();

        wrapper2.setProperty(VertexWrapper.properties.id, "1");
        assertThat(wrapper1.equals(wrapper2)).isTrue();

        assertThat(wrapper1.equals(new Object())).isFalse();

        assertThat(wrapper1.equals(null)).isFalse();
    }
}
