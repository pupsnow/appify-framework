package be.appify.view.jaxrs;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;

public class InstanceDecorator<T> implements Instance<T> {

    private final List<T> list;

    public InstanceDecorator(List<T> list) {
        this.list = list;
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public T get() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instance<T> select(Annotation... qualifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUnsatisfied() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAmbiguous() {
        throw new UnsupportedOperationException();
    }

}
