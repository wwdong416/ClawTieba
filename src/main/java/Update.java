import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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

public class Update {
    private static List<String> imgpaths = new CopyOnWriteArrayList<String>();
    private static int index = 0;

    public static void main(String[] args) throws Exception {
        List<String> configs = getFileData("E://" + "jsoup/config.txt");
        if (configs.isEmpty()) {
            File file = new File("E://" + "jsoup/config.txt");
            file.mkdirs();
            System.out.println("配置信息有误,请到E://jsoup/config.txt下修改配置。");
            return;
        }
        String[] urls = configs.get(0).split("/", 2);

        String url = urls[1];
        // System.out.println("---------------------"+ url);
        for (int q = 1; q < configs.size(); q++) {
            url = urls[1];
            //获取第二行开始的字符串（需求标签）
            System.out.println(" " + configs.get(q));
            Document configDoc = null;
            try {
                //获取网址对应的html文件
                configDoc = Jsoup
                        .connect("http://" + urls[0] + "/" + url)
                        .timeout(50000).userAgent("Mozilla").cookie("auth", "token").get();
                //获取class_list(图片分类，配置中的标签属于其中)
                Elements listEles = configDoc.getElementsByClass("class_list");
                Element nameEle = listEles.select("a[title=" + configs.get(q) + "]").get(0);
                url = nameEle.attr("href");//标签对应的网址
                System.out.println("url:" + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (url != null) {
                try {
                    System.out.println("http://" + urls[0] + "/" + url);
                    Document mainDoc = Jsoup
                            .connect("http://" + urls[0] + "/" + url)
                            .timeout(50000).userAgent("Mozilla").cookie("auth", "token").get();
                    Elements mainEles = mainDoc.getElementsByClass("ba_info");
                    /**
                     * 获取贴吧中的入口"ba_info"
                     * */
                    for (Element mainEle : mainEles) {
                        // mainUrl =//tieba.baidu.com/f?kw=%E6%9D%A8%E6%B4%8B&ie=utf-8&tab=album
                        String mainUrl = "//" + urls[0] + mainEle.select("a").attr("href") + "&ie=utf-8&tab=album";
//                        String mainUrl = "//tieba.baidu.com/f?kw=%E4%BA%95%E6%9F%8F%E7%84%B6&ie=utf-8&tab=album";
                        /**
                         * 依次进入个人贴吧
                         * */
                        System.out.println("This URL is " + mainUrl);
                        String baName = mainEle.getElementsByClass("ba_name").get(0).text();
//                        String  baName = "井柏然吧";
//                        if ("井柏然吧".equals(baName)){
//                            continue;
//                        }
                        //吧名
                        String savePath = "E://" + "jsoup/" + configs.get(q) + "/" + baName;
                        /**
                         * 如果文件夹存在，已提取过该吧图片，跳过
                         */

                        if (new File(savePath).exists()) {
                            continue;
                        }

                        //   System.out.println("111111111111111111111111111111"+mainEle.select("a").attr("href"));
                        String nameUrl = mainEle.select("a").attr("href");
//                        String nameUrl = "/f?kw=%E4%BA%95%E6%9F%8F%E7%84%B6";
                        String name = nameUrl.substring(3);
                        // System.out.println("+++++++++++++++"+name);
                        while (mainUrl != null) {

                            Document document = Jsoup
                                    .connect("http:" + mainUrl)
                                    .timeout(50000)
                                    .userAgent("Mozilla")
                                    .cookie("auth", "token")
                                    .get();
//                System.out.println(""+document.toString());
                            //   System.out.println("----------+++++++++------------"+document);

                            //  if (ispic != null){

                            Elements elementurls = document.getElementsByClass("grbm_ele_title");
                     
                                System.out.println("//////////////////////////////////////");

                                Elements elementurl = elementurls.select("a");
                                // System.out.println("----------------------"+elementurls);
                                //Element开始
                                for (Element e : elementurl) {
                                    System.out.println("start");
                                    String urlnode = "http://" + urls[0] + e.attr("href");
                                    String urltitle = e.attr("title");
                                    System.out.println("urlnode:" + urlnode);
                                    String saveName = urltitle;//相册名
                                    saveName = saveName.replaceAll("\\\\", "_");
                                    //  System.out.println("222222222222222222222222222"+ saveName);
                                    String str = e.attr("href");
                                    str = str.trim();
                                    /**
                                     * 拆分地址栏获取该相册的ID
                                     * */
                                    String str2 = "";
                                    if (str != null && !"".equals(str)) {
                                        for (int i = 0; i < str.length(); i++) {
                                            if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                                                str2 += str.charAt(i);
                                            }
                                        }

                                    }

                                    /**
                                     * 获取贴吧相册的json数据地址
                                     * picUrl:json地址
                                     * 返回图片集合jsonString picUrl = "http://tieba.baidu.com/photo/g/bw/picture/list?kw=黄子韬&alt=jview&rn=200&tid=2211977819";
                                     * 单个图片地址所在范例http://tieba.baidu.com/photo/p?kw=%E6%9D%A8%E6%B4%8B&tid=4682861821&pic_id=cfacab773912b31bdb6821508e18367adab4e161
                                     * https://imgsa.baidu.com/forum/pic/item/3b20a38b87d6277fcb34fa982f381f30e924fc0a.jpg
                                     * */
                                    String picUrl = "http://tieba.baidu.com/photo/g/bw/picture/list?" + name + "&alt=jview&rn=200&tid=" + str2 + "&ps=1&pe=1024";
                                    Document doc = Jsoup
                                            .connect(picUrl).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                                            .timeout(50000).cookie("auth", "token").get();
                                    String newjson = jsonString(doc.text());
                                    if (isJSONValid2(newjson)) {
                                        JSONObject jsonObject = JSONObject.fromObject(newjson);
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        JSONArray jsonArray = data.getJSONArray("pic_list");
                                        JSONObject row = null;
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            row = jsonArray.getJSONObject(i);
                                            // System.out.println("descr:"+row.get("descr"));
                                            //获取pic_id用于拼接高清图片地址
                                            String picId = row.getString("pic_id");

                                            String purls = row.get("purl").toString();
                                            String suffix = purls.substring(purls.lastIndexOf("."));
                                            //高清图片地址（可能是图片服务器）
                                            String picUrls = "https://imgsa.baidu.com/forum/pic/item/" + picId + suffix;
                                            System.out.println(picUrls);
//                                            System.out.println("imgUrl:" + row.get("purl"));
//                                            imgpaths.add(row.get("purl").toString());
                                            imgpaths.add(picUrls);
                                            saveImage(imgpaths, savePath + "/" + saveName, saveName + row.get("index").toString());
                                            imgpaths.clear();
                                            //downloadPicture(row.get("purl").toString(),p);
                                        }
                                    }
                                }
                                //Element结束
                                 mainUrl = isFinsh(document, "div#frs_list_pager", "a.next pagination-item");//贴吧相册内容页数
                            
                        }
                    }
                    url = isFinsh(mainDoc, "div[class=pagination]", "a[class=next]");
                } catch (IOException e) {
                    System.out.println("error");
                }
            }
        }
    }
    /**
     * 解析json数据中是否含有英文的引号，并将其改变成中文的引号
     */
    private static String jsonString(String s) {
        char[] temp = s.toCharArray();
        int n = temp.length;
        for (int i = 0; i < n; i++) {
            if (temp[i] == ':' && temp[i + 1] == '"') {
                for (int j = i + 2; j < n; j++) {
                    if (temp[j] == '"') {
                        if (temp[j + 1] != ',' && temp[j + 1] != '}') {
                            temp[j] = '”';
                        } else if (temp[j + 1] == ',' && temp[j + 2] == ' ') {
                            temp[j] = '”';
                        } else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
                            break;
                        }
                    }
                }
            }

        }
        return new String(temp);
    }

    /**
     * isJSONValid2判断返回的json数据是否合法
     * 排除一些不合法的json数据
     * */
    public final static boolean isJSONValid2(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static List<String> getFileData(String s) {
        List<String> configs = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(s), "gb2312"));
            String str = null;
            while ((str = reader.readLine()) != null) {
                configs.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configs;
    }

    private static String isFinsh(Document document, String s, String s1) throws IOException {
        Elements last_elements = document.select(s);
        System.out.println("" + s + s1);
        Elements last_element = last_elements.select(s1);
        if (!last_element.isEmpty()) {
            return last_element.get(0).attr("href");
        } else {
            return null;
        }

    }

    private static void saveImage(final List<String> paths, final String savePath, final String saveName) {
        if (paths.size() < 20) {
            for (int i = 0; i < paths.size(); i++) {
                save(paths.get(i), savePath, saveName);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                Thread thread = new Thread(new Runnable() {

                    public void run() {
                        int size = paths.size() / 20;
                        List<String> list = null;
                        if (index < 19) {
                            if ((index + 1) * size > paths.size()) {
                                list = paths.subList(index * size, paths.size() - 1);
                            } else {
                                list = paths.subList(index * size, (index + 1) * size - 1);
                            }
                        } else {
                            if (index * size < paths.size() - 1) {
                                list = paths.subList(index * size, paths.size() - 1);
                            }
                        }
                        if (list != null) {
                            for (int k = 0; k < list.size(); k++) {
                                save(list.get(k), savePath, saveName);
                            }
                        }
                    }
                });
                index++;
                thread.start();
            }
        }
    }

    private static void save(String path, String savePath, String name) {
        File file = new File(savePath);
        /**
         * 解析名称中的/符号，避免与路径冲突
         * */
        String namex = name.replaceAll("\\/", "_");
        System.out.println("Saving this  " + namex + ".jpg");
        File savedfile = new File(savePath + File.separator + namex + ".jpg");
        if (!file.exists()) {
            file.mkdirs();
        }
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = getInputStream(path);
            if (inputStream != null) {
                byte[] data = new byte[1024];
                int len = 0;
                fileOutputStream = new FileOutputStream(savedfile);
                while ((len = inputStream.read(data)) != -1) {
                    fileOutputStream.write(data, 0, len);
                }
            } else {
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static InputStream getInputStream(String urlstr) throws IOException {
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
                    if (inputStream.available() < 100) {
                        return null;
                    }
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("error" + e.getMessage());
            e.printStackTrace();
        }
        return inputStream;
    }

}
