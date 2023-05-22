package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final ConcurrentMap<Long, Post> mapRepo;
    private final AtomicLong idCounter = new AtomicLong();

    public PostRepository() {
        this.mapRepo = new ConcurrentHashMap<>();
    }

    public Collection<Post> all() {
        return  mapRepo.values();
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(mapRepo.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long id = idCounter.incrementAndGet();
            post.setId(id);
            mapRepo.put(id, post);
        } else {
            long concurrentId = post.getId();
            mapRepo.put(concurrentId, post);
        }
        return post;
    }

    public void removeById(long id) {
        mapRepo.remove(id);
    }
}
