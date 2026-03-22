<template>
  <div class="post-grid">
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane v-for="blogger in bloggerList" 
        :key="blogger.userId" 
        :name="blogger.userId.toString()" 
        :label="blogger.username">
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

    <post-detail-card
      v-model:visible="detailVisible"
      :post="selectedPost"
      @follow-status-changed="handleFollowStatusChanged"
    />
  </div>
</template>

<script>
import { getPostByUserIdApi } from '@/api/post'
import { getFollowingApi } from '@/api/follow'
import PostCard from '@/components/PostCard.vue'
import PostDetailCard from '@/components/PostDetailCard.vue'
import '@/assets/baseHome.css'

export default {
  components: {
    PostCard,
    PostDetailCard
  },
  data() {
    return {
      postList: [],
      selectedPost: {},
      detailVisible: false,
      bloggerList: [],
      bloggerId: 0,
      activeName: '',
    }
  },  
  methods: {
    async fetchPosts() {
      this.loading = true
      try {
        const response = await getPostByUserIdApi(this.bloggerId)
        this.postList = response.data
      } catch (error) {
        console.error('获取帖子列表失败:', error)
      }
    },
    async fetchFollowing() {
      try {
        const response = await getFollowingApi()
        this.bloggerList = response.data
        // 设置默认选中第一个博主
        if (this.bloggerList.length > 0) {
          this.activeName = this.bloggerList[0].userId.toString()
          this.bloggerId = this.bloggerList[0].userId
          this.fetchPosts()
        }
      } catch (error) {
        console.error('获取关注用户列表失败:', error)
      }
    },
    handleTabClick(tab) {
      const bloggerId = parseInt(tab.paneName) 
      this.bloggerId = bloggerId
      this.activeName = tab.paneName
      this.fetchPosts()
    },
    showDetail(post) {
      this.selectedPost = post
      this.detailVisible = true
    },
    handleFollowStatusChanged(bloggerId) {
      // 当取消关注时，重新获取关注用户列表以更新界面
      this.fetchFollowing()
    }
  },
  mounted() {
    this.fetchFollowing()
  }
}
</script>