import request from './request'

export function getNewsList(params?: any) {
  return request.get('/news', { params })
}
