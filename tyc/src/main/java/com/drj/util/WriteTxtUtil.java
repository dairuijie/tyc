package com.drj.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jsoup.select.Elements;

import com.drj.reptile.ReptileDemo;

public class WriteTxtUtil {
    private static String url = "https://www.tianyancha.com/search/os1-ot";
    public static int[] time = { 8000, 11000, 10000, 9000 };

    public static void main(String[] args) {
        try {
            WriteToTxt(url);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 通过url 获取页面中公司请求地址保存到txt
     * 
     * @param url
     * @throws InterruptedException
     */
    public static void WriteToTxt(String url) throws InterruptedException {
        File file = new File("D://test.txt");
        FileWriter fileWrite = null;
        PrintWriter print = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWrite = new FileWriter(file, true);
            print = new PrintWriter(fileWrite);
            Map<String, Object> params = new HashMap<String, Object>();
            int count = 1;
            for (int c = 110101; c < 110119; c++) {
            	if(c == 3 || c == 4 || c == 10) {
            		continue;
            	}
                for (int code = 1; code < 11; code++) {
                    params.put("base", "bj");
                    params.put("areaCode", c + "");
                    System.out.println(params);
                    for (int j = 1; j < 6; j++) {
                        Elements eles = ReptileDemo.getHrefValue(url + code + "/p" + j, params);
                        if (eles != null) {
                            for (int i = 0; i < eles.size(); i++) {
                                System.out.println(eles.get(i).attr("href"));
                                print.println(eles.get(i).attr("href"));
                                count++;
                            }
                            print.flush();
                        }
                        System.out.println("等待30秒左右…………");
                        Thread.sleep(30000);
                    }
                    // Thread.sleep(1000*20);
                    System.err.println(count);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fileWrite.close();
                print.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
