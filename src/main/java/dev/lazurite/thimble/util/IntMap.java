package dev.lazurite.thimble.util;

import com.google.common.collect.Lists;

import java.util.List;

public class IntMap<T> {
    private final List<IntPair<T>> list;
    private int nextId;

    public IntMap() {
        list = Lists.newArrayList();
    }

    public void add(T object) {
        list.add(new IntPair<>(nextId, object));
        ++nextId;
    }

    public int get(T object) {
        for (IntPair<T> pair : list) {
            if (pair.getObject().equals(object)) {
                return pair.getInt();
            }
        }

        return -1;
    }

    public T get(int integer) {
        for (IntPair<T> pair : list) {
            if (pair.getInt() == integer) {
                return pair.getObject();
            }
        }

        return null;
    }

    public List<T> getAll() {
        List<T> out = Lists.newArrayList();
        list.forEach(pair -> out.add(pair.getObject()));
        return out;
    }

    public void remove(T object) {
        list.removeIf(pair -> pair.getObject().equals(object));
    }

    public void remove(int integer) {
        list.removeIf(pair -> pair.getInt() == integer);
    }

    public void removeAll() {
        list.clear();
    }

    static class IntPair<U> {
        private final int integer;
        private final U object;

        public IntPair(int integer, U object) {
            this.integer = integer;
            this.object = object;
        }

        public int getInt() {
            return this.integer;
        }

        public U getObject() {
            return this.object;
        }
    }
}


