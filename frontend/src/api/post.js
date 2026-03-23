import request from "@/utils/request"
export const getPostListApi = (topicId) => request.get(`/posts/list/${topicId}`)
export const getPostDetailApi = (postId) => request.get(`/posts/details/${postId}`)
export const getPostByUserIdApi = (userId) => request.get(`/posts/list/user/${userId}`)
export const createPostApi = (postDTO) => request.post('/posts/insert', postDTO)
