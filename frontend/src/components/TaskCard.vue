<template>
  <el-card class="task-card" shadow="hover">
    <h3>{{ task.title }}</h3>
    <p>{{ task.description }}</p>
    <el-tag :type="task.status === 'done' ? 'success' : 'warning'" size="small">
      {{ task.status === 'done' ? '已完成' : '进行中' }}
    </el-tag>
  </el-card>
</template>

<script setup lang="ts">
defineProps<{ task: any }>()
</script>

<style scoped>
.task-card {
  margin-bottom: 12px;
}
</style>
