import request from '@/utils/request'

export const getPostListApi = (params) => request.get('/posts/list', {params})