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

    <post-detail-card
      v-model:visible="detailVisible"
      :post="selectedPost"
      :show-follow-btn="currUserId !== selectedPost.userId"
    />

    </div>
</template>

<script>
import { getPostListApi } from '@/api/post'
import { getTopicListApi } from '@/api/topic'
import PostCard from '@/components/PostCard.vue'
import PostDetailCard from '@/components/PostDetailCard.vue'
import '@/assets/baseHome.css'
import { mapState } from 'pinia'
import { useUserStore } from '@/stores/user'

export default {
  components: {
    PostCard,
    PostDetailCard,
  },
  computed: {
    ...mapState(useUserStore, {
      currUserId: 'userId',
    }),
  },
  data() {
    return {
      postList: [],
      selectedPost: {},
      detailVisible: false,
      topicList: [],
      topicId: 0,
    }
  },  
  methods: {
    async initInfo(){
      const [postRes,topicRes] = await Promise.all([
        getPostListApi(this.topicId),
        getTopicListApi()
      ])
      this.postList = postRes.data
      this.topicList = topicRes.data
    },
    async handleTabClick(tab) {
      const topicId = parseInt(tab.paneName) 
      this.topicId = topicId
      const response = await getPostListApi(this.topicId)
      this.postList = response.data
    },
    showDetail(post) {
      this.selectedPost = post
      this.detailVisible = true
    }
  },
  mounted() {
    this.initInfo()
  }
}
</script>