package cn.qtec.bbs.kit;


import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * enjoy模板引擎使用共享方法类，
 * 更多关于enjoy的共享方法请查阅jfinal使用文档
 */
@Service
public class EJ {

    public String date(long time){
        return date(time, "yyyy-MM-dd HH:mm:ss");
    }
    public String date(long time, String pattern){
        return new SimpleDateFormat(pattern).format(time);
    }

}
