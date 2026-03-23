<template>
  <div class="topic-selector">
    <h4>选择话题</h4>
    <div class="tag-area" v-if="!isLoadingTopics">
      <el-tag
        v-for="topic in topicList"
        :key="topic.topicId"
        :type="selectedTopics.includes(topic.topicId) ? 'primary' : 'info'"
        @click="toggleTopic(topic.topicId)"
        class="selectable-tag"
      >
        {{ topic.topicName }}
      </el-tag>
    </div>
    <div v-else class="loading-topics">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>加载话题中...</span>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import { getTopicListApi } from '@/api/topic'
import { Loading } from '@element-plus/icons-vue'

export default {
  name: 'TopicSelector',
  components: {
    Loading
  },
  props: {
    modelValue: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      topicList: [],
      isLoadingTopics: true,
      selectedTopics: [...this.modelValue]
    }
  },
  mounted() {
    this.preloadTopics()
  },
  methods: {
    async preloadTopics() {
      try {
        this.isLoadingTopics = true
        const res = await getTopicListApi()
        this.topicList = res.data
      } catch (err) {
        console.error('加载话题失败')
      } finally {
        this.isLoadingTopics = false
      }
    },

    toggleTopic(topicId) {
      const idx = this.selectedTopics.indexOf(topicId)
      if (idx > -1) {
        this.selectedTopics.splice(idx, 1)
      } else {
        this.selectedTopics.push(topicId)
      }
      this.$emit('update:modelValue', [...this.selectedTopics])
    }
  }
}
</script>

<style scoped>
.topic-selector h4 {
  font-size: 16px;
  color: #333;
  margin: 0 0 8px 0;
}

.tag-area {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  margin-top: 10px;
}
.selectable-tag {
  cursor: pointer;
  user-select: none;
}
.selectable-tag:hover {
  opacity: 0.8;
}

.loading-topics {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 0;
  color: #666;
  font-size: 14px;
}
</style>
