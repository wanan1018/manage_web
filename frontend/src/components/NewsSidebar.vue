<template>
  <el-card>
    <h3>最新通知</h3>
    <div v-for="item in newsList" :key="item.id" class="news-item">
      <p class="news-title">{{ item.title }}</p>
      <small>{{ item.time }}</small>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getNewsList } from '../api/news'

const newsList = ref<any[]>([])

onMounted(async () => {
  const res: any = await getNewsList()
  newsList.value = res.data || res
})
</script>

<style scoped>
.news-item {
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}
.news-title {
  margin: 0 0 4px;
}
</style>
