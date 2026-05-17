/**
 * @project   task-management-system
 * @module    backend  
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
package com.icinfo.task.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long userId;
}
