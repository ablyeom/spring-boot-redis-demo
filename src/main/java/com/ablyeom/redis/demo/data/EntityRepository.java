package com.ablyeom.redis.demo.data;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     Test code.
 * Project : demo
 * Package : {@link com.ablyeom.redis.demo.data}
 * Creator : abel
 * Date : 3/22/18
 * </pre>
 */
@Component
public class EntityRepository implements CommandLineRunner {

    private Map<Long, Entity> mockDB = new HashMap<>();

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Override
    public void run(String... args) throws Exception {
        connectionFactory.getConnection().serverCommands().flushAll();

        for (int i = 0; i < 10000; i++) {
            Entity entity = new Entity();
            entity.setId((long) (i + 1));
            entity.setTimestamp(new DateTime().getMillis());
            mockDB.put(entity.getId(), entity);
        }
    }

    public Entity save(Entity entity) {

        if (entity.getId() == null) {
            return create(entity);
        }

        return update(entity);
    }

    public void delete(Long id) {
        mockDB.remove(id);
    }

    public void deleteAll() {
        mockDB.clear();
    }

    public List<Entity> findAll() {
        return new ArrayList<>(mockDB.values());
    }

    public Entity findOne(Long id) {
        if (mockDB.containsKey(id)) {
            return mockDB.get(id);
        }

        throw new NullPointerException("Cannot find");
    }

    private Entity create(Entity entity) {
        entity.setId(getLastKey() + 1L);
        entity.setTimestamp(new DateTime().getMillis());

        mockDB.put(entity.getId(), entity);

        return entity;
    }

    private Entity update(Entity entity) {

        Entity found = mockDB.get(entity.getId());

        if (found.getId().equals(entity.getId())) {
            mockDB.remove(entity.getId());
            entity.setTimestamp(new DateTime().getMillis());
            mockDB.put(entity.getId(), entity);
        }

        throw new NullPointerException("Couldn't update");
    }

    private Long getLastKey() {

        if (mockDB.size() == 0) {
            return 0L;
        }

        List<Map.Entry<Long, Entity>> entryList = new ArrayList<>(mockDB.entrySet());
        return entryList.get(entryList.size() - 1).getKey();
    }
}
