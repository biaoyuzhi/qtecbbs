package cn.qtec.bbs.controller;

import cn.qtec.bbs.entity.model.Content;
import cn.qtec.bbs.entity.model.UserRecord;
import cn.qtec.bbs.service.UserService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;


/**
 * Created by wuzh on 2019/1/3.
 */
public class ColumnController extends Controller {

    private static UserService userService = new UserService();

    public void index() {
        getSessionMine();
        String solved = getPara("solved");
        String wonderful = getPara("wonderful");
        if (null == solved && null == wonderful) {
            int totalRow = userService.countSysUserRecordList();
            setAttr("totalRow", totalRow);

            String pindex = getPara("pindex");
            if (pindex==null){
                pindex = "1";
            }
            Page<UserRecord> userRecordList = userService.getSysUserRecordList(Integer.parseInt(pindex),15);
            setAttr("contents", userRecordList.getList());
            setAttr("curr",pindex);
        } else if (null != solved) {
            List<UserRecord> listBySolved = userService.getSysUserRecordListBySolved(solved);
            setAttr("contents", listBySolved);
        } else {
            List<UserRecord> listBySolved = userService.getSysUserRecordListByWonderful();
            setAttr("contents", listBySolved);
        }
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
        renderTemplate("/index.html");
    }

    public void qz() {
        getSessionMine();
        List<UserRecord> userRecordList = userService.getSysUserRecordListByType(10);
        setAttr("contents", userRecordList);
        setAttr("totalRow", userRecordList.size());
        //置顶显示卡数据
        getTopListByType(10);
        renderTemplate("/index.html");
    }

    //获得session中的信息统一抽离出
    private void getSessionMine() {
        UserRecord mine = getSessionAttr("mine");
        if (null != mine) {
            setAttr("isMine", true);
            setAttr("mine", mine);
        }
    }
    //抽取出置顶选项卡的数据获取
    private void getTopListByType(int type) {
        //置顶显示卡数据
        List<UserRecord> listOnTop = userService.getSysUserRecordListOnTopByType(type);
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
    }

    public void fx() {
        getSessionMine();
        List<UserRecord> userRecordList = userService.getSysUserRecordListByType(20);
        setAttr("contents", userRecordList);
        setAttr("totalRow", userRecordList.size());
        //置顶显示卡数据
        getTopListByType(20);
        renderTemplate("/index.html");
    }

    public void dt() {
        getSessionMine();
        List<UserRecord> userRecordList = userService.getSysUserRecordListByType(50);
        setAttr("contents", userRecordList);
        setAttr("totalRow", userRecordList.size());
        //置顶显示卡数据
        getTopListByType(50);
        renderTemplate("/index.html");
    }

}
