import request from '@/utils/request'

export const getPostListApi = (params) => request.get('/posts/list', {params})
export const getPostDetailApi = (postId) => request.get(`/postsDetails/${postId}`)
export const getBloggerPostsApi = (bloggerId) => request.get(`/posts/list/${bloggerId}`)