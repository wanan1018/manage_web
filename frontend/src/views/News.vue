/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
<template>
  <div class="news-page">
    <div class="page-header">
      <h2>实时资讯</h2>
      <div class="header-actions">
        <el-input v-model="searchKeyword" placeholder="搜索资讯..." style="width:240px" clearable @input="onSearchInput">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button @click="fetchNews">
          <el-icon><Refresh /></el-icon> 刷新
        </el-button>
      </div>
    </div>

    <div v-if="loading" class="news-loading">
      <el-skeleton :rows="6" animated />
    </div>

    <div v-else-if="newsList.length === 0" class="news-empty">
      <el-empty description="暂无资讯">
        <el-button type="primary" @click="fetchNews">点击刷新</el-button>
      </el-empty>
    </div>

    <div v-else class="news-grid">
      <el-card v-for="(item, idx) in newsList" :key="idx" class="news-card" shadow="hover" :body-style="{ padding: 0 }">
        <div class="news-card-img-wrap">
          <img :src="getNewsImage(idx)" class="news-card-img" alt="" />
          <div class="news-card-overlay">
            <span class="news-source-badge">{{ item.source?.name || '资讯来源' }}</span>
          </div>
        </div>
        <div class="news-card-body">
          <a :href="item.url" target="_blank" class="news-link">
            <h3 class="news-title">{{ item.title }}</h3>
          </a>
          <p class="news-desc">{{ item.description || item.title }}</p>
          <div class="news-card-footer">
            <el-tag v-if="item.publishedAt" size="small" type="info">{{ formatTime(item.publishedAt) }}</el-tag>
            <el-button size="small" text type="primary" :href="item.url" target="_blank">
              阅读原文 <el-icon><Right /></el-icon>
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Refresh, Search, Right } from '@element-plus/icons-vue'
import { getNewsList } from '../api/news'

const newsList = ref<any[]>([])
const loading = ref(false)
const searchKeyword = ref('')

let searchTimer: ReturnType<typeof setTimeout> | null = null

const imagePool = [
  'https://picsum.photos/seed/tech1/400/200',
  'https://picsum.photos/seed/code2/400/200',
  'https://picsum.photos/seed/data3/400/200',
  'https://picsum.photos/seed/ai4/400/200',
  'https://picsum.photos/seed/web5/400/200',
  'https://picsum.photos/seed/cloud6/400/200',
  'https://picsum.photos/seed/design7/400/200',
  'https://picsum.photos/seed/robot8/400/200',
  'https://picsum.photos/seed/space9/400/200',
  'https://picsum.photos/seed/cyber10/400/200',
  'https://picsum.photos/seed/neural11/400/200',
  'https://picsum.photos/seed/block12/400/200',
  'https://picsum.photos/seed/pixel13/400/200',
  'https://picsum.photos/seed/quantum14/400/200',
  'https://picsum.photos/seed/vr15/400/200',
]

function getNewsImage(idx: number) {
  return imagePool[idx % imagePool.length]
}

function onSearchInput() {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => fetchNews(), 400)
}

async function fetchNews() {
  loading.value = true
  try {
    const params: any = {}
    if (searchKeyword.value) params.q = searchKeyword.value
    const res: any = await getNewsList(params)
    newsList.value = res.data || res || []
  } finally {
    loading.value = false
  }
}

function formatTime(dateStr: string) {
  if (!dateStr) return ''
  try {
    return new Date(dateStr).toLocaleString('zh-CN', {
      month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit',
    })
  } catch { return dateStr }
}

onMounted(fetchNews)
</script>

<style scoped>
.news-page { max-width: 1300px; margin: 0 auto; }

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.page-header h2 { font-size: 22px; font-weight: 600; }

.header-actions { display: flex; gap: 12px; align-items: center; }

.news-loading { padding: 40px 0; }

.news-empty { padding: 60px 0; text-align: center; }

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 20px;
}

.news-card {
  transition: transform 0.2s, box-shadow 0.2s;
  overflow: hidden;
  border-radius: 10px;
}
.news-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}

.news-card-img-wrap {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.news-card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s;
}
.news-card:hover .news-card-img { transform: scale(1.05); }

.news-card-overlay {
  position: absolute;
  top: 12px;
  left: 12px;
}

.news-source-badge {
  background: rgba(0,0,0,0.55);
  color: #fff;
  padding: 2px 10px;
  border-radius: 4px;
  font-size: 12px;
  backdrop-filter: blur(4px);
}

.news-card-body { padding: 16px; }

.news-link { text-decoration: none; color: inherit; }

.news-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  line-height: 1.5;
  margin: 0 0 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.news-title:hover { color: #409eff; }

.news-desc {
  font-size: 13px;
  color: #909399;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
