<template>
  <el-col :xs="24" :sm="12"  :md="8" :lg="6" class="post-col">
    <el-card :body-style="{ padding: '0px' }" @click="showDetail(post)">
      <div class="post-image">
        <img :src="post.coverImage" :alt="post.title">
      </div>
      <div class="post-content">
        <h3 class="post-title">{{ post.title }}</h3>
        <p class="post-description">{{ post.content }}</p>
        <div class="post-user">
          <img :src="post.userAvatar" :alt="post.createTime" class="avatar">
          <span>{{ post.userName }}</span>
          <span class="time">{{ post.createTime }}</span>
        </div>
      </div>
    </el-card>
  </el-col>
</template>

<script>
export default {
  props: {
    post: {
      type: Object,
      required: true
    }
  },
  emits: ['show-detail'],
  methods: {
    showDetail(post) {
      this.$emit('show-detail', post)
    }
  }
}
</script>