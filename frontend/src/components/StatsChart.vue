/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
<template>
  <el-card>
    <h3>{{ title }}</h3>
    <p>统计数据展示区域</p>
  </el-card>
</template>

<script setup lang="ts">
defineProps<{ title: string }>()
</script>
