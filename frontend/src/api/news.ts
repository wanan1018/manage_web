/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
import request from './request'

export function getNewsList(params?: any) {
  return request.get('/news', { params })
}
