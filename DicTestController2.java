package com.jd.itms.gw;

import com.jd.itms.framework.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @describe 加载缓存字典
 */
@RestController
@RequestMapping("/rest")
public class DicTestController2 {

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/getAreaDic2", method = RequestMethod.GET)
    @ResponseBody
    public void getAreaDic(HttpServletRequest request, HttpServletResponse response) {
        try {
//            cacheService.data2Cache("areaDic", "谷尚居\n高新四路\n万科魅力北区\n万科魅力南区\n当代国际花园\n佛祖岭\n");
            String content  = cacheService.getDataFromCache("areaDic", String.class);
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
