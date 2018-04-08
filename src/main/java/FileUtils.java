import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program distinguish
 * @author yz3702
 * @date 2018/3/6
 */
public class FileUtils {
    public static final Logger log = LoggerFactory.getLogger(FileUtils.class);
    public static void createFile(String path){
        File out = new File(path);
        if (!out.getParentFile().exists()) {
            log.warn(String.format("文件夹不存在，将新建%s",out.getParent()));
            //判断父目录路径是否存在，即test.txt前的I:\a\b\
            try {
                out.getParentFile().mkdirs();
                //不存在则创建父目录
                out.createNewFile();
            } catch (IOException e) {
                log.warn("IOException",e);
            }
        }
    }
    public static void createDirectory(String path){
        File out = new File(path);
        if (!out.exists()) {
                out.mkdirs();
        }
    }
    public static boolean checkLegal(String path){

        String regex = "[a-zA-Z]:(?:[/\\\\][^/\\\\:*?\"<>|]{1,255})+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        return matcher.matches();

    }

    public static boolean checkExist(String path){

        boolean exist = false;
        if (checkLegal(path)){
            File needCheck = new File(path);
            if (needCheck.exists()){
                exist = true;
            }
        }
        return exist;

    }
}
