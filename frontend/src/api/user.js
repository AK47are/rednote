import request from '@/utils/request'

export const loginApi = (data) => request.post('/users/login', data)
export const registerApi = (data) => request.post('/users/register', data)
export const getCurrApi = () => request.get('/users/current')
export const getByIdApi = (userId) => request.get(`/users/${userId}`)
export const getDetailByIdApi = (userId) => request.get(`/users/details/${userId}`)
export const updateUserApi = (data) => request.put('/users/profile', data)
