import request from './request'

export function getUserList() {
  return request.get('/users')
}
