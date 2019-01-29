import com.weiwan.common.cfg.core.admin.Admin;
import com.weiwan.common.cfg.core.ConfigCenter;
import com.weiwan.common.cfg.pojo.Config;

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
        conf.put("cfg.zk.servers", "127.0.0.1:2181");
        conf.put("cfg.db.type", "redis");

        conf.put("cfg.db.redis.server", "192.168.101.101");
        conf.put("cfg.db.redis.port", "3306");
        conf.put("cfg.db.redis.pass", "ipaynow");

        conf.put("cfg.zk.base.dir", "configcenter");

        //启动配置中心
        ConfigCenter configCenter = ConfigCenter.createInstance(conf);
        //配置中心管理接口
        Admin admin = configCenter.getAdmin();
        //获得配置缓存
        Map<String, Config> cache = configCenter.getCache();
//        //获得model配置
//        Config aaa = cache.get("model1");
//        //获得配置
//        String key1 = aaa.get("key1");
//        //使用配置
//        System.out.println(key1);
        while (true) {

        }
    }

}
