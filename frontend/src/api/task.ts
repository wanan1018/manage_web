/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
import request from './request'

export function getTaskList(params?: any) {
  return request.get('/tasks', { params })
}

export function createTask(data: any) {
  return request.post('/tasks', data)
}

export function updateTask(id: number, data: any) {
  return request.put(`/tasks/${id}`, data)
}

export function deleteTask(id: number) {
  return request.delete(`/tasks/${id}`)
}
