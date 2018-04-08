import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Url {


    public static void main(String[] args) throws Exception {

        Document doc = Jsoup.connect("http://tieba.baidu.com/f?kw=%E4%BA%95%E6%9F%8F%E7%84%B6&tab=album&cat_id=all&pn=2")
                .data("query", "Java") //请求参数
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(50000)
                .get();

        Elements dd = doc.getElementsByClass("grbm_ele_title");


        if (dd.isEmpty()) {
            System.out.println(dd + "00000000000");
        } else {

            System.out.println(dd+"----------");
        }

    }


    }




