import request from '@/utils/request'

export const getCommentListApi = (postId) => request.get(`/comments/list/${postId}` )
export const addCommentApi = (data) => request.post('/comments/insert', data)
