<template>
  <div class="post-grid">
    <el-tabs @tab-click="handleTabClick">
      <el-tab-pane label="推荐" name="0"></el-tab-pane>
      <el-tab-pane v-for="topic in topicList" 
        :key="topic.topicId" :name="topic.topicId.toString()">
        <template #label>
          <el-tooltip :content="topic.topicDescription" placement="top">
            <span>{{ topic.topicName }}</span>
          </el-tooltip>
        </template>
      </el-tab-pane>
    </el-tabs>

    <el-row :gutter="20">
      <post-card 
        v-for="post in postList" 
        :key="post.postId"
        :post="post"
        @show-detail="showDetail"
      />
    </el-row>

    <post-detail-modal
      v-model:visible="detailVisible"
      :post="selectedPost"
      :show-follow-btn="userInfo.userId !== selectedPost.userId"
    />
  </div>
</template>

<script>
import { getPostListApi, getTopicListApi } from '@/api/post'
import { getCurrentUserInfo } from '@/api/user'
import PostCard from '@/components/PostCard.vue'
import PostDetailModal from '@/components/PostDetailModal.vue'
import '@/assets/baseHome.css'

export default {
  components: {
    PostCard,
    PostDetailModal
  },
  data() {
    return {
      postList: [],
      selectedPost: {},
      detailVisible: false,
      topicList: [],
      topicId: 0,
      userInfo: {},
    }
  },  
  methods: {
    async fetchPosts() {
      this.loading = true
      try {
        const response = await getPostListApi({
          topicId: this.topicId,
        })
        this.postList = response.data
      } catch (error) {
        console.error('获取帖子列表失败:', error)
      }
    },
    async fetchUserInfo(){
      try {
        const response = await getCurrentUserInfo()
        this.userInfo = response.data
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    },
    async fetchTopics() {
      try {
        const response = await getTopicListApi()
        this.topicList = response.data
      } catch (error) {
        console.error('获取话题列表失败:', error)
      }
    },
    handleTabClick(tab) {
      const topicId = parseInt(tab.paneName) 
      this.topicId = topicId
      this.fetchPosts()
    },
    showDetail(post) {
      this.selectedPost = post
      this.detailVisible = true
    }
  },
  mounted() {
    this.fetchPosts()
    this.fetchTopics()
    this.fetchUserInfo()
  }
}
</script>