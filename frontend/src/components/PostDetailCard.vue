<template>
  <div v-if="visible">

    <div class="modal-mask" @click="handleClose"></div>
    <div class="modal-content-wrap">
      <div class="left-card">
        <div class="image-container" v-if="imageUrlList.length > 0">

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

          <img 
              v-else
              :src="imageUrlList[currentImageIndex]" 
              :alt="`Image ${currentImageIndex + 1}`"
              class="post-image"
          >

          <el-button 
              class="nav-btn prev-btn" 
              @click="prevImage"
              :disabled="currentImageIndex === 0"
              :icon="ArrowLeft"
              circle
          />
          <el-button 
              class="nav-btn next-btn" 
              @click="nextImage"
              :disabled="currentImageIndex === imageUrlList.length - 1"
              :icon="ArrowRight"
              circle
          />

          <div class="image-indicator">
              {{ currentImageIndex + 1 }} / {{ imageUrlList.length }}
          </div>
        </div>
        <div class="no-image" v-else>
            <p>暂无图片</p>
        </div>
      </div>

      <div class="right-card">

        <div class="right-card-header">

          <div class="user-header">
            <img class="avatar" :src="bloggerInfo.bloggerAvatar" alt="头像" />
            <span class="username">{{ bloggerInfo.bloggerName }}</span>

            <el-button 
              v-if="currUserId && currUserId !== Number(bloggerInfo.bloggerId)"
              class="follow-btn" 
              @click="handleFollow"
              :type="isFollowed ? 'default' : 'primary'"
              size="small"
            >{{ isFollowed ? '取消关注' : '关注' }}</el-button>
          </div>

          <div class="post-content">
            <p class="post-title">{{ post.title }}</p>
            <p class="post-desc">
              {{ post.content }}
            </p>
            <p class="post-meta">发布时间{{ post.createTime }}</p>
          </div>
        </div>

        <el-scrollbar class="comment-scroll-area">
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
        </el-scrollbar>

        <div class="right-card-footer">
          <div class="bottom-bar">
            <el-input 
              type="text" 
              placeholder="说点什么..." 
              class="comment-input" 
              v-model="commentInfo.content" 
              @keyup.enter="handleSendComment"
              clearable
            />
            <el-button class="send-btn" @click="handleSendComment" type="primary">发送</el-button>
            <el-button class="like-btn" @click="handleLike" :type="isLiked ? 'danger' : 'default'">{{ isLiked ? '取消点赞' : '点赞' }}</el-button>
            <span>{{ postDetail.likeCount }}</span>
          </div>
        </div>
      </div>
    </div>

    <el-button class="close-btn" @click="handleClose" :icon="Close" circle />
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import { getPostDetailApi} from '@/api/post'
import { getPostImagesApi } from '@/api/post_image'
import { getCommentListApi, addCommentApi } from '@/api/comment'
import { isLikeApi, toggleLikeApi } from '@/api/like'
import { isFollowApi, toggleFollowApi } from '@/api/follow'
import { mapState } from 'pinia'
import { useUserStore } from '@/stores/user'

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
  },
  computed: {
    ...mapState(useUserStore, {
      currUserId: 'userId',
    }),
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
      isFollowed: false
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
      
      const [detailsRes, imagesRes, commentRes, followRes, likeRes] = await Promise.all([
        getPostDetailApi(this.post.postId),
        getPostImagesApi(this.post.postId),
        getCommentListApi(this.post.postId),
        isFollowApi(this.post.userId),
        isLikeApi(this.post.postId)
      ])
      this.postDetail = detailsRes.data
      this.imageUrlList = imagesRes.data
      this.commentList = commentRes.data
      this.isFollowed = followRes.data
      this.isLiked = likeRes.data
    },
    async handleFollow(){
      try {
        const response = await toggleFollowApi(this.bloggerInfo.bloggerId)
        const previousStatus = this.isFollowed
        const followRes = await isFollowApi(this.bloggerInfo.bloggerId)
        this.isFollowed = followRes.data
        if (!this.isFollowed && previousStatus) {
          this.$emit('follow-status-changed', this.bloggerInfo.bloggerId)
        } 
      } catch (e) {
          console.log(`关注后用户数据更新失败：${e}`)
      }
    },
    async handleSendComment() {
      if (!this.commentInfo.content.trim()) {
        ElMessage.warning('请输入评论内容')
        return
      }
      try {
        await addCommentApi(this.commentInfo)
        this.commentInfo.content = ''
        const response = await getCommentListApi(this.currentPostId)
        this.commentList = response.data
        ElMessage.success('评论成功')
      } catch (e) {
        console.log(`评论失败: ${e}`)
      } 
    },
    async handleLike(){
      try {
        const response = await toggleLikeApi(this.currentPostId)
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
  },
}
</script>