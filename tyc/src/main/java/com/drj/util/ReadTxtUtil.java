package com.drj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.drj.reptile.ReptileDemo;

public class ReadTxtUtil {
    public static int count = 1;

    public static void main(String[] args) {
        ReadTxt();
    }

    public static void ReadTxt() {
        List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
        try {
            File file = new File("D://test.txt");
            if (file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                Random rand = new Random();
                while ((lineTxt = br.readLine()) != "" && lineTxt != null) {
                   // System.out.println("正在查询第:" + count + "地址" + lineTxt);
                    /*if (lineTxt.startsWith("https")) {
                        info.add(ReptileDemo.selectInfo(lineTxt)); Thread.sleep(WriteTxtUtil.time[rand.nextInt(4)]);
                    }
                    if(count % 100 == 0) {
                        ReptileDemo.exportExcle(info);
                        info.clear();
                        System.out.println("30秒等下一轮…………");
                        Thread.sleep(1000*30);
                    }*/
                	System.out.println(count);
                    count++;
                }
                System.err.println("start insert" + info.size());
               // ReptileDemo.exportExcle(info);
                //Thread.sleep(WriteTxtUtil.time[rand.nextInt(4)]);
                System.err.println("start end");
                br.close();
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!"+e);
        }
    }
}
