<template>
  <div class="upload-video-page">
    <div class="upload-video-container">
      <div v-if="!isUploadStarted" class="initial-upload">
        <div class="upload-header">
          <h3>视频上传</h3>
          <p>支持 MP4、MOV、AVI 格式，单个文件不超过 200MB</p>
        </div>

        <el-upload
          class="video-upload-zone"
          action="/files/update"
          :file-list="videoFileList"
          :before-upload="beforeVideoUpload"
          :on-change="handleFileChange"
          :auto-upload="false"
          accept="video/mp4,video/quicktime,video/x-msvideo,video/mov"
          :limit="1"
        >
          <div class="upload-box">
            <i class="el-icon-video-camera" />
            <div class="upload-text">点击或拖拽视频上传</div>
            <div class="upload-tip">仅支持 MP4/MOV/AVI，≤200MB</div>
          </div>
        </el-upload>
      </div>

      <div v-else class="publish-edit">
        <div class="top-bar">
          <div class="video-upload-status">
            <span class="file-name">{{ currentVideoName }}</span>
          </div>
        </div>

        <div class="main-content">
          <div class="edit-area">
            <PostCoverEdit v-model="coverUrl" />

            <div class="content-edit">
              <PostHeaderEdit v-model="noteForm.title" />
              <el-input
                v-model="noteForm.content"
                type="textarea"
                :rows="6"
                placeholder="输入正文描述，真诚有价值的分享予人温暖"
                class="content-textarea"
              />

              <PostTopicEdit v-model="selectedTopics" />
            </div>

            <div class="bottom-btns">
              <el-button type="default" @click="cancelEdit">取消</el-button>
              <el-button type="primary" class="publish-btn" @click="publishVideoNote" :loading="isUploading">发布</el-button>
            </div>
          </div>

          <div class="preview-area">
            <div class="preview-tabs">
              <div class="tab-item active">笔记预览</div>
              <!-- <div class="tab-item">封面预览</div> -->
            </div>
            <div class="phone-preview">
              <div class="phone-header">
                <span class="time">{{ currentTime }}</span>
              </div>
              <div class="phone-content">
                <!-- <div class="video-preview"> -->
                  <img :src="coverImage"  class="preview-img"/>
                  <!-- <i class="el-icon-video-play play-icon"></i> -->
                <!-- </div> -->
                <div class="note-info">
                  <div class="user-info">
                    <img :src="userInfo.avatar" alt="头像" class="avatar">
                    <span class="username">{{ userInfo.username }} <i class="el-icon-star-on vip-icon"></i></span>
                  </div>
                  <p class="publish-time">编辑于 刚刚公开可见</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { uploadFileApi } from '@/api/file'
import { createPostApi } from '@/api/post'
import { getCurrApi } from '@/api/user'
import PostCoverEdit from '@/components/PostCoverEdit.vue'
import PostHeaderEdit from '@/components/PostHeaderEdit.vue'
import PostTopicEdit from '@/components/PostTopicEdit.vue'
import  '@/assets/PostVideoEditView.css'
export default {
  name: 'UploadVideo',
  components: {
    PostCoverEdit,
    PostHeaderEdit,
    PostTopicEdit
  },
  data() {
    return {
      isUploadStarted: false,
      videoFileList: [],
      currentVideoName: '',

      uploadedVideoUrl: '',
      isUploading: false,
      currentTime: new Date().toLocaleTimeString('zh-CN', { timeZone: 'Asia/Shanghai' }),
      noteForm: {
        title: '',
        content: ''
      },
       coverUrl: '',
      selectedTopics: [],
      selectedFile: null,
      postDTO: {
        title: '',
        content: '',
        coverImage: '',
        topicIds: [],
        fileUrls: []
      },
      userInfo: {}
    }
  },
  mounted() {
    this.fetchUserInfo()
    setInterval(this.updateCurrentTime, 1000)
  },
  methods: {
    updateCurrentTime() {
      this.currentTime = new Date().toLocaleTimeString('zh-CN', { timeZone: 'Asia/Shanghai' });
    },

    beforeVideoUpload(file) {
      const isVideo = file.type.startsWith('video/')
      const isLt200M = file.size / 1024 / 1024 < 200

      if (!isVideo) {
        ElMessage.error('只能上传视频文件！')
        return false
      }
      if (!isLt200M) {
        ElMessage.error('视频大小不能超过 200MB！')
        return false
      }
      return true
    },

    async fetchUserInfo() {
      try {
        const response = await getCurrApi()
        this.userInfo = response.data
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    },

    handleFileChange(uploadFile) {
      if (uploadFile.status === 'ready') {
        const valid = this.beforeVideoUpload(uploadFile.raw)
        if (!valid) {
          this.videoFileList = []
          return
        }
        this.selectedFile = uploadFile.raw
        this.currentVideoName = uploadFile.name
        this.isUploadStarted = true
        this.uploadVideo(uploadFile.raw)
      }
    },

    async uploadVideo(uploadfile) {
      if (!uploadfile) return

      this.isUploading = true

      try {
        const res = await uploadFileApi(uploadfile)
        console.log("url: ", res.data)
        this.uploadedVideoUrl = res.data
        ElMessage.success('视频上传完成！')

      } catch (err) {
        console.error('上传失败', err)
      } finally {
        this.isUploading = false
      }
    },

    cancelEdit() {
      this.isUploadStarted = false
      this.videoFileList = []
      this.selectedFile = null
      this.uploadedVideoUrl = ''
      this.coverUrl = ''
      this.noteForm.title = ''
      this.noteForm.content = ''
      this.selectedTopics = []
      this.isUploading = false
      this.postDTO = {
        title: '',
        content: '',
        coverImage: '',
        topicIds: [],
        fileUrls: []
      }
      ElMessage.info('已取消编辑')
    },

    async publishVideoNote() {
      if (!this.noteForm.title) {
        ElMessage.warning('请填写标题')
        return
      }
      if (!this.uploadedVideoUrl) {
        ElMessage.warning('视频未上传完成')
        return
      }
      this.postDTO = {
        title: this.noteForm.title,
        content: this.noteForm.content,
        coverImage: this.coverUrl,
        topicIds: this.selectedTopics,
        fileUrls: [this.uploadedVideoUrl]
      }

      this.isUploading = true
      const res = await createPostApi(this.postDTO)
      setTimeout(() => {
        if(res.code === 200){
          ElMessage.success('发布成功！')
        }else{
          console.error('发布失败：' + res.message)
        }
      }, 3)

      this.isUploadStarted = false
      this.videoFileList = []
      this.selectedFile = null
      this.uploadedVideoUrl = ''
      this.coverUrl = ''
      this.noteForm.title = ''
      this.noteForm.content = ''
      this.selectedTopics = []
      this.isUploading = false
      this.postDTO = {
        title: '',
        content: '',
        coverImage: '',
        topicIds: [],
        fileUrls: []
      }
    }
  }
}
</script>
