<template>
  <div class="cover-setting">
    <h4>设置封面</h4>
    <div class="cover-options">
      <div class="custom-cover">
        <input
          type="file"
          ref="coverInput"
          accept="image/jpeg,image/png,image/gif"
          style="display: none"
          @change="handleCoverChange"
        />
        <div class="cover-btn" @click="selectCover">
          <i class="el-icon-picture"></i>
          <span>设置封面</span>
        </div>
      </div>
      <div v-if="coverImage" class="cover-preview">
        <img :src="coverImage" alt="封面预览" class="cover-img" />
        <el-button type="text" class="remove-cover" @click="removeCover">移除封面</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import { uploadFileApi } from '@/api/file'

export default {
  name: 'CoverSetting',
  props: {
    modelValue: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      coverImage: null,
      coverUrl: this.modelValue,
      coverUploading: false
    }
  },
  methods: {
    selectCover() {
      // 触发隐藏的文件输入框
      this.$refs.coverInput.click()
    },
    async handleCoverChange(event) {
      const file = event.target.files[0]
      if (!file) return
      const isImage = file.type.startsWith('image/')
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isImage) {
        ElMessage.error('只能上传图片文件！')
        return
      }
      if (!isLt10M) {
        ElMessage.error('图片大小不能超过 10MB！')
        return
      }
      const reader = new FileReader()
      reader.onload = (e) => {
        this.coverImage = e.target.result
      }
      reader.readAsDataURL(file)
      if (!file) return

      this.coverUploading = true

      try {
        const res = await uploadFileApi(file)
        this.coverUrl = res.data
        this.$emit('update:modelValue', this.coverUrl)
        ElMessage.success('封面上传成功！')
      } catch (err) {
        console.error('封面上传失败', err)
        this.coverImage = null
      } finally {
        this.coverUploading = false
      }
    },
    removeCover() {
      this.coverImage = null
      this.coverUrl = ''
      this.$emit('update:modelValue', '')
      if (this.$refs.coverInput) {
        this.$refs.coverInput.value = ''
      }
    }
  }
}
</script>

<style scoped>
.cover-setting h4 {
  font-size: 16px;
  color: #333;
  margin: 0 0 8px 0;
}

.cover-options {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}
.custom-cover .cover-btn {
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s;
}
.custom-cover .cover-btn:hover {
  border-color: #409eff;
  background: #f0f7ff;
}
.custom-cover .cover-btn i {
  font-size: 24px;
  color: #409eff;
}
.custom-cover .cover-btn span {
  font-size: 12px;
  color: #666;
}

.cover-preview {
  position: relative;
}
.cover-img {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  object-fit: cover;
}
.remove-cover {
  position: absolute;
  bottom: -20px;
  left: 0;
  color: #f56c6c !important;
  font-size: 12px;
}
</style>
