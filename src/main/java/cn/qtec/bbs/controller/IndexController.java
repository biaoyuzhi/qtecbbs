package cn.qtec.bbs.controller;


import cn.qtec.bbs.entity.model.Content;
import cn.qtec.bbs.entity.model.UserRecord;
import cn.qtec.bbs.service.UserService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;


/**
 * Created by lenovo on 2017/6/9.
 */
public class IndexController extends Controller {
    private static UserService userService = new UserService();

    public void index(){
        Boolean isMine = getSessionAttr("isMine",false);
        setAttr("isMine",isMine);
        if (isMine){
            setAttr("mine",getSessionAttr("mine"));
        }
        //综合显示卡数据
        int totalRow = userService.countSysUserRecordList();
        setAttr("totalRow", totalRow);

        String pindex = getPara("pindex");
        if (pindex==null){
            pindex = "1";
        }
        Page<UserRecord> userRecordList = userService.getSysUserRecordList(Integer.parseInt(pindex),15);
        setAttr("curr",pindex);
        setAttr("contents", userRecordList.getList());

        //置顶显示卡数据
        List<UserRecord> listOnTop = userService.getSysUserRecordListOnTop();
        setAttr("top",listOnTop);
        //右侧的总用户数
        int userCount = userService.countSysUserRecord();
        setAttr("userCount",userCount);
        //右侧的最新加入
        List<UserRecord> lastReg = userService.getSysUserRecordListByCreatetime();
        setAttr("lastReg",lastReg);
        //右侧的本周热帖
        List<Content> hotView = userService.getSysContentListByViewnum();
        setAttr("hotView",hotView);
        render("index.html");
    }

}
