package com.drj.reptile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.drj.util.ExcelUtil;
import com.drj.util.HttpClient4Utils;

/**
 * 
 * @ClassName: ReptileDemo
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: drj
 * @date: 2018年10月24日 下午5:53:00
 * 
 * @Copyright: 2018
 *
 */
public class ReptileDemo {
    // https://www.tianyancha.com/company/2724280
    // public static String Url = "https://www.tianyancha.com/";
    // public static String uri = "https://www.tianyancha.com/search/p6";
    // public static StringBuilder URL = new StringBuilder(uri);//
    // http://www.itcast.cn/
    public static String reps = null;

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 解析html 页面
     */
    public static Map<String, Object> selectInfo(String URL) {
        Map<String, Object> map = new HashMap<String, Object>();
        reps = HttpClient4Utils.sendPost(URL.toString(), null);
        if (reps.contains("414 Request-URI Too Large")) {
            System.err.println("需要重新验证");
            return null;
        }
        Document doc = Jsoup.parse(reps);
        String f4 = doc.select("div").attr("class", "imgContent").html();
        if (f4.contains("404-blue.jpg")) {
            System.out.println(404);
            return null;
        }
        Elements company = doc.select("meta").attr("name", "tyc-wx-name");
        String companyName = company.eq(18).attr("content");// 公司名称
        map.put("companyName", companyName);
        String phone = doc.select("span").eq(10).text();// 电话
        if (!isInteger(phone.replace("-", ""))) {
            if (!phone.contains("-") && !phone.contains("暂无信息") && phone.length() != 11) {
                phone = doc.select("span").eq(9).text();// 电话
                if (!phone.contains("暂无信息")) {
                    phone = doc.select("span").eq(12).text();// 电话
                    if (phone.length() == 2) {
                        phone = doc.select("span").eq(11).text();// 电话
                    }
                }
            }
        }
        map.put("phone", phone);
        Elements elements = doc.select("table").eq(1);
        for (int i = 1; i < 8; i = i + 6) {
            Elements trs = elements.select("tr").eq(i);
            Elements tds = trs.select("td");
            for (int j = 0; j < tds.size(); j++) {
                if (j == 1) {
                    map.put(String.valueOf(i) + String.valueOf(j),
                            i == 7 ? tds.get(j).text().replace("附近公司", "") : tds.get(j).text());
                }
            }
        }
        System.out.println(map);
        return map;
    }

    public static void main(String[] args) {
        List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
        Map<String, Object> params = selectInfo("https://www.tianyancha.com/company/5497731");
        info.add(params);
        exportExcle(info);
        /*
         * for (int i = 1; i < 100; i++) {
         * 
         * try { Thread.sleep(500); uri = uri + i; selectInfo(uri); uri = Url; } catch
         * (InterruptedException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); System.err.println(e.getMessage()); } }
         */

    }

    /**
     * 导出到excel
     * 
     * @param userApplyMap
     */
    public static void exportExcle(List<Map<String, Object>> userApplyMap) {
        ServletOutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            JSONArray userApplyInfos = new JSONArray();
            if (!userApplyMap.isEmpty()) {// 数据集
                for (Map<String, Object> map : userApplyMap) {
                    com.alibaba.fastjson.JSONObject userApply = new com.alibaba.fastjson.JSONObject();
                    if (map != null && map.size() > 0) {
                        userApply.put("companyName", map.get("companyName"));
                        userApply.put("phone", map.get("phone"));
                        userApply.put("11", map.get("11"));
                        userApply.put("71", map.get("71"));
                        userApplyInfos.add(userApply);
                    }

                }
                /**
                 * 重新生成需要的头部信息
                 */

                Map<String, String> headMap = new LinkedHashMap<String, String>();// 存放表头部信息
                headMap.put("companyName", "公司名称");
                headMap.put("phone", "联系方式");
                headMap.put("11", "纳税人识别号 ");
                headMap.put("71", "注册地址");
                /**
                 * 重新生成新的excel
                 */
                /*
                 * OutputStream outXlsx = new FileOutputStream(ExcelUtil.DIR_PATH);
                 * ExcelUtil.exportToExcel(headMap, userApplyInfos, null,
                 * ExcelUtil.DEFAULT_COLOUMN_WIDTH, outXlsx); outXlsx.close();
                 */

                ExcelUtil.DealExcel(userApplyInfos);// 追加excel
            } else {
                System.out.println("ERRO");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            System.out.println("excel end");
        }
    }

    /**
     * 获取每个公司的href
     * 
     * @param URL
     * @return
     */
    public static Elements getHrefValue(String URL, Map<String, Object> params) {
        System.out.println(URL);
        reps = HttpClient4Utils.sendPost(URL.toString(), params);
        // System.out.println(reps);
        if (reps.contains("抱歉，没有找到相关结果")) {
            return null;
        }
        if (reps.contains("414 Request-URI Too Large")) {
            System.err.println("需要重新验证");
            return null;
        }
        Document doc = Jsoup.parse(reps);
        // System.out.println(doc);
        Elements d = doc.select("div[class='result-list']").select("div[class='header']").select("a");
        return d;
    }

    /**
     * 是否出现验证
     * 
     * @param response
     * @param context
     * @return
     */
    public static boolean checkRobotVerification(HttpResponse response, HttpClientContext context) {
        boolean result = false;

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        System.out.println(statusCode);
        if (statusCode != HttpStatus.SC_OK) {
            return true;
        }

        return result;
    }

    public  static boolean getCheckUrl(CloseableHttpResponse response, HttpClientContext context) {
        if (checkRobotVerification(response, context)) {
            List<URI> redirectLocations = context.getRedirectLocations();
            System.out.println("注意！出现机器人验证，请点击下面的链接，在验证完后输入 ok 继续运行。。。");
            System.out.println();
            System.out.println();
            System.out.print("完成验证后，请在此处输入OK：");
            while (!"ok".equalsIgnoreCase(new Scanner(System.in).nextLine())) {
                return true;
            }
            return false;
        }
        return true;
    }
}
