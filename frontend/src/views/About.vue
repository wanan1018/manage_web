<template>
  <div class="about-page">
    <el-card class="about-card">
      <h2>关于我们</h2>
      <el-divider />

      <div class="about-section">
        <h3>任务管理系统</h3>
        <p>本系统是一个基于 Spring Boot 3 + Vue 3 + Element Plus 构建的内部任务管理平台，旨在为团队提供高效的任务分配、跟踪与协作体验。</p>
      </div>

      <el-divider />

      <div class="about-section">
        <h3>技术栈</h3>
        <el-row :gutter="16" class="tech-row">
          <el-col :span="12">
            <el-card shadow="never" class="tech-card">
              <h4>后端</h4>
              <ul class="tech-list">
                <li>Java 17 + Spring Boot 3.2.x</li>
                <li>MyBatis-Plus 3.5.7</li>
                <li>MySQL 8.0</li>
                <li>JWT 认证（jjwt 0.11.5）</li>
                <li>Hutool 工具库</li>
              </ul>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="never" class="tech-card">
              <h4>前端</h4>
              <ul class="tech-list">
                <li>Vue 3 (Composition API)</li>
                <li>Vite 构建工具</li>
                <li>Element Plus UI 框架</li>
                <li>Axios 请求库</li>
                <li>Pinia 状态管理</li>
                <li>Vue Router 路由</li>
              </ul>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <el-divider />

      <div class="about-section">
        <h3>主要功能</h3>
        <el-row :gutter="16">
          <el-col v-for="feature in features" :key="feature.title" :span="8">
            <el-card shadow="hover" class="feature-card">
              <el-icon :size="28" :color="feature.color"><component :is="feature.icon" /></el-icon>
              <h4>{{ feature.title }}</h4>
              <p>{{ feature.desc }}</p>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <el-divider />

      <div class="about-section text-center">
        <h3>个人简历</h3>
        <p class="resume-desc">点击下方按钮下载我的个人简历（PDF）</p>
        <el-button type="primary" size="large" @click="downloadResume">
          <el-icon><Download /></el-icon> 下载简历
        </el-button>
      </div>

      <el-divider />

      <div class="about-section text-center">
        <p class="version-info">版本 v1.0.0 | 2026 全栈实习项目</p>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { List, DataAnalysis, User, ChatLineSquare, Download } from '@element-plus/icons-vue'

const features = [
  { title: '任务管理', desc: '创建、编辑、删除任务，支持状态流转（待办→进行中→已完成）和优先级分类', icon: List, color: '#409eff' },
  { title: '数据分析', desc: '仪表盘展示任务统计、完成率趋势、优先级分布图表', icon: DataAnalysis, color: '#67c23a' },
  { title: '成员管理', desc: '导师可查看所有实习生及其任务进度，支持按成员筛选和分配任务', icon: User, color: '#e6a23c' },
  { title: '实时资讯', desc: '获取最新技术资讯动态，支持搜索和浏览', icon: ChatLineSquare, color: '#f56c6c' },
]

function downloadResume() {
  window.open('/resume.pdf', '_blank')
}
</script>

<style scoped>
.about-page { max-width: 1000px; margin: 0 auto; }

.about-card { padding: 8px; }

.about-card h2 { text-align: center; font-size: 26px; font-weight: 700; color: #303133; }

.about-section { margin: 8px 0; }

.about-section h3 { font-size: 18px; font-weight: 600; color: #303133; margin-bottom: 12px; }

.about-section p { font-size: 14px; color: #606266; line-height: 1.8; }

.tech-row { gap: 0; }

.tech-card h4 { font-size: 16px; color: #409eff; margin-bottom: 8px; }

.tech-list { list-style: none; padding: 0; }

.tech-list li {
  padding: 4px 0;
  font-size: 14px;
  color: #606266;
  position: relative;
  padding-left: 16px;
}

.tech-list li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #409eff;
  font-weight: bold;
}

.feature-card {
  text-align: center;
  height: 140px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.feature-card h4 { font-size: 15px; font-weight: 600; margin: 0; }

.feature-card p { font-size: 12px; color: #909399; margin: 0; line-height: 1.5; }

.text-center { text-align: center; }

.version-info { font-size: 13px; color: #c0c4cc; }

.resume-desc { font-size: 14px; color: #909399; margin-bottom: 16px; }
</style>
