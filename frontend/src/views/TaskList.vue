<template>
  <div class="task-list-page">
    <div class="page-header">
      <h2>任务管理</h2>
      <div class="header-actions">
        <el-select v-if="isTutor" v-model="filterAssignee" placeholder="按成员筛选" clearable style="width:150px" @change="fetchTasks">
          <el-option v-for="u in userList" :key="u.id" :label="u.username" :value="u.id" />
        </el-select>
        <el-select v-model="filterStatus" placeholder="按状态筛选" clearable style="width:140px" @change="fetchTasks">
          <el-option label="待办" value="TODO" />
          <el-option label="进行中" value="IN_PROGRESS" />
          <el-option label="已完成" value="DONE" />
        </el-select>
        <el-button type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon> 新建任务
        </el-button>
        <el-button @click="fetchTasks">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
    </div>

    <el-card>
      <el-table :data="tasks" stripe v-loading="loading" empty-text="暂无任务">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" min-width="160">
          <template #default="{ row }">
            <span :style="row.status === 'DONE' ? 'text-decoration:line-through;color:#999' : ''">
              {{ row.title }}
            </span>
          </template>
        </el-table-column>
        <el-table-column v-if="isTutor" label="负责人" width="100">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ row.assigneeName || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="优先级" width="90">
          <template #default="{ row }">
            <el-tag :type="priorityType(row.priority)" size="small">{{ priorityLabel(row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="200">
          <template #default="{ row }">
            <el-button-group>
              <el-button size="small" :type="row.status === 'TODO' ? 'info' : 'default'" :disabled="row.status === 'TODO'" @click="changeStatus(row, 'TODO')">待办</el-button>
              <el-button size="small" :type="row.status === 'IN_PROGRESS' ? 'warning' : 'default'" :disabled="row.status === 'IN_PROGRESS'" @click="changeStatus(row, 'IN_PROGRESS')">进行中</el-button>
              <el-button size="small" :type="row.status === 'DONE' ? 'success' : 'default'" :disabled="row.status === 'DONE'" @click="changeStatus(row, 'DONE')">完成</el-button>
            </el-button-group>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="截止日期" width="120">
          <template #default="{ row }">
            <span :style="isOverdue(row.dueDate) ? 'color:red' : ''">{{ row.dueDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <TaskFormDialog v-model:visible="dialogVisible" :task-data="editingTask" @saved="fetchTasks" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh } from '@element-plus/icons-vue'
import { getTaskList, updateTask, deleteTask } from '../api/task'
import { getUserList } from '../api/user'
import { useUserStore } from '../store/user'
import TaskFormDialog from '../components/TaskFormDialog.vue'

const userStore = useUserStore()
const tasks = ref<any[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const editingTask = ref<any>(null)
const filterStatus = ref('')
const filterAssignee = ref<number | null>(null)
const userList = ref<any[]>([])
const isTutor = computed(() => userStore.isTutor)

function priorityType(val: string) { return val === 'HIGH' ? 'danger' : val === 'MEDIUM' ? 'warning' : 'info' }
function priorityLabel(val: string) { return val === 'HIGH' ? '高' : val === 'MEDIUM' ? '中' : '低' }

function isOverdue(dueDate: string) {
  if (!dueDate) return false
  return new Date(dueDate) < new Date(new Date().toDateString())
}

async function fetchTasks() {
  loading.value = true
  try {
    const params: any = {}
    if (filterStatus.value) params.status = filterStatus.value
    if (isTutor.value && filterAssignee.value) params.assigneeId = filterAssignee.value
    const res: any = await getTaskList(params)
    const rawList = res.data || res || []

    if (isTutor.value && rawList.length > 0) {
      const userMap: Record<number, string> = {}
      userList.value.forEach((u: any) => { userMap[u.id] = u.username })
      tasks.value = rawList.map((t: any) => ({
        ...t,
        assigneeName: userMap[t.assigneeId] || `ID:${t.assigneeId}`,
      }))
    } else {
      tasks.value = rawList
    }
  } finally { loading.value = false }
}

async function fetchUsers() {
  const res: any = await getUserList()
  userList.value = res.data || res || []
}

async function changeStatus(row: any, status: string) {
  await updateTask(row.id, { status })
  ElMessage.success('状态已更新')
  fetchTasks()
}

function openCreate() {
  editingTask.value = null
  dialogVisible.value = true
}

function openEdit(row: any) {
  editingTask.value = { ...row }
  dialogVisible.value = true
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('确认删除该任务？', '提示', { type: 'warning' })
  await deleteTask(id)
  ElMessage.success('删除成功')
  fetchTasks()
}

onMounted(() => {
  if (isTutor.value) fetchUsers()
  fetchTasks()
})
</script>

<style scoped>
.task-list-page { max-width: 1200px; margin: 0 auto; }

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.page-header h2 { font-size: 22px; font-weight: 600; }

.header-actions { display: flex; gap: 12px; align-items: center; }
</style>
