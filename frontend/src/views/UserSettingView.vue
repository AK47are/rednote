<template>
  <div class="user-info">
    <div>
      <el-upload
        class="avatar-uploader"
        :http-request="customUpload"
        :show-file-list="false"
        accept="image/*"
      >
        <el-avatar :size="140" :src="form.avatar" class="clickable-avatar" />
        <div class="upload-mask">
          <el-icon :size="24"><Camera /></el-icon>
          <span>修改头像</span>
        </div>
      </el-upload>
    </div>

    <div class="info-wrapper">
      <div class="info-header">
        <div class="user-name">编辑个人资料</div>
        <div class="actions">
          <el-button
            size="large"
            type="primary"
            style="width: 96px"
            :loading="submitting"
            round
            @click="submitUpdate"
          >
            保存
          </el-button>
        </div>
      </div>

      <div class="meta">
        <span>用户ID：{{ form.userId || '--' }}</span>
      </div>

      <el-form :model="form" label-position="top" class="edit-form">
        <el-form-item label="昵称">
          <el-input
            v-model="form.username"
            placeholder="输入你的昵称"
            maxlength="20"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :value="1">
              <el-icon color="#409EFF"><Male /></el-icon> 男
            </el-radio>
            <el-radio :value="2">
              <el-icon color="#ff69b4"><Female /></el-icon> 女
            </el-radio>
            <el-radio :value="0">保密</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="简介">
          <el-input
            v-model="form.bio"
            type="textarea"
            :rows="3"
            placeholder="介绍一下自己吧"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import { Camera, Male, Female } from '@element-plus/icons-vue'
import { getByIdApi, getDetailByIdApi, updateUserApi } from '@/api/user'
import { uploadFileApi } from '@/api/file'
import { mapState } from 'pinia'
import { useUserStore } from '@/stores/user'

export default {
  name: 'UserSetting',
  data() {
    return {
      submitting: false,
      loading: false,
      form: {
        userId: null,
        username: '',
        avatar: '',
        bio: '',
        gender: 0,
      },
    }
  },
  computed: {
    ...mapState(useUserStore, {
      currUserId: 'userId',
    }),
  },
  async created() {
    await this.fetchUser()
  },
  methods: {
    async fetchUser() {
      this.loading = true
      try {
        const [userRes, detailsRes] = await Promise.all([
          getByIdApi(this.currUserId),
          getDetailByIdApi(this.currUserId),
        ])
        this.form = {
          userId: userRes.data.userId,
          username: userRes.data.username,
          avatar: userRes.data.avatar,
          bio: detailsRes.data.bio,
          gender: detailsRes.data.gender,
        }
      } catch (e) {
        console.log(`初始化用户信息失败: ${e}`)
      } finally {
        this.loading = false
      }
    },
    async customUpload(options) {
      const res = await uploadFileApi(options.file)
      this.form.avatar = res.data
      ElMessage.success('头像上传成功')
    },
    async submitUpdate() {
      this.submitting = true
      try {
        await updateUserApi(this.form)
        ElMessage.success('资料更新成功')
        this.$router.push(`/profiles/${this.currUserId}`)
      } finally {
        this.submitting = false
      }
    },
  },
}
</script>

<style scoped>
.user-info {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 32px;
}

.avatar-uploader {
  cursor: pointer;
  position: relative;
  border-radius: 50%;
  overflow: hidden;
}

.clickable-avatar {
  transition: filter 0.3s;
}

.upload-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 140px;
  height: 140px;
  background: rgba(0, 0, 0, 0.4);
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s;
  font-size: 12px;
  gap: 8px;
}

.avatar-uploader:hover .upload-mask {
  opacity: 1;
}

.info-wrapper {
  flex: 1;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.user-name {
  font-weight: 600;
  font-size: 26px;
  line-height: 120%;
}

.meta {
  font-size: 12px;
  color: #999;
  margin-bottom: 24px;
}

.edit-form {
  margin-top: 20px;
}
</style>
