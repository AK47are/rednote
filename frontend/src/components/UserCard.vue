<template>
  <div class="user-info">
    <div class="avatar-wrapper">
      <el-avatar :size="140" :src="user.avatar" />
    </div>
    <div>
      <div class="user-name">{{ user.username || '用户不存在' }}</div>
      <div class="meta">
        <span>用户ID：{{ user.userId || '--' }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import { getByIdApi } from '@/api/user'

export default {
  name: 'UserCard',
  props: {
    userId: {
      type: [Number, String],
      required: true,
    },
  },
  data() {
    return {
      user: {
        userId: null,
        username: '',
        avatar: '',
      },
    }
  },
  watch: {
    userId: {
      immediate: true,
      handler(newId) {
        if (newId) {
          this.fetchUser(newId)
        }
      },
    },
  },
  methods: {
    async fetchUser(id) {
      try {
        const userRes = await getByIdApi(id)
        this.user = userRes.data
      } catch (err) {
        ElMessage.error(err.message || '获取用户信息失败')
      }
    },
  },
}
</script>

<style scoped>
.user-info {
  display: flex;
}

.avatar-wrapper {
  width: calc((100vw - 7 * 32px) / 6 * 1);
  height: calc(0.7 * calc((100vw - 7 * 32px) / 6 * 1));
  text-align: center;
}

.user-name {
  font-weight: 600;
  font-size: 26px;
  line-height: 120%;
  word-wrap: break-word;
  margin-bottom: 5px;
}
.meta {
  font-size: 12px;
  color: #999;
}
</style>
