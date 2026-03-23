<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="200px">
        <el-menu :default-active="activeTab" router class="el-menu-vertical-demo menu-container">
          <div class="menu-center-wrapper">
            <el-dropdown @command="handleDropdownCommand">
              <span class="el-dropdown-link publish-dropdown">
                <el-icon class="publish-icon"><CirclePlus /></el-icon>
                <span class="publish-text">发布笔记</span>
                <el-icon class="el-icon--right publish-arrow">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="/video"><el-icon><VideoCamera /></el-icon>上传视频</el-dropdown-item>
                  <el-dropdown-item command="/image"><el-icon><Picture /></el-icon>上传图文</el-dropdown-item>
                  <el-dropdown-item command="/article"><el-icon><EditPen /></el-icon>写长文</el-dropdown-item>
                  <el-dropdown-item command="/explore"><el-icon><HomeFilled /></el-icon>返回主页</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
        </el-header>
        <el-main >
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeTab: this.$route.path
    }
  },
  watch: {
    '$route.path'(newPath) {
      this.activeTab = newPath;
    }
  },
  methods: {
    handleDropdownCommand(command) {
      this.$router.push(command);
    },
    handleTabClick(tab) {
      this.$router.push(tab.props.name);
    }
  }
};
</script>

<style>
/* 菜单容器 */
.menu-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-right: none;
}
.menu-center-wrapper {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 10px; /* 顶部间距，可自行调整 */
}
.publish-dropdown {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-radius: 8px;
  background-color: #409EFF;
  color: white;
  font-weight: bold;
  transition: all 0.3s ease;
  cursor: pointer;
}

.publish-dropdown:hover {
  background-color: #66B1FF;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.publish-icon {
  font-size: 18px;
  margin-right: 8px;
}

.publish-text {
  font-size: 16px;
  margin-right: 8px;
}

.publish-arrow {
  font-size: 14px;
}
</style>
