package cn.qtec.bbs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by wuzh on 2019/1/7.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecordEntity implements Serializable {

    private int userid;
    private String username;
    private String password;
    private short sex;
    private String phone;
    private String nickname;
    private String avatar;
    private String realname;
    private String email;
    private int roleid;
    private String site;
    private String git;
    private long createtime;
    private String sign;
    private String city;
    private short status;
}
