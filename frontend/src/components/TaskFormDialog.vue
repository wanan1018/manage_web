/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑任务' : '新建任务'"
    width="520px"
    @update:model-value="$emit('update:visible', $event)"
    @closed="resetForm"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入任务标题" />
      </el-form-item>
      <el-form-item v-if="isTutor && !isEdit" label="负责人">
        <el-select v-model="form.assigneeId" placeholder="请选择负责人" style="width:100%">
          <el-option v-for="u in userList" :key="u.id" :label="u.username + (u.role === 'TUTOR' ? '(导师)' : '')" :value="u.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入任务描述" />
      </el-form-item>
      <el-form-item label="优先级">
        <el-select v-model="form.priority" style="width:100%">
          <el-option label="低" value="LOW" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="高" value="HIGH" />
        </el-select>
      </el-form-item>
      <el-form-item label="截止日期">
        <el-date-picker
          v-model="form.dueDate"
          type="date"
          style="width:100%"
          placeholder="请选择截止日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item v-if="isEdit" label="状态">
        <el-select v-model="form.status" style="width:100%">
          <el-option label="待办" value="TODO" />
          <el-option label="进行中" value="IN_PROGRESS" />
          <el-option label="已完成" value="DONE" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取消</el-button>
      <el-button type="primary" @click="handleSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { createTask, updateTask } from '../api/task'
import { getUserList } from '../api/user'
import { useUserStore } from '../store/user'

const userStore = useUserStore()
const props = defineProps<{
  visible: boolean
  taskData?: any
}>()

const emit = defineEmits<{
  'update:visible': [v: boolean]
  saved: []
}>()

const isEdit = ref(false)
const userList = ref<any[]>([])
const isTutor = computed(() => userStore.isTutor)

const formRef = ref<FormInstance>()
const form = reactive({
  title: '',
  description: '',
  priority: 'MEDIUM',
  status: '',
  dueDate: '',
  assigneeId: null as number | null,
})

const rules: FormRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
}

onMounted(async () => {
  if (isTutor.value) {
    const res: any = await getUserList()
    userList.value = res.data || res || []
  }
})

watch(
  () => props.visible,
  (val) => {
    if (!val) return
    if (props.taskData && props.taskData.id) {
      isEdit.value = true
      form.title = props.taskData.title || ''
      form.description = props.taskData.description || ''
      form.priority = props.taskData.priority || 'MEDIUM'
      form.status = props.taskData.status || 'TODO'
      form.dueDate = props.taskData.dueDate || ''
      form.assigneeId = props.taskData.assigneeId || null
    } else {
      isEdit.value = false
      resetForm()
    }
  },
)

async function handleSave() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  const payload: any = {
    title: form.title,
    description: form.description,
    priority: form.priority,
    dueDate: form.dueDate || null,
  }

  if (form.assigneeId) payload.assigneeId = form.assigneeId

  if (isEdit.value) {
    payload.status = form.status
    await updateTask(props.taskData.id, payload)
  } else {
    await createTask(payload)
  }

  ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
  emit('saved')
  emit('update:visible', false)
}

function resetForm() {
  form.title = ''
  form.description = ''
  form.priority = 'MEDIUM'
  form.status = ''
  form.dueDate = ''
  form.assigneeId = null
  formRef.value?.resetFields()
}
</script>
