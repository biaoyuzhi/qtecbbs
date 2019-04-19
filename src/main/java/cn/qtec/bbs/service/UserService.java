package cn.qtec.bbs.service;


import cn.qtec.bbs.entity.CommentEntity;
import cn.qtec.bbs.entity.ContentEntity;
import cn.qtec.bbs.entity.UserRecordEntity;
import cn.qtec.bbs.entity.model.Comment;
import cn.qtec.bbs.entity.model.Content;
import cn.qtec.bbs.entity.model.UserRecord;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class UserService {

    //查询置顶的数据
    public List<UserRecord> getSysUserRecordListOnTop() {
        return UserRecord.dao.find("SELECT DISTINCT u.userid,u.nickname,u.avatar,c.createtime,c.replynum,c.title,c.contentid,c.top,c.type,c.viewnum,c.status " +
                "FROM sys_userrecord AS u " +
                "INNER JOIN sys_content AS c " +
                "ON u.userid = c.userid AND c.status!=-10 AND c.top!=10 where u.status!=-10");
    }
    //查询置顶的数据,按类型(求助/分享/动态)
    public List<UserRecord> getSysUserRecordListOnTopByType(int type) {
        return UserRecord.dao.find("SELECT DISTINCT u.userid,u.nickname,u.avatar,c.createtime,c.replynum,c.title,c.contentid,c.top,c.type,c.viewnum,c.status " +
                "FROM sys_userrecord AS u " +
                "INNER JOIN sys_content AS c " +
                "ON u.userid = c.userid AND c.status!=-10 AND c.top!=10 AND c.type=? where u.status!=-10",type);
    }

    public int countByEmail(String email) {
        return Db.queryInt("select count(1) from sys_userrecord where username=? and status!=-10",email);
    }
    //统计帖子总数
    public int countSysUserRecordList() {
        return Db.queryInt("select count(1) from sys_userrecord AS u INNER JOIN sys_content AS c ON u.userid = c.userid AND c.status!=-10 where u.status!=-10");
    }
    //统计用户总数
    public int countSysUserRecord() {
        return Db.queryInt("select count(1) from sys_userrecord where status!=-10");
    }

    //查询不同类型：求助/分享/动态的数据
    public List<UserRecord> getSysUserRecordListByType(int type) {
        return UserRecord.dao.find("SELECT DISTINCT u.userid,u.nickname,u.avatar,c.createtime,c.replynum,c.title,c.contentid,c.top,c.type,c.viewnum,c.status " +
                "FROM sys_userrecord AS u " +
                        "INNER JOIN sys_content AS c " +
                        "ON u.userid = c.userid and c.type=? and c.status!=-10 where u.status!=-10",type);
    }

    //查询综合中的数据
    public Page<UserRecord> getSysUserRecordList(int pageNum,int pageSize) {
        return UserRecord.dao.paginate(pageNum,pageSize,"SELECT DISTINCT u.userid,u.nickname,u.avatar,c.createtime,c.replynum,c.title,c.contentid,c.top,c.type,c.viewnum,c.status " ,
                "FROM sys_userrecord AS u " +
                "INNER JOIN sys_content AS c " +
                "ON u.userid = c.userid and c.status!=-10 where u.status!=-10");
    }

    public List<UserRecord> getSysUserRecordListBySolved(String solved) {
        return UserRecord.dao.find("SELECT DISTINCT u.userid,u.nickname,u.avatar,c.createtime,c.replynum,c.title,c.contentid,c.top,c.type,c.viewnum,c.status " +
                "FROM sys_userrecord AS u " +
                "INNER JOIN sys_content AS c " +
                "ON u.userid = c.userid AND c.solved =  ? and c.status!=-10 where u.status!=-10",solved);
    }

    public List<UserRecord> getSysUserRecordListByWonderful() {
        return UserRecord.dao.find("SELECT DISTINCT u.userid,u.nickname,u.avatar,c.createtime,c.replynum,c.title,c.contentid,c.top,c.type,c.viewnum,c.status " +
                "FROM sys_userrecord AS u " +
                "INNER JOIN sys_content AS c " +
                "ON u.userid = c.userid AND c.wonderful =  ? and c.status!=-10 where u.status!=-10",20);
    }

    public Record getSysUserRecordByUserid(int userid) {
        return Db.findById("sys_userrecord", "userid", userid);
    }

    public UserRecord getSysUserRecordByUsernameAndPassword(String username) {
        return UserRecord.dao.findFirst("select userid,nickname,avatar,password from sys_userrecord where username=? and status!=-10",username);
    }

    public UserRecord getPasswordByUserid(int userid) {
        return UserRecord.dao.findFirst("select password from sys_userrecord where userid=? and status!=-10",userid);
    }

    public String saveSysUserRecord(UserRecordEntity entity) {
        UserRecord.dao.set("username", entity.getEmail()).set("nickname", entity.getNickname()).set("email", entity.getEmail())
                .set("createtime", System.currentTimeMillis()).set("password", "linshimima@_@").save();
        return getSysUserRecordByUsernameAndPassword(entity.getEmail()).getStr("userid");
    }

    public Record getSysContent(int contentid) {
        return Db.findById("sys_content", "contentid", contentid);
    }

    public List<Content> getSysContentListByUserid(int userid) {
        return Content.dao.find("select * from sys_content where userid=? and status!=-10",userid);
    }

    public List<Content> getSysContentListJoinSysActlogByUserid(int userid) {
        return Content.dao.find("select c.contentid,c.title,c.createtime " +
                "from sys_content as c " +
                "inner join sys_actlog as a " +
                "on a.tid=c.contentid and a.userid=? and a.cate=20 and a.status!=-10 " +
                "where c.status!=-10",userid);
    }

    public boolean saveSysContent(ContentEntity content, int userid) {
        return Content.dao.set("userid",userid).set("title",content.getTitle()).set("content",content.getContent()).set("createtime",System.currentTimeMillis())
                .set("type",content.getType()).set("status",content.getStatus()).save();
    }

    //更新自己的老帖子
    public int updateSysContentByContentid(ContentEntity content) {
        return Db.update("update sys_content set title=?,content=?,type=?,status=? where contentid=?",
                content.getTitle(),content.getContent(),content.getType(),content.getStatus(),content.getContentid());
    }
    //更新我的资料
    public int updateSysUserrecordByUserid(int userid, UserRecordEntity entity) {
        return Db.update("update sys_userrecord set email=?,nickname=?,sex=?,city=?,sign=? where userid=?",
                entity.getEmail(),entity.getNickname(),entity.getSex(),entity.getCity(),entity.getSign(),userid);
    }
    //更新用户密码
    public int updatePasswordByUserid(int userid, String pass) {
        return Db.update("update sys_userrecord set password=? where userid=?",pass,userid);
    }
    //将supportnum自增1
    public int updateSupportnumByCommentid(int commentid, int supportnum) {
        return Db.update("update sys_comment set supportnum=supportnum+? WHERE commentid=?",supportnum,commentid);
    }
    //将该贴子从未结改为已结
    public int updateSysContentSolvedByCommentid(int commentid) {
        return Db.update("update sys_content set solved=20 WHERE contentid=(select contentid from sys_comment where commentid=?)",commentid);
    }
    //更新自己的回帖的内容
    public int updateSysCommentContentByCommentid(String content, int commentid) {
        return Db.update("update sys_comment set content=? WHERE commentid=?",content,commentid);
    }
    //将自己的回帖状态改为删除
    public int updateSysCommentStatusByCommentid(int commentid) {
        return Db.update("update sys_comment set status=-10 WHERE commentid=?",commentid);
    }
    //将该回帖标记为最佳答案，即cate设置为10
    public int updateSysCommentCateByCommentid(int commentid) {
        return Db.update("update sys_comment set cate=10 WHERE commentid=?",commentid);
    }
    //将该帖子的浏览量增一
    public int updateSysContentViewnumByContentid(int contentid) {
        return Db.update("update sys_content set viewnum=viewnum+1 WHERE contentid=?",contentid);
    }
    //将该帖子的回复量增一
    public int updateSysContentReplynumByContentid(int contentid) {
        return Db.update("update sys_content set replynum=replynum+1 WHERE contentid=?",contentid);
    }
    //将该帖子的回复量减少一，参数为commentid
    public int decreaseSysContentReplynumByCommentid(int commentid) {
        return Db.update("update sys_content set replynum=replynum-1 WHERE contentid=(select contentid from sys_comment where commentid=?)",commentid);
    }

    public List<Comment> getSysCommentByContentid(int contentid) {
        return Comment.dao.find("SELECT c.commentid,c.userid,c.content,c.supportnum,c.createtime,c.cate,u.avatar,u.nickname " +
                "FROM sys_comment as c " +
                "INNER JOIN sys_userrecord as u " +
                "ON c.userid=u.userid and u.status!=-10 " +
                "where c.status!=-10 and c.contentid=?",contentid);
    }

    public boolean saveSysComment(CommentEntity comment, int userid) {
        return Comment.dao.set("userid",userid).set("contentid",comment.getContentid()).set("pid",comment.getPid())
                .set("createtime",System.currentTimeMillis()).set("content",comment.getContent()).save();
    }

    public List<Comment> getSysCommentJoinContentByUserid(int userid) {
        return Comment.dao.find("SELECT t.createtime,t.content,t.contentid,t.cate,c.title " +
                "FROM sys_comment AS t " +
                "INNER JOIN sys_content AS c " +
                "ON c.contentid=t.contentid AND c.status!=-10 " +
                "WHERE t.userid=? and t.status!=-10",userid);
    }

    public Record getSysCommentByCommentid(int commentid) {
        return Db.findById("sys_comment", "commentid", commentid);
    }

    //获得最新加入的4个用户
    public List<UserRecord> getSysUserRecordListByCreatetime() {
        return UserRecord.dao.find("select userid,avatar,nickname,createtime from sys_userrecord where status!=-10 order by createtime desc limit 0,4");
    }
    //获得本周热帖的10个帖子
    public List<Content> getSysContentListByViewnum() {
        return Content.dao.find("select contentid,title,viewnum from sys_content where status!=-10 order by viewnum desc limit 0,10");
    }

}
