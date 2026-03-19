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
      <el-menu-item index="/profile">
        <el-icon><User /></el-icon>
        <span>我</span>
      </el-menu-item>
    </div>
    <div class="menu-bottom">
      <el-menu-item index="settings">
        <el-dropdown trigger="click" placement="top-start">
          <span>
            <el-icon><Setting /></el-icon> 设置
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>The Action 1st</el-dropdown-item>
              <el-dropdown-item>The Action 2nd</el-dropdown-item>
              <el-dropdown-item>The Action 3rd</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-menu-item>
    </div>
  </el-menu>
</template>

<script setup></script>

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
