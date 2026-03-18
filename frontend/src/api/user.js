import request from '@/utils/request'

export const loginApi = (data) => request.post('/users/login', data)
export const registerApi = (data) => request.post('/users/register', data)
export const getCurrApi = (data) => request.get('/users/current')
