package com.jd.itms.gw;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @author pengchangcheng
 * @date 2018/7/16
 * @describe 加载项目内字典
 */
@RestController
@RequestMapping("/rest")
public class DicTestController {

    @RequestMapping(value = "/getAreaDic", method = RequestMethod.GET)
    @ResponseBody
    public void getAreaDic(HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = this.getClass().getClassLoader().getResource("/").getPath().replaceAll("WEB-INF/classes/", "")+ "dic/areaDic.dic";
            File file = new File(path);
            String content = "";
            if (file.exists()) {
                //读取文件
                FileInputStream fi = new FileInputStream(file);
                byte[] buffer = new byte[(int) file.length()];
                int offset = 0, numRead = 0;
                while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                    offset += numRead;
                }
                fi.close();
                content = new String(buffer, "UTF-8");
            }
            // 返回数据
            OutputStream out = response.getOutputStream();
            response.setHeader("Last-Modified", String.valueOf(content.length()));
            response.setHeader("ETag", String.valueOf(content.length()));
            response.setContentType("text/plain; charset=utf-8");
            out.write(content.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
