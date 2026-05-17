<template>
  <el-card>
    <h3>{{ title }}</h3>
    <p>统计数据展示区域</p>
  </el-card>
</template>

<script setup lang="ts">
defineProps<{ title: string }>()
</script>
