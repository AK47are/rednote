<template>
  <div class="user-info">
    <div class="avatar-wrapper">
      <el-avatar :size="140" :src="user.avatar" />
    </div>
    <div class="info-wrapper">
      <div class="info-header">
        <div class="user-name">{{ user.username || '用户不存在' }}</div>
        <div class="actions" v-if="currUserId && currUserId !== Number(userId)">
          <el-button
            size="large"
            style="width: 96px"
            :type="isFollowed ? 'default' : 'primary'"
            :loading="followLoading"
            round
            @click="handleToggleFollow"
          >
            {{ isFollowed ? '已关注' : '关注' }}
          </el-button>
          <el-button size="large" icon="More" text circle />
        </div>
      </div>

      <div class="meta">
        <span>用户ID：{{ user.userId || '--' }}</span>
      </div>
      <div class="bio">{{ user.bio || '暂无简介' }}</div>
      <div class="tag">
        <el-tag v-if="user.gender === 1" size="small" type="info" effect="light" round>
          <span style="display: flex; align-items: center; gap: 2px">
            <el-icon color="#409EFF" style="cursor: default"><Male /></el-icon>
            <span>男</span>
          </span>
        </el-tag>
        <el-tag v-else-if="user.gender === 2" size="small" type="info" effect="light" round>
          <span style="display: flex; align-items: center; gap: 2px">
            <el-icon color="#ff69b4" style="cursor: default"><Female /></el-icon>
            <span>女</span>
          </span>
        </el-tag>
      </div>
      <div class="stats">
        <div class="stat-item">
          <span class="stat-count">{{ user.followCount || 0 }}</span>
          <span class="stat-label">关注</span>
        </div>
        <div class="stat-item">
          <span class="stat-count">{{ user.fansCount || 0 }}</span>
          <span class="stat-label">粉丝</span>
        </div>
        <div class="stat-item">
          <span class="stat-count">{{ user.likeReceiveCount || 0 }}</span>
          <span class="stat-label">获赞与收藏</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import { getByIdApi, getDetailByIdApi, getCurrApi } from '@/api/user'
import { toggleFollowApi, isFollowApi } from '@/api/follow'

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
      currUserId: null,
      isFollowed: false,
      followLoading: false,
      user: {
        userId: null,
        username: '',
        avatar: '',
        bio: '',
        gender: 0,
        followCount: 0,
        fansCount: 0,
        likeReceiveCount: 0,
      },
    }
  },
  created() {
    getCurrApi()
      .then((res) => {
        this.currUserId = res.data.userId
        console.log('当前登录用户:', this.currUserId)
      })
      .catch((err) => {
        console.error('获取当前用户失败', err)
      })
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
        const [userRes, detailsRes] = await Promise.all([getByIdApi(id), getDetailByIdApi(id)])

        this.user = {
          userId: userRes.data.userId,
          username: userRes.data.username,
          avatar: userRes.data.avatar,
          bio: detailsRes.data.bio,
          gender: detailsRes.data.gender,
          followCount: detailsRes.data.followCount,
          fansCount: detailsRes.data.fansCount,
          likeReceiveCount: detailsRes.data.likeReceiveCount,
        }

        const res = await isFollowApi(id)
        this.isFollowed = res.data
      } catch (err) {
        ElMessage.error(err.message || '获取用户信息失败')
      }
    },
    async handleToggleFollow() {
      this.followLoading = true
      try {
        await toggleFollowApi(this.userId)
        this.isFollowed = !this.isFollowed
        this.fetchUser(this.userId)
      } catch (err) {
        ElMessage.error(err.message || '操作失败')
      } finally {
        this.followLoading = false
      }
    },
  },
}
</script>

<style scoped>
.user-info {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  gap: 32px;
}

.info-wrapper {
  flex: 1;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  margin-bottom: 8px;
}

.bio {
  font-size: 14px;
  margin-bottom: 12px;
  line-height: 1.5;
  display: -webkit-box;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 600px; /* 限制最大宽度，避免过长 */
}

.stats {
  display: flex;
  gap: 24px; /* 统计项之间的间距 */
  margin-top: 12px; /* 与上方的简介保持间距 */
}

.stat-item {
  display: flex;
  align-items: baseline; /* 数字和标签底部对齐 */
  gap: 4px; /* 数字和标签之间的间距 */
}

.stat-count {
  font-size: 18px;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
}
</style>
