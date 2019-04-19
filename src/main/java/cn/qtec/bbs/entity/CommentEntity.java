package cn.qtec.bbs.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity implements Serializable {

    private int commentid;
    private int userid;
    private int pid;
    private short cate;
    private int contentid;
    private String content;
    private long createtime;
    private int supportnum;
    private short status;

}
