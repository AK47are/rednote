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

    </div>
</template>

<script>
import { getPostListApi } from '@/api/post'
import { getTopicListApi } from '@/api/topic'
import PostCard from '@/components/PostCard.vue'
import '@/assets/baseHome.css'

export default {
  components: {
    PostCard,
  },
  data() {
    return {
      postList: [],
      
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

    }
  },
  mounted() {
    this.initInfo()
  }
}
</script>