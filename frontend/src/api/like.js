import request from '@/utils/request'

export const toggleLikeApi = (postId) => request.post(`/likes/${postId}`)
export const isLikeApi = (postId) => request.get(`/likes/status/${postId}`)
