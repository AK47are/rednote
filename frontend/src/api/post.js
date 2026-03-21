import request from "@/utils/request"
export const getPostListApi = (topicId) => request.get(`/posts/list/${topicId}`)
export const getPostDetailApi = (postId) => request.get(`/posts/details/${postId}`)
