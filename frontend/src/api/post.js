import request from "@/utils/request"
export const getPostListApi = (topicId) => request.get(`/posts/list/${topicId}`)
