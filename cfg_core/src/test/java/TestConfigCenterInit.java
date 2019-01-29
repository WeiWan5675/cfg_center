import com.weiwan.common.cfg.core.ConfigCenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2019/1/29 12:36
 * @Author: xiaozhennan
 * @Package: PACKAGE_NAME
 * @ClassName: TestConfigCenterInit
 * @Description:
 **/
public class TestConfigCenterInit {

    public static void main(String[] args) {
        Map<String, String> conf = new HashMap<String, String>();
        conf.put("cfg.zk.servers","127.0.0.1:2181");
        conf.put("cfg.db.type","redis");

        conf.put("cfg.db.redis.server","127.0.0.1");
        conf.put("cfg.db.redis.port","3306");
        conf.put("cfg.db.redis.user","admin");
        conf.put("cfg.db.redis.pass","redis123");

        conf.put("cfg.zk.base.dir","/configcenter");

        ConfigCenter configCenter = ConfigCenter.createInstance(conf);


    }

}
