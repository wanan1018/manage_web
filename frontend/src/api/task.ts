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
