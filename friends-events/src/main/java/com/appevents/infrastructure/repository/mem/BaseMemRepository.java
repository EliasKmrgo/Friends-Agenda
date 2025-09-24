package com.appevents.infrastructure.repository.mem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BaseMemRepository<T> {
    protected final Map<Long, T> data = new ConcurrentHashMap<>();
    protected final AtomicLong seq = new AtomicLong(0);

    private final Function<T, Long> idGetter;
    private final BiConsumer<T, Long> idSetter;

    public BaseMemRepository(Function<T, Long> idGetter, BiConsumer<T, Long> idSetter) {
        this.idGetter = idGetter;
        this.idSetter = idSetter;
    }

    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    public T save(T entity) {
        Long id = idGetter.apply(entity);
        if (id == null) {
            id = seq.incrementAndGet();
            idSetter.accept(entity, id);
        }
        data.put(id, entity);
        return entity;
    }

    public void delete(Long id) {
        data.remove(id);
    }
}

