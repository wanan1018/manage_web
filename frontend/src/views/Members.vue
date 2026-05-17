/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
<template>
  <div class="members-page">
    <h2>成员管理</h2>
    <el-card>
      <el-table :data="members" v-loading="loading" empty-text="暂无成员">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 'TUTOR' ? 'danger' : 'success'" size="small">
              {{ row.role === 'TUTOR' ? '导师' : '实习生' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="任务数" width="80">
          <template #default="{ row }">
            <el-badge :value="memberTaskCount(row.id)" type="primary" :hidden="memberTaskCount(row.id) === 0" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" @click="viewTasks(row)">查看任务</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="'任务列表 - ' + selectedMember?.username" width="700px">
      <el-table :data="memberTasks" v-loading="taskLoading" empty-text="暂无任务" size="small">
        <el-table-column prop="id" label="ID" width="50" />
        <el-table-column prop="title" label="标题" min-width="140">
          <template #default="{ row: t }">
            <span :style="t.status === 'DONE' ? 'text-decoration:line-through;color:#999' : ''">{{ t.title }}</span>
          </template>
        </el-table-column>
        <el-table-column label="优先级" width="80">
          <template #default="{ row: t }">
            <el-tag :type="priorityType(t.priority)" size="small">{{ priorityLabel(t.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="200">
          <template #default="{ row: t }">
            <el-button-group>
              <el-button size="small" :type="t.status === 'TODO' ? 'info' : 'default'" :disabled="t.status === 'TODO'" @click="changeMemberTaskStatus(t, 'TODO')">待办</el-button>
              <el-button size="small" :type="t.status === 'IN_PROGRESS' ? 'warning' : 'default'" :disabled="t.status === 'IN_PROGRESS'" @click="changeMemberTaskStatus(t, 'IN_PROGRESS')">进行中</el-button>
              <el-button size="small" :type="t.status === 'DONE' ? 'success' : 'default'" :disabled="t.status === 'DONE'" @click="changeMemberTaskStatus(t, 'DONE')">完成</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserList } from '../api/user'
import { getTaskList, updateTask } from '../api/task'

const loading = ref(false)
const taskLoading = ref(false)
const members = ref<any[]>([])
const dialogVisible = ref(false)
const selectedMember = ref<any>(null)
const memberTasks = ref<any[]>([])

function priorityType(val: string) { return val === 'HIGH' ? 'danger' : val === 'MEDIUM' ? 'warning' : 'info' }
function priorityLabel(val: string) { return val === 'HIGH' ? '高' : val === 'MEDIUM' ? '中' : '低' }

async function fetchMembers() {
  loading.value = true
  try {
    const res: any = await getUserList()
    members.value = res.data || res || []
  } finally { loading.value = false }
}

async function viewTasks(row: any) {
  selectedMember.value = row
  dialogVisible.value = true
  taskLoading.value = true
  try {
    const res: any = await getTaskList({ assigneeId: row.id })
    memberTasks.value = res.data || res || []
  } finally { taskLoading.value = false }
}

async function changeMemberTaskStatus(task: any, status: string) {
  await updateTask(task.id, { status })
  ElMessage.success('状态已更新')
  if (selectedMember.value) viewTasks(selectedMember.value)
}

function memberTaskCount(_userId: number) {
  return 0
}

onMounted(fetchMembers)
</script>

<style scoped>
.members-page { max-width: 1200px; margin: 0 auto; }
.members-page h2 { font-size: 22px; font-weight: 600; margin-bottom: 20px; }
</style>
