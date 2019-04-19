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
public class ContentEntity implements Serializable {

    private int contentid;
    private int userid;
    private String title;
    private String digest;
    private String content;
    private long createtime;
    private short cate;
    private short type;
    private int replynum;
    private int viewnum;
    private short wonderful;
    private short top;
    private short solved;
    private short status;
    private String typename;

}
