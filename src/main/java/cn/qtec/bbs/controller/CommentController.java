package cn.qtec.bbs.controller;

import cn.qtec.bbs.entity.CommentEntity;
import cn.qtec.bbs.entity.model.UserRecord;
import cn.qtec.bbs.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by wuzh on 2019/1/8.
 */
public class CommentController extends Controller {
    private static UserService userService = new UserService();

    //提交回复后的操作
    public void save(){
        JSONObject object = new JSONObject();
        UserRecord mine = getSessionAttr("mine");
        if (null==mine){
            object.put("retcode",1);
            object.put("retinfo","请登录后再回复帖子！");
        }else {
            String bean = getPara("bean");
            CommentEntity comment = JSON.parseObject(bean, CommentEntity.class);
            userService.saveSysComment(comment,mine.get("userid"));
            userService.updateSysContentReplynumByContentid(comment.getContentid());
            object.put("retcode",0);
        }
        renderJson(object);
    }

    //点赞操作
    public void support(){
        int commentid = Integer.parseInt(getPara("commentid"));
        int supportnum = Integer.parseInt(getPara("ok"));
        userService.updateSupportnumByCommentid(commentid,supportnum);
        returnRetcode();
    }

    //将返回成功码为0的操作，统一抽离出来
    private void returnRetcode() {
        JSONObject object = new JSONObject();
        object.put("retcode",0);
        renderJson(object);
    }

    //采纳为最佳回复
    public void jieda_accept(){
        int commentid = Integer.parseInt(getPara("commentid"));
        userService.updateSysCommentCateByCommentid(commentid);
        userService.updateSysContentSolvedByCommentid(commentid);
        returnRetcode();
    }

    //编辑自己的回复,实现原数据的回显
    public void get_da(){
        int commentid = Integer.parseInt(getPara("commentid"));
        Record comment = userService.getSysCommentByCommentid(commentid);
        JSONObject object = new JSONObject();
        object.put("retcode",0);
        object.put("retinfo",comment.getStr("content"));
        renderJson(object);
    }
    //编辑完自己回复提交后需要更新回复
    public void update_da(){
        int commentid = Integer.parseInt(getPara("commentid"));
        String content = getPara("content");
        userService.updateSysCommentContentByCommentid(content,commentid);
        returnRetcode();
    }

    //删除自己的回复
    public void jieda_delete(){
        int commentid = Integer.parseInt(getPara("commentid"));
        userService.updateSysCommentStatusByCommentid(commentid);
        userService.decreaseSysContentReplynumByCommentid(commentid);
        returnRetcode();
    }

}
