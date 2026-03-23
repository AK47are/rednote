<template>
  <div class="content-title">
    <h4>设置标题</h4>
    <el-input
      v-model="title"
      placeholder="填写标题会有更多赞哦"
      class="title-input"
      @input="handleInput"
    />
  </div>
</template>

<script>
export default {
  name: 'ContentTitle',
  props: {
    modelValue: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      title: this.modelValue
    }
  },
  methods: {
    handleInput() {
      this.$emit('update:modelValue', this.title)
    }
  },
  watch: {
    modelValue: {
      handler(newValue) {
        this.title = newValue
      },
      immediate: true
    }
  }
}
</script>

<style scoped>
.content-title h4 {
  font-size: 16px;
  color: #333;
  margin: 0 0 8px 0;
}

.title-input {
  font-size: 16px;
  padding: 10px;
  border-radius: 4px;
  width: 100%;
}
</style>
