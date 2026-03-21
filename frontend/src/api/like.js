import request from '@/utils/request'

export const likedApi = (postId) => request.post(`/likes/isliked/${postId}` )
export const isLikedApi = (postId) => request.get(`/likes/isliked/${postId}` )
