/**
 * @project   task-management-system
 * @module    backend  
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
package com.icinfo.task.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String role;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
