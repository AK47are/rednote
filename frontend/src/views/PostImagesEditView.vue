<template>
  <div class="upload-image-page">
    <div class="upload-image-container">
      <!-- 初始上传界面 -->
      <div v-if="!isUploadStarted" class="initial-upload">
        <div class="upload-header">
          <h3>图片上传</h3>
          <p>支持 JPG、PNG、WEBP 格式，单张不超过 20MB，最多上传 9 张</p>
        </div>

        <el-upload
          class="image-upload-zone"
          action="/file/update"
          :file-list="imageFileList"
          :before-upload="beforeImageUpload"
          :on-change="handleFileChange"
          :on-exceed="handleExceed"
          :on-remove="handleRemove"
          :auto-upload="false"
          accept="image/jpeg,image/png,image/webp"
          :limit="9"
          list-type="picture-card"
        >
          <div class="upload-box">
            <i class="el-icon-picture" />
            <div class="upload-text">点击或拖拽图片上传</div>
            <div class="upload-tip">仅支持 JPG/PNG/WEBP，≤20MB，最多 9 张</div>
          </div>
        </el-upload>
      </div>

      <div v-else class="publish-edit">


        <div class="main-content">
          <div class="edit-area">
            <PostCoverEdit v-model="coverUrl" />
            <div class="image-slider-section">
              <div class="image-slider-container">
                <div class="image-slider">
                  <div
                    class="image-item"
                    v-for="(item, index) in imageFileList"
                    :key="index"
                  >
                    <img :src="item.url" alt="预览图" />
                    <i class="el-icon-close" @click="handleRemove(item)" />
                  </div>

                  <el-upload
                    v-if="imageFileList.length < 9"
                    class="add-image-upload"
                    action="/api/upload/image"
                    :file-list="imageFileList"
                    :before-upload="beforeImageUpload"
                    :on-change="handleInPageChange"
                    :on-remove="handleRemove"
                    :on-exceed="handleExceed"
                    :auto-upload="false"
                    accept="image/jpeg,image/png,image/webp"
                    :limit="9"
                  >
                    <div class="add-btn">
                      <i class="el-icon-plus"></i>
                    </div>
                  </el-upload>
                </div>
              </div>
            </div>
            <div class="content-form">
              <PostHeaderEdit v-model="noteForm.title" />
              <el-input
                v-model="noteForm.content"
                type="textarea"
                :rows="6"
                class="content-input"
                placeholder="添加正文描述..."
              />
              <PostTopicEdit v-model="selectedTopics" />
            </div>

            <div class="action-buttons">
              <el-button type="default" @click="cancelEdit">取消</el-button>
              <el-button type="primary" @click="publishImagesNote">发布</el-button>
            </div>
          </div>

          <div class="preview-area">
            <div class="phone-preview">
              <div class="phone-header">
                <span>{{ currentTime }}</span>
                <div class="status-icons">
                  <i class="el-icon-signal"></i>
                  <i class="el-icon-wifi"></i>
                </div>
              </div>
              <div class="phone-body">
                <img :src="coverUrl" class="preview-image" />
                <div class="preview-content">
                  <div class="preview-user">
                    <img :src="userInfo.avatar" alt="头像" />
                    <span>{{ userInfo.username }}</span>
                  </div>
                  <p class="preview-title">{{ noteForm.title || '请输入标题' }}</p>
                  <p class="preview-desc">{{ noteForm.content || '请输入描述' }}</p>
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
import PostCoverEdit from '@/components/PostCoverEdit.vue'
import PostHeaderEdit from '@/components/PostHeaderEdit.vue'
import PostTopicEdit from '@/components/PostTopicEdit.vue'
import {createPostApi} from '@/api/post'
import { getCurrApi } from '@/api/user'
import  '@/assets/PostImagesEditView.css'
export default {
  name: 'UploadImage',
  components: {
    PostCoverEdit,
    PostHeaderEdit,
    PostTopicEdit
  },
  data() {
    return {
      userInfo: {},
      currentTime: new Date().toLocaleTimeString('zh-CN', { timeZone: 'Asia/Shanghai' }),
      isUploadStarted: false,
      imageFileList: [],
      noteForm: { title: '', content: '' },
      selectedTopics: [],
      isUploading: false,
      coverUrl: '',
      postDTO: {
        title: '',
        content: '',
        coverImage: '',
        topicIds: [],
        fileUrls: []
      }
    }
  },
    mounted() {
    this.fetchUserInfo()
    // 每秒钟更新一次当前时间
    setInterval(this.updateCurrentTime, 1000)
  },
  methods: {
    updateCurrentTime() {
      this.currentTime = new Date().toLocaleTimeString('zh-CN', { timeZone: 'Asia/Shanghai' });
    },
    // 上传校验
    beforeImageUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt20M = file.size / 1024 / 1024 < 20
      if (!isImage) { ElMessage.error('只能上传图片'); return false }
      if (!isLt20M) { ElMessage.error('单张不超过20MB'); return false }
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
    // 数量超限
    handleExceed() {
      ElMessage.warning('最多上传9张')
    },

    // 删除图片
    handleRemove(file) {
      this.imageFileList = this.imageFileList.filter(item => item.uid !== file.uid)
    },

    // 初始上传
    handleFileChange(file, fileList) {
      if (file.status === 'ready') {
        file.url = URL.createObjectURL(file.raw)
        this.imageFileList = fileList
        this.isUploadStarted = true
        this.uploadSingleImage(file)
      }
    },

    // 编辑页内添加
    handleInPageChange(file, fileList) {
      if (file.status === 'ready') {
        file.url = URL.createObjectURL(file.raw)
        this.imageFileList = fileList
        this.uploadSingleImage(file)
      }
    },

    async uploadSingleImage(UploadFile) {
      try {

        const response = await uploadFileApi(UploadFile.raw)

        const index = this.imageFileList.findIndex(item => item.uid === UploadFile.uid)
        if (index !== -1) {
          // 将接口返回的URL添加到imageFileList中，用于显示图片
          this.imageFileList[index].url = response.data
          // 保存服务器返回的URL到serverUrl字段
          this.imageFileList[index].serverUrl = response.data
          ElMessage.success('图片上传成功')
        }
      } catch (error) {
        console.error('上传失败:', error)
      }
    },

    cancelEdit() {
      this.isUploadStarted = false
      this.imageFileList = []
      this.noteForm.title = ''
      this.noteForm.content = ''
      this.selectedTopics = []
      this.coverUrl = ''
      this.postDTO = {
        title: '',
        content: '',
        coverImage: '',
        topicIds: [],
        fileUrls: []
      }
      ElMessage.info('已取消编辑')
    },

    async publishImagesNote() {
      if (!this.noteForm.title) {
        ElMessage.warning('请填写标题')
        return
      }
      if (this.imageFileList.length === 0) {
        ElMessage.warning('请上传图片')
        return
      }

      // 检查是否所有图片都已上传完成
      const unuploaded = this.imageFileList.some(item => !item.serverUrl)
      if (unuploaded) {
        ElMessage.warning('部分图片尚未上传完成，请稍候重试')
        return
      }

      // 构建fileUrls
      const fileUrls = this.imageFileList.map(item => item.serverUrl)

      this.postDTO = {
        title: this.noteForm.title,
        content: this.noteForm.content,
        coverImage: this.coverUrl,
        topicIds: this.selectedTopics,
        fileUrls: fileUrls
      }

      this.isUploading = true
      try {
        const res = await createPostApi(this.postDTO)
        ElMessage.success('发布成功！')
        this.isUploadStarted = false
        this.imageFileList = []
        this.noteForm.title = ''
        this.noteForm.content = ''
        this.selectedTopics = []
        this.coverUrl = ''
      } catch (error) {
        console.error('发布失败：' + (error.message || '未知错误'))
      } finally {
        this.isUploading = false
      }
      this.isUploadStarted = false
      this.imageFileList = []
      this.noteForm.title = ''
      this.noteForm.content = ''
      this.selectedTopics = []
      this.coverUrl = ''
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

