package dev.lazurite.thimble.util;

/**
 * Interface used for creating a list that can be ticked.
 * @param <T> any type you want :P
 */
public interface TickableList<T> {
    void add(T entry);
    void clear();
    void tick();
}
