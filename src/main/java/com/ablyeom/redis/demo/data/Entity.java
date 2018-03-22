package com.ablyeom.redis.demo.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * Project : demo
 * Package : {@link com.ablyeom.redis.demo}
 * Creator : abel
 * Date : 3/21/18
 * </pre>
 */
@Getter
@Setter
public class Entity implements Serializable {

    private Long id;
    private long timestamp;
}
