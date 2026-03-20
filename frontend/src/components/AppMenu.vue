<template>
  <el-menu :default-active="$route.path" router class="el-menu-vertical-demo menu-container">
    <div class="menu-top">
      <el-menu-item index="/explore">
        <el-icon><HomeFilled /></el-icon>
        <span>发现</span>
      </el-menu-item>
      <el-menu-item index="3">
        <el-icon><CirclePlus /></el-icon>
        <span>发布</span>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon><Message /></el-icon>
        <span>通知</span>
      </el-menu-item>
      <el-menu-item :index="'/profiles/' + userId">
        <el-icon><User /></el-icon>
        <span>我</span>
      </el-menu-item>
    </div>
    <div class="menu-bottom">
      <el-menu-item index="/settings">
        <el-icon><Setting /></el-icon>
        <span>设置</span>
      </el-menu-item>
    </div>
  </el-menu>
</template>

<script>
import { mapState } from 'pinia'
import { useUserStore } from '@/stores/user'

export default {
  computed: {
    ...mapState(useUserStore, ['userId']),
  },
}
</script>

<style scoped>
.menu-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.menu-top {
  flex: 1;
  overflow-y: auto;
}

.menu-bottom {
  margin-top: auto;
  padding-bottom: 20px;
}
</style>
