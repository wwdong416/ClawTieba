import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @program distinguish
 * @author yz3702
 * @date 2018/3/6
 */
public class PropertiesUtils {
    private static Properties props;
    static {
//        String fileName ="test2.properties";
        props = new Properties();
        String proFilePath = System.getProperty("user.dir") + "\\claw.properties";


        try{
            InputStream in = new BufferedInputStream(new FileInputStream(proFilePath));
            props.load(new InputStreamReader(in,"GBK"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }
}
