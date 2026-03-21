import request from "@/utils/request"

export const getPostImagesApi = (postId) => request.get(`/post/images/${postId}`)
