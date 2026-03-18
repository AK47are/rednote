<template>
  <div class="profile-page">
    <UserCard :user-id="userId" />
  </div>
</template>

<script>
import UserCard from '@/components/UserCard.vue'

export default {
  name: 'ProfileView',
  components: { UserCard },
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
