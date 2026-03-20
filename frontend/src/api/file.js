import request from '@/utils/request'

export const uploadFileApi = (file) => {
  const formData = new FormData()
  formData.append('file', file)

  return request.post('/files/update', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}
