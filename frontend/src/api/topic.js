import request from '@/utils/request'

export const getTopicListApi = () => request.get('/topics/list')