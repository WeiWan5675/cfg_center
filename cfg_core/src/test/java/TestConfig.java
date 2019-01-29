import com.weiwan.common.cfg.pojo.CfgConstant;
import com.weiwan.common.cfg.pojo.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2019/1/29 11:34
 * @Author: xiaozhennan
 * @Package: PACKAGE_NAME
 * @ClassName: TestConfig
 * @Description:
 **/
public class TestConfig {


    public static void main(String[] args) {
        Config conf = new Config();


        conf.put("testMap", new HashMap<String, String>());
        conf.put("testList", new ArrayList<String>());
        conf.put("testString", "adada");

        conf.put("testtt", new CfgConstant());

        List<String> testList = conf.getList("testList");

        Map<String, String> testMap = conf.getMap("testMap");

        Object testtt = conf.getObject("testtt");

        String testString = conf.get("testString");

    }

}
