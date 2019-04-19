package cn.qtec.bbs.controller;

import cn.qtec.bbs.entity.UserRecordEntity;
import cn.qtec.bbs.entity.model.Comment;
import cn.qtec.bbs.entity.model.Content;
import cn.qtec.bbs.entity.model.UserRecord;
import cn.qtec.bbs.kit.CommonUtils;
import cn.qtec.bbs.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * Created by wuzh on 2019/1/3.
 */
public class UserController extends Controller {
    private static UserService userService = new UserService();

    public void login() {
        render("login.html");
    }

    public void loginlogin() {
        String bean = getPara("bean");
        if (null != bean) {
            UserRecordEntity entity = JSON.parseObject(bean, UserRecordEntity.class);
            UserRecord userRecord = userService.getSysUserRecordByUsernameAndPassword(entity.getUsername());
            JSONObject object = new JSONObject();
            if (null != userRecord
                    && (CommonUtils.passwordErrorVerify(entity.getPassword(), userRecord.getStr("password"), userRecord.getStr("userid"))))
            {
                object.put("retcode", 0);
                setSessionAttr("isMine", true);
                setSessionAttr("mine", userRecord);
            } else {
                object.put("retcode", 1);
                object.put("retinfo", "账号或密码错误!");
            }
            renderJson(object);
        }
    }

    public void logout() {
        removeSessionAttr("isMine");
        removeSessionAttr("mine");
        renderJson();
    }

    public void reg() {
        render("reg.html");
    }

    public void register() {
        String bean = getPara("bean");
        if (null != bean) {
            UserRecordEntity entity = JSON.parseObject(bean, UserRecordEntity.class);
            String email = entity.getEmail().trim();
            int count = userService.countByEmail(email);
            JSONObject object = new JSONObject();
            if (count > 0) {
                object.put("retcode", 1);
                object.put("retinfo", "该邮箱已经注册，请不要重复注册!");
            } else {
                object.put("retcode", 0);
                String userid = userService.saveSysUserRecord(entity);
                userService.updatePasswordByUserid(Integer.parseInt(userid),CommonUtils.passwordEncrypt(entity.getPassword(),userid));
                render("reg.html");
            }
            renderJson(object);
        }
    }

    public void activate() {
        render("activate.html");
    }

    public void forget() {
        render("forget.html");
    }

    public void home() {
        UserRecord mine = getSessionAttr("mine");
        if (null == mine) {
            render("login.html");
        } else {
            setAttr("isMine", true);
            setAttr("mine", mine);
            int userid;
            String para = getPara("userid");
            if (null != para) {
                userid = Integer.parseInt(para);
            } else {
                userid = mine.get("userid");
            }
            //展示用户信息
            Record userRecord = userService.getSysUserRecordByUserid(userid);
            setAttr("user", userRecord);
            //展示最近的帖子
            List<Content> contentList = userService.getSysContentListByUserid(userid);
            setAttr("contents", contentList);
            //展示最近的回答
            List<Comment> commentList = userService.getSysCommentJoinContentByUserid(userid);
            setAttr("comments", commentList);
            boolean isCaiNa = false;
            if (!CollectionUtils.isEmpty(commentList)){
                for (int i = 0; i < commentList.size(); i++) {
                    if (commentList.get(i).getInt("cate") > 1) {
                        isCaiNa = true;
                    }
                }
            }
            setAttr("isCaiNa", isCaiNa);

            render("home.html");
        }
    }

    public void index() {
        UserRecord mine = getSessionAttr("mine");
        if (null == mine) {
            render("login.html");
        } else {
            setAttr("isMine", true);
            setAttr("mine", mine);
            int userid = mine.get("userid");
            //查询我发的帖的数据
            List<Content> contentList = userService.getSysContentListByUserid(userid);
            JSONObject object1 = new JSONObject();
            object1.put("total", contentList.size());
            object1.put("rows", contentList);
            setAttr("contents", object1);

            //查询我收藏的帖的数据
            List<Content> collectList = userService.getSysContentListJoinSysActlogByUserid(userid);
            JSONObject object2 = new JSONObject();
            object2.put("total", collectList.size());
            object2.put("rows", collectList);
            setAttr("collects", object2);
            render("index.html");
        }
    }

    public void message() {
        render("message.html");
    }

    public void set() {
        UserRecord mine = getSessionAttr("mine");
        if (null == mine) {
            render("login.html");
        } else {
            setAttr("isMine", true);
            setAttr("mine", mine);
            int userid = mine.get("userid");
            Record userRecord = userService.getSysUserRecordByUserid(userid);
            setAttr("mine", userRecord);
            render("set.html");
        }
    }

    //更新我的资料
    public void update() {
        String bean = getPara("bean");
        UserRecordEntity entity = JSON.parseObject(bean, UserRecordEntity.class);
        int userid = getUseridByMine();
        String email = entity.getEmail().trim();
        JSONObject object = new JSONObject();
        Record record = userService.getSysUserRecordByUserid(userid);
        if (!email.equals(record.getStr("email"))){
            int countByEmail = userService.countByEmail(email);
            if (countByEmail > 0) {
                object.put("retcode", 1);
                object.put("retinfo", "该邮箱已经在使用，请不要重复!");
            }
        } else {
            int count = userService.updateSysUserrecordByUserid(userid, entity);
            if (count == 1) {
                object.put("retcode", 0);
            } else {
                object.put("retcode", 1);
                object.put("retinfo", "修改失败!");
            }
        }
        renderJson(object);
    }

    //抽取出从session的mine中获得该用户的userid方法
    private int getUseridByMine() {
        UserRecord mine = getSessionAttr("mine");
        return mine.get("userid");
    }

    //修改密码
    public void changepwd() {
        String nowpass = getPara("nowpass");
        String pass = getPara("pass");
        int userid = getUseridByMine();
        UserRecord userRecord = userService.getPasswordByUserid(userid);
        String password = null;
        if (null != userRecord) password = userRecord.get("password");
        JSONObject object = new JSONObject();
        if (CommonUtils.passwordEncrypt(nowpass, String.valueOf(userid)).equals(password)) {
            userService.updatePasswordByUserid(userid, CommonUtils.passwordEncrypt(pass, String.valueOf(userid)));
            object.put("retcode", 0);
        } else {
            object.put("retcode", 1);
            object.put("retinfo", "当前密码有误!");
        }
        renderJson(object);
    }

}
