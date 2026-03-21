<template>
  <div v-if="visible">
    <!-- 遮罩层 -->
    <div class="modal-mask" @click="handleClose"></div>
    <div class="modal-content-wrap">
      <div class="left-card">
        <div class="image-container" v-if="imageUrlList.length > 0">
          <!-- 视频播放 -->
          <video 
              v-if="isVideo(imageUrlList[currentImageIndex])"
              :src="imageUrlList[currentImageIndex]"
              :alt="`Video ${currentImageIndex + 1}`"
              class="post-image"
              controls
              autoplay
              muted
              loop
          ></video>
          <!-- 图片显示 -->
          <img 
              v-else
              :src="imageUrlList[currentImageIndex]" 
              :alt="`Image ${currentImageIndex + 1}`"
              class="post-image"
          >
          <!-- 翻页按钮 -->
          <button 
              class="nav-btn prev-btn" 
              @click="prevImage"
              :disabled="currentImageIndex === 0"
          >
              &lt;
          </button>
          <button 
              class="nav-btn next-btn" 
              @click="nextImage"
              :disabled="currentImageIndex === imageUrlList.length - 1"
          >
              &gt;
          </button>
          <!-- 图片指示器 -->
          <div class="image-indicator">
              {{ currentImageIndex + 1 }} / {{ imageUrlList.length }}
          </div>
        </div>
        <div class="no-image" v-else>
            <p>暂无图片</p>
        </div>
      </div>

      <div class="right-card">
        <!-- 顶部固定区域：用户信息 + 帖子内容 -->
        <div class="right-card-header">
          <!-- 顶部用户信息 -->
          <div class="user-header">
            <img class="avatar" :src="bloggerInfo.bloggerAvatar" alt="头像" />
            <span class="username">{{ bloggerInfo.bloggerName }}</span>
            <button 
              v-if="showFollowBtn"
              class="follow-btn" 
              @click="handleFollow">{{ followed ? '取消关注' : '关注' }}</button>
          </div>

          <!-- 正文内容 -->
          <div class="post-content">
            <p class="post-title">{{ post.title }}</p>
            <p class="post-desc">
              {{ post.content }}
            </p>
            <p class="post-meta">发布时间{{ post.createTime }}</p>
          </div>
        </div>

        <!-- 中间可滚动评论区 -->
        <div class="comment-scroll-area">
          <div class="comment-section">
            <p class="comment-count">共{{ commentList.length }}条评论</p>
            <div class="comment-item" v-for="comment in commentList" :key="comment.commentId">
              <img class="comment-avatar" :src="comment.userAvatar" alt="评论头像" />
              <div class="comment-content">
                <span class="comment-username">{{ comment.userName }}</span>
                <p class="comment-text">{{ comment.content }}</p>
                <p class="comment-meta">{{ comment.createTime }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 底部固定区域：评论输入 + 互动按钮 -->
        <div class="right-card-footer">
          <div class="bottom-bar">
            <input type="text" placeholder="说点什么..." 
            class="comment-input" 
            v-model="commentInfo.content" 
            @keyup.enter="handleSendComment"
            />
            <button class="send-btn" @click="handleSendComment">发送</button>
            <button class="like-btn" @click="handleLike">{{ isLiked ? '取消点赞' : '点赞' }}</button>
            <span>{{ postDetail.likeCount }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 关闭按钮 --> 
    <button class="close-btn" @click="handleClose">×</button>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import { getPostDetailApi} from '@/api/post'
import { getPostImagesApi } from '@/api/image'
import { getCommentListApi, addCommentApi } from '@/api/comment'
import { isLikedApi, likedApi } from '@/api/like'
import { followUserApi, isFollowedApi } from '@/api/user'

export default {
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    post: {
      type: Object,
      default: () => ({})
    },
    showFollowBtn: {
      type: Boolean,
      default: true
    }
  },
  emits: ['update:visible', 'follow-status-changed'],
  data() {
    return {
      postDetail: {},
      imageUrlList: [],
      currentImageIndex: 0,
      commentList: [],
      commentInfo: {
        postId: 0,
        content: '',
      },
      currentPostId: 0,
      isLiked: false,
      bloggerInfo: {
        bloggerId: 0,
        bloggerName: '',
        bloggerAvatar: '',
      },
      followed: false
    }
  },
  watch: {
    visible(newVal) {
      if (newVal && this.post.postId) {
        this.initPostDetail()
      }
    },
    post(newPost) {
      if (this.visible && newPost.postId) {
        this.initPostDetail()
      }
    }
  },
  methods: {
    async initPostDetail() {
      if (!this.post.postId) return
      
      this.currentPostId = this.post.postId
      this.commentInfo.postId = this.post.postId
      this.bloggerInfo.bloggerId = this.post.userId
      this.bloggerInfo.bloggerName = this.post.userName
      this.bloggerInfo.bloggerAvatar = this.post.userAvatar
      this.currentImageIndex = 0
      
      await Promise.all([
        this.getPostDetail(this.post.postId),
        this.fetchPostImages(this.post.postId),
        this.fetchCommentList(this.post.postId),
        this.isFollowed(this.post.userId),
        this.isLike(this.post.postId)
      ])
    },
    async getPostDetail(postId){
      try {
        const response = await getPostDetailApi(postId)
        this.postDetail = response.data
      } catch (error) {
        console.error('获取帖子详情失败:', error)
      }
    },
    async fetchPostImages(postId) {
      try {
        const response = await getPostImagesApi(postId)
        this.imageUrlList = response.data
      } catch (error) {
        console.error('获取帖子图片失败:', error)
      }
    },
    async fetchCommentList(postId){
      try {
        const response = await getCommentListApi(postId)
        this.commentList = response.data
      } catch (error) {
        console.error('获取评论列表失败:', error)
      }
    },
    async isLike(postId){
      try {
        const response = await isLikedApi(postId)
        this.isLiked = response.data
      } catch (error) {
        ElMessage.error('操作失败，请稍后重试')
      }
    },
    async isFollowed(bloggerId){
      try {
        const response = await isFollowedApi(bloggerId)
        this.followed = response.data
      } catch (error) {
        ElMessage.error('操作失败，请稍后重试')
      }
    },
    async handleFollow(){
      try {
        const response = await followUserApi(this.bloggerInfo.bloggerId)
        const previousStatus = this.followed
        this.followed = response.data
        if (response.code === 500){
          ElMessage.error('不能关注自己')
          return
        }
        if (this.followed) {
          ElMessage.success('关注成功')
        } else {
          ElMessage.success('已经取消关注')
          // 当从关注状态变为未关注状态时，触发事件通知父组件
          if (previousStatus) {
            this.$emit('follow-status-changed', this.bloggerInfo.bloggerId)
          }
        }
      } catch (error) {
        ElMessage.error('操作失败，请稍后重试')
      }
    },
    async handleSendComment() {
      if (!this.commentInfo.content.trim()) {
        ElMessage.warning('请输入评论内容')
        return
      }
      try {
        await addCommentApi(this.commentInfo)
        ElMessage.success('评论成功')
        this.commentInfo.content = ''
        this.fetchCommentList(this.currentPostId)
      } catch (error) {
        ElMessage.error('评论失败')
      }
    },
    async handleLike(){
      try {
        const response = await likedApi(this.currentPostId)
        this.isLiked = response.data
        if(this.isLiked){
          ElMessage.success('点赞成功')
          this.postDetail.likeCount++
        }else{
          ElMessage.success('取消点赞成功')
          this.postDetail.likeCount--
        }
      } catch (error) {
        console.error('点赞失败:', error)
      }
    },
    handleClose() {
      this.$emit('update:visible', false)
      this.currentImageIndex = 0
    },
    prevImage() {
      if (this.currentImageIndex > 0) {
        this.currentImageIndex--
      }
    },
    nextImage() {
      if (this.currentImageIndex < this.imageUrlList.length - 1) {
        this.currentImageIndex++
      }
    },
    isVideo(url) {
      const videoExtensions = ['.mp4', '.webm', '.ogg', '.mov', '.avi']
      return videoExtensions.some(ext => url.toLowerCase().endsWith(ext))
    }
  }
}
</script>