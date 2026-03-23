<template>
  <div class="upload-article-page">
    <div class="upload-article-container">
      <div v-if="!isStartEdit" class="initial-upload">
        <div class="upload-header">
          <h3>文章发布</h3>
          <p>编写原创文章，分享你的知识与见解</p>
        </div>
        <el-button type="primary" size="large" @click="startEdit">开始编写文章</el-button>
      </div>
      <div v-else class="publish-edit">
        <div class="main-content">
          <div class="edit-area">
            <PostCoverEdit v-model="coverUrl" />
            <div class="content-edit">
              <PostHeaderEdit v-model="articleForm.title" />
              <el-input v-model="articleForm.content" type="textarea" :rows="18" placeholder="请输入文章内容" class="content-textarea" />
              <PostTopicEdit v-model="selectedTopics" />
            </div>

            <div class="bottom-btns">
              <el-button type="default" @click="cancelEdit">取消</el-button>
              <el-button type="primary" @click="publishArticleNote">发布文章</el-button>
            </div>
          </div>
          <div class="preview-area">
            <div class="preview-tabs">
              <div class="tab-item active">文章预览</div>
            </div>
            <div class="phone-preview">
              <div class="phone-header">
                <span class="time">{{  currentTime }}</span>
                <div class="status-icons">
                  <i class="el-icon-signal"></i>
                  <i class="el-icon-wifi"></i>
                </div>
              </div>
              <div class="phone-content">
                <h3 class="article-title">{{ articleForm.title || '文章标题' }}</h3>
                <div class="article-content">{{ articleForm.content || '文章正文预览...' }}</div>
                <div class="note-info">
                  <div class="user-info">
                    <img :src="userInfo.avatar" alt="用户头像" class="avatar">
                    <span class="username"> {{ userInfo.username }}</span>
                  </div>
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
import { ElMessage } from 'element-plus'
import { uploadFileApi } from '@/api/file'
import {createPostApi} from '@/api/post'
import { getCurrApi } from '@/api/user'
import PostCoverEdit from '@/components/PostCoverEdit.vue'
import PostHeaderEdit from '@/components/PostHeaderEdit.vue'
import PostTopicEdit from '@/components/PostTopicEdit.vue'
import  '@/assets/PostArticleEdit.css'
export default {
  name: 'UploadArticle',
  components: {
    PostCoverEdit,
    PostHeaderEdit,
    PostTopicEdit
  },
  data() {
    return {
      isStartEdit: false,
      articleForm: {
        title: '',
        content: ''
      },
      userInfo: {},
      currentTime: new Date().toLocaleTimeString('zh-CN', { timeZone: 'Asia/Shanghai' }),
      coverUrl: '',
      selectedTopics: [],
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
    // 用于更新当前时间
    updateCurrentTime() {
      this.currentTime = new Date().toLocaleTimeString('zh-CN', { timeZone: 'Asia/Shanghai' });
    },

    startEdit() {
      this.isStartEdit = true
    },

    cancelEdit() {
      this.isStartEdit = false
      this.coverUrl = ''
      this.articleForm.title = ''
      this.articleForm.content = ''
      this.selectedTopics = []
      this.postDTO = {
        title: '',
        content: '',
        coverImage: '',
        topicIds: [],
        fileUrls: []
      }
      ElMessage.info('已取消编辑')
    },

    async fetchUserInfo() {
      try {
        const response = await getCurrApi()
        this.userInfo = response.data
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    },

    async publishArticleNote() {
      if (!this.articleForm.title) {
        ElMessage.warning('请填写标题')
        return
      }
      if (!this.articleForm.content) {
        ElMessage.warning('内容未上传完成')
        return
      }
      this.postDTO = {
        title: this.articleForm.title,
        content: this.articleForm.content,
        coverImage: this.coverUrl,
        topicIds: this.selectedTopics,
        fileUrls: [this.coverUrl]
      }

      const res = await createPostApi(this.postDTO)
      ElMessage.success('发布成功！')
      this.isStartEdit = false
      this.coverUrl = ''
      this.articleForm.title = ''
      this.articleForm.content = ''
      this.selectedTopics = []
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
