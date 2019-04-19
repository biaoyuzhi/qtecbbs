package cn.qtec.bbs.controller;

import cn.qtec.bbs.entity.model.UserRecord;
import cn.qtec.bbs.service.UserService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;


/**
 * Created by wuzh on 2019/1/4.
 */
public class ProjectController extends Controller {
    private static UserService userService = new UserService();

    public void index(){
        Record content = userService.getSysContent(1);
        setAttr("bean",content);
        UserRecord mine = getSessionAttr("mine");
        if (null!=mine){
            setAttr("isMine",true);
            setAttr("mine",mine);
        }
        renderTemplate("index.html");
    }

}
