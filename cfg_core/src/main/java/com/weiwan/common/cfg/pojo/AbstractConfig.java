package com.weiwan.common.cfg.pojo;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2019/1/29 11:25
 * @Author: xiaozhennan
 * @Package: com.weiwan.common.cfg.pojo
 * @ClassName: Config
 * @Description: 配置对象类
 **/
public abstract class AbstractConfig extends ConcurrentHashMap<String, Object> {
    static final String MAP_KEY_PREFIX = "_MAP_";
    static final String LIST_KEY_PREFIX = "_LIST_";
    static final String OBJECT_KEY_PREFIX = "_OBJ_";

    public Map<String, String> getMap(String key) {
        return (Map<String, String>) super.get(MAP_KEY_PREFIX + key);
    }

    public List<String> getList(String key) {
        return (List<String>) super.get(LIST_KEY_PREFIX + key);
    }

    public String get(String key) {
        return (String) super.get(key);
    }

    public Object getObject(String key) {
        return super.get(OBJECT_KEY_PREFIX + key);
    }


    public Object put(String key, Object val) {
        if (val instanceof Map) {
            return super.put(MAP_KEY_PREFIX + key, val);
        } else if (val instanceof Collection) {
            return super.put(LIST_KEY_PREFIX + key, val);
        } else if (val instanceof String) {
            return super.put(key, val);
        } else {
            return super.put(OBJECT_KEY_PREFIX + key, val);
        }
    }

}
