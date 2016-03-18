package com.ergh99.web.connections.users.model;

import org.apache.tinkerpop.gremlin.structure.Element;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by syn227 on 3/16/16.
 * Class for edge and vertex wrappers to extend
 */
@lombok.extern.slf4j.XSlf4j
public abstract class ElementWrapper {

    protected final Element element;

    public ElementWrapper(Element element) {
        if (element == null) {
            throw log.throwing(new IllegalArgumentException("Attempted to wrap null element"));
        }
        this.element = element;
    }

    protected Element e() {
        return element;
    }

    protected Object getProperty(Enum property) {
        return element.property(property.name()).orElse(null);
    }

    public void setProperty(Enum property, Object value) {
        element.property(property.name(), value);
    }

    protected abstract Enum[] properties();

    @Override
    public String toString() {
        return Arrays.stream(properties())
            .map(p -> p.name() + "=" + getProperty(p) + "")
            .collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof ElementWrapper)) return false;

        ElementWrapper ewOther = (ElementWrapper) other;
        return Arrays.stream(properties()).allMatch(p ->
            Objects.equals(getProperty(p), ewOther.getProperty(p))
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.stream(properties()).map(this::getProperty).toArray());
    }

    protected static Enum[] joinEnums(Enum[] a, Enum[] b) {
        Enum[] result = new Enum[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
