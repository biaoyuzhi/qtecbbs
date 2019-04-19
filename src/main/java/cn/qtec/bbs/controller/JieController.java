package cn.qtec.bbs.controller;

import cn.qtec.bbs.entity.ContentEntity;
import cn.qtec.bbs.entity.model.Comment;
import cn.qtec.bbs.entity.model.Content;
import cn.qtec.bbs.entity.model.UserRecord;
import cn.qtec.bbs.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by wuzh on 2019/1/3.
 */
public class JieController extends Controller {
    private static UserService userService = new UserService();

    public void index() {
        UserRecord mine = getSessionAttr("mine");
        if (null != mine) {
            setAttr("isMine", true);
            setAttr("mine", mine);
        }
        //右侧的本周热帖
        List<Content> hotView = userService.getSysContentListByViewnum();
        setAttr("hotView", hotView);
        render("index.html");
    }

    public void add() {
        UserRecord mine = getSessionAttr("mine");
        if (null != mine) {
            setAttr("isMine", true);
            setAttr("mine", mine);

            String contentid = getPara("contentid");
            if (null != contentid) {
                Record content = userService.getSysContent(Integer.parseInt(contentid));
                setAttr("editContent", contentid);
                setAttr("bean", content);
            }
            render("add.html");
        } else {
            render("/user/login.html");
        }
    }

    public void detail() {
        int contentid = Integer.parseInt(getPara("contentid"));
        userService.updateSysContentViewnumByContentid(contentid);
        //查询该帖子信息
        Record content = userService.getSysContent(contentid);
        setAttr("bean", content);
        //查询该帖子下的所有评论信息
        List<Comment> commentList = userService.getSysCommentByContentid(contentid);
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

        UserRecord mine = getSessionAttr("mine");
        if (null != mine) {
            setAttr("isMine", true);
            setAttr("mine", mine);
        }
        //右侧的本周热帖
        List<Content> hotView = userService.getSysContentListByViewnum();
        setAttr("hotView", hotView);
        render("detail.html");
    }

    public void save() {
        String bean = getPara("bean");
        ContentEntity content = JSON.parseObject(bean, ContentEntity.class);
        UserRecord mine = getSessionAttr("mine");
        JSONObject object = new JSONObject();
        if (0 != content.getContentid()) {//更新、编辑此贴
            userService.updateSysContentByContentid(content);
            object.put("retcode", 0);
        } else {//新增、发布新帖
            userService.saveSysContent(content, mine.get("userid"));
            object.put("retcode", 0);
        }
        renderJson(object);
    }

}
