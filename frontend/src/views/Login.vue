/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
<template>
  <el-card class="login-card">
    <h2>登录</h2>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" type="password" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleLogin">登录</el-button>
        <el-button @click="handleRegister">注册</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { login, register } from '../api/auth'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()

const form = reactive({ username: '', password: '' })
const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  try {
    const res: any = await login(form)
    userStore.setToken(res.data.token)
    userStore.setUsername(res.data.username)
    userStore.setRole(res.data.role || 'INTERN')
    ElMessage.success('登录成功')
    router.push('/tasks')
  } catch {
    // error handled in interceptor
  }
}

async function handleRegister() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  try {
    await register(form)
    ElMessage.success('注册成功，请登录')
  } catch {
    // error handled in interceptor
  }
}
</script>

<style scoped>
.login-card {
  max-width: 420px;
  margin: 80px auto;
}
</style>
