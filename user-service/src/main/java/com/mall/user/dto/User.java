package com.mall.user.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

/**
 * @author hongfei
 */
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@TableName("user")
public class User extends Model<User>  {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    private String userId;
    private String userName;
    private String address;
    private String tel;
    private String other;
}
