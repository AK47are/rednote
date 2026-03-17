<template>
  <div class="profile-page">
    <h1>个人主页</h1>
    <p>用户ID：{{ userId }}</p>
  </div>
</template>

<script>
export default {
  name: 'ProfileView',
  computed: {
    userId() {
      return this.$route.params.userId
    },
  },
}
</script>

<style scoped>
.profile-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}
</style>
