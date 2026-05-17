import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const role = ref(localStorage.getItem('role') || 'INTERN')

  const isTutor = computed(() => role.value === 'TUTOR')

  function setToken(val: string) {
    token.value = val
    localStorage.setItem('token', val)
  }

  function setUsername(val: string) {
    username.value = val
    localStorage.setItem('username', val)
  }

  function setRole(val: string) {
    role.value = val
    localStorage.setItem('role', val)
  }

  function logout() {
    token.value = ''
    username.value = ''
    role.value = 'INTERN'
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('role')
  }

  return { token, username, role, isTutor, setToken, setUsername, setRole, logout }
})
