package com.ablyeom.redis.demo.controller;

import com.ablyeom.redis.demo.data.Entity;
import com.ablyeom.redis.demo.data.EntityRepository;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <pre>
 * Project : demo
 * Package : {@link com.ablyeom.redis.demo.controller}
 * Creator : abel
 * Date : 3/22/18
 * </pre>
 */
@RestController
public class RestEntityController {

    private static final Logger log = LoggerFactory.getLogger("Demo");

    private final EntityRepository entityRepository;

    @Autowired
    public RestEntityController(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Cacheable(value = "entities")
    @GetMapping("entities")
    public List<Entity> getEntities() {

        return entityRepository.findAll();
    }

    @Cacheable(value = "entity", key = "#id")
    @GetMapping("entities/{id}")
    public Entity getEntity(@PathVariable Long id) {
        return entityRepository.findOne(id);
    }

    @CacheEvict(value = "entities")
    @DeleteMapping("entities")
    public void deleteAll() {
        entityRepository.deleteAll();
    }

    @CacheEvict(value = "entity", key = "#id")
    @DeleteMapping("entities/{id}")
    public void delete(@PathVariable Long id) {
        entityRepository.delete(id);
    }

    @CachePut(value = "entity", key = "#entity.id")
    @PutMapping("entities/{id}")
    public Entity updateEntityById(@PathVariable Long id, @RequestBody Entity entity) {
        log.info("update entity with id {}", entity.getId());
        entity.setTimestamp(new DateTime().getMillis());
        entityRepository.save(entity);
        return entity;
    }

    @PostMapping("entities")
    public Entity createEntity() {

        Entity entity = new Entity();
        entityRepository.save(entity);

        return entity;
    }
}
