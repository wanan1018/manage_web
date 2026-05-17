/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
<template>
  <div class="app-container">
    <el-container>
      <el-header class="app-header">
        <div class="header-left">
          <span class="app-title">任务管理系统</span>
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            router
            class="header-menu"
          >
            <el-menu-item index="/tasks">任务管理</el-menu-item>
            <el-menu-item index="/dashboard">数据仪表盘</el-menu-item>
            <el-menu-item v-if="isTutor" index="/members">成员管理</el-menu-item>
            <el-menu-item index="/news">资讯</el-menu-item>
            <el-menu-item index="/about">关于我们</el-menu-item>
          </el-menu>
        </div>
        <div class="header-right" v-if="userStore.token">
          <el-dropdown>
            <span class="user-info">
              <el-icon><UserFilled /></el-icon>
              {{ userStore.username }}
              <el-tag :type="isTutor ? 'danger' : 'success'" size="small" class="role-tag">
                {{ isTutor ? '导师' : '实习生' }}
              </el-tag>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-item disabled>角色：{{ isTutor ? '导师' : '实习生' }}</el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { UserFilled, ArrowDown } from '@element-plus/icons-vue'
import { useUserStore } from './store/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => {
  if (route.path.startsWith('/members')) return '/members'
  if (route.path.startsWith('/news')) return '/news'
  if (route.path.startsWith('/about')) return '/about'
  if (route.path.startsWith('/dashboard')) return '/tasks'
  return route.path
})

const isTutor = computed(() => userStore.isTutor)

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif; }

.app-container { height: 100vh; display: flex; flex-direction: column; }

.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0 24px;
  height: 60px;
}

.header-left { display: flex; align-items: center; gap: 24px; }

.app-title {
  color: #fff;
  font-size: 20px;
  font-weight: 700;
  white-space: nowrap;
}

.header-menu {
  background: transparent !important;
  border-bottom: none !important;
}
.header-menu .el-menu-item {
  color: rgba(255,255,255,0.8) !important;
  border-bottom: none !important;
  height: 60px;
  line-height: 60px;
}
.header-menu .el-menu-item:hover,
.header-menu .el-menu-item.is-active {
  color: #fff !important;
  background: rgba(255,255,255,0.15) !important;
}

.header-right { display: flex; align-items: center; }

.user-info {
  color: #fff; cursor: pointer; display: flex; align-items: center; gap: 8px; font-size: 14px;
}

.role-tag { margin-left: 2px; }

.el-main {
  flex: 1;
  overflow-y: auto;
  background: #f0f2f5;
  padding: 24px;
}
</style>
