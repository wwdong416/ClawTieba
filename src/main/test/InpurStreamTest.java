import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author yz3702
 * @program Save
 * @date 2018/4/4
 */
public class InpurStreamTest {
    String urlstr ="https://imgsa.baidu.com/forum/pic/item/3b20a38b87d6277fcb34fa982f381f30e924fc0a.jpg";
    @Test
    public void getInputStream() throws IOException {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlstr);
            if (url != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                // 设置连接网络的超时时间
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setDoInput(true);
                // 设置本次http请求使用get方式请求
                httpURLConnection.setRequestMethod("GET");
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    inputStream = httpURLConnection.getInputStream();
                    System.out.println("inputstream size  " + inputStream.available() + "b");
                    if (inputStream.available() < 10000) {
//                        return null;
                    }
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("error" + e.getMessage());
            e.printStackTrace();
        }
//        return inputStream;
    }
}
