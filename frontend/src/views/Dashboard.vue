/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
<template>
  <div class="dashboard-page">
    <h2>数据仪表盘</h2>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-todo">
          <div class="stat-icon"><el-icon :size="36"><List /></el-icon></div>
          <div class="stat-body">
            <div class="stat-num">{{ counts.TODO }}</div>
            <div class="stat-label">待办任务</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-progress">
          <div class="stat-icon"><el-icon :size="36"><Loading /></el-icon></div>
          <div class="stat-body">
            <div class="stat-num">{{ counts.IN_PROGRESS }}</div>
            <div class="stat-label">进行中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-done">
          <div class="stat-icon"><el-icon :size="36"><CircleCheck /></el-icon></div>
          <div class="stat-body">
            <div class="stat-num">{{ counts.DONE }}</div>
            <div class="stat-label">已完成</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card stat-total">
          <div class="stat-icon"><el-icon :size="36"><TrendCharts /></el-icon></div>
          <div class="stat-body">
            <div class="stat-num">{{ totalTasks }}</div>
            <div class="stat-label">全部任务</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="16">
        <el-card>
          <template #header>
            <span class="card-header-title">完成率趋势</span>
            <el-button size="small" @click="fetchData">
              <el-icon><Refresh /></el-icon> 刷新
            </el-button>
          </template>
          <div class="progress-section">
            <el-progress
              type="dashboard"
              :percentage="completionRate"
              :color="colors"
            >
              <span class="progress-text">{{ completionRate }}%</span>
            </el-progress>
            <div class="progress-hint">任务完成率 = 已完成 / 全部任务</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="quick-link-card">
          <template #header><span class="card-header-title">快捷入口</span></template>
          <div class="quick-links">
            <el-button type="primary" @click="router.push('/tasks')">
              <el-icon><Plus /></el-icon> 新建任务
            </el-button>
            <el-button @click="router.push('/news')">
              <el-icon><ChatLineSquare /></el-icon> 浏览资讯
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="24">
        <el-card>
          <template #header><span class="card-header-title">优先级分布</span></template>
          <div class="priority-chart">
            <div class="priority-bar">
              <div class="bar-label">高</div>
              <div class="bar-track">
                <div class="bar-fill high" :style="{ width: priorityPercent('HIGH') + '%' }"></div>
              </div>
              <div class="bar-num">{{ countsByPriority.HIGH }}</div>
            </div>
            <div class="priority-bar">
              <div class="bar-label">中</div>
              <div class="bar-track">
                <div class="bar-fill medium" :style="{ width: priorityPercent('MEDIUM') + '%' }"></div>
              </div>
              <div class="bar-num">{{ countsByPriority.MEDIUM }}</div>
            </div>
            <div class="priority-bar">
              <div class="bar-label">低</div>
              <div class="bar-track">
                <div class="bar-fill low" :style="{ width: priorityPercent('LOW') + '%' }"></div>
              </div>
              <div class="bar-num">{{ countsByPriority.LOW }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { List, Loading, CircleCheck, TrendCharts, Plus, ChatLineSquare } from '@element-plus/icons-vue'
import { getTaskList } from '../api/task'

const router = useRouter()
const tasks = ref<any[]>([])

const counts = computed(() => {
  const c: Record<string, number> = { TODO: 0, IN_PROGRESS: 0, DONE: 0 }
  tasks.value.forEach((t) => {
    if (c[t.status] !== undefined) c[t.status]++
  })
  return c
})

const totalTasks = computed(() => tasks.value.length)

const completionRate = computed(() => {
  if (totalTasks.value === 0) return 0
  return Math.round((counts.value.DONE / totalTasks.value) * 100)
})

const countsByPriority = computed(() => {
  const c: Record<string, number> = { HIGH: 0, MEDIUM: 0, LOW: 0 }
  tasks.value.forEach((t) => {
    const p = t.priority || 'MEDIUM'
    if (c[p] !== undefined) c[p]++
  })
  return c
})

const maxPriority = computed(() => Math.max(countsByPriority.value.HIGH, countsByPriority.value.MEDIUM, countsByPriority.value.LOW, 1))

function priorityPercent(key: string) {
  return Math.round((countsByPriority.value[key] / maxPriority.value) * 100)
}

const colors = [
  { color: '#f56c6c', percentage: 20 },
  { color: '#e6a23c', percentage: 50 },
  { color: '#67c23a', percentage: 100 },
]

async function fetchData() {
  const res: any = await getTaskList()
  tasks.value = res.data || res || []
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.dashboard-page { max-width: 1200px; margin: 0 auto; }
.dashboard-page h2 { font-size: 22px; font-weight: 600; margin-bottom: 20px; }

.stats-row { margin-bottom: 0; }

.stat-card { display: flex; align-items: center; padding: 8px; }
.stat-card .el-card__body { display: flex; align-items: center; gap: 14px; width: 100%; }
.stat-icon { width: 56px; height: 56px; border-radius: 12px; display: flex; align-items: center; justify-content: center; color: #fff; }
.stat-todo .stat-icon { background: linear-gradient(135deg, #909399, #606266); }
.stat-progress .stat-icon { background: linear-gradient(135deg, #e6a23c, #f5a623); }
.stat-done .stat-icon { background: linear-gradient(135deg, #67c23a, #85ce61); }
.stat-total .stat-icon { background: linear-gradient(135deg, #409eff, #79bbff); }

.stat-num { font-size: 28px; font-weight: 700; line-height: 1.2; }
.stat-label { font-size: 13px; color: #909399; }

.card-header-title { font-weight: 600; }

.progress-section { text-align: center; padding: 20px 0; }
.progress-text { font-size: 22px; font-weight: 700; }
.progress-hint { margin-top: 12px; color: #909399; font-size: 13px; }

.priority-chart { padding: 10px 0; }
.priority-bar { display: flex; align-items: center; gap: 12px; margin-bottom: 16px; }
.priority-bar:last-child { margin-bottom: 0; }
.bar-label { width: 30px; font-weight: 600; color: #606266; }
.bar-track { flex: 1; height: 22px; background: #f0f2f5; border-radius: 11px; overflow: hidden; }
.bar-fill { height: 100%; border-radius: 11px; transition: width 0.5s; }
.bar-fill.high { background: linear-gradient(90deg, #f56c6c, #e6a23c); }
.bar-fill.medium { background: linear-gradient(90deg, #e6a23c, #f5a623); }
.bar-fill.low { background: linear-gradient(90deg, #67c23a, #85ce61); }
.bar-num { width: 30px; text-align: right; font-weight: 600; color: #303133; }

.quick-link-card .el-card__body { padding: 20px; }
.quick-links { display: flex; flex-direction: column; gap: 12px; }
.quick-links .el-button { width: 100%; justify-content: center; }
</style>
