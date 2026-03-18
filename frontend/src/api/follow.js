import request from '@/utils/request'

export const toggleFollowApi = (userId) => request.post(`/follow/${userId}`)
export const isFollowApi = (userId) => request.get(`/follow/status/${userId}`)
