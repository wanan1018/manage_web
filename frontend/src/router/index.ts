/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import TaskList from '../views/TaskList.vue'
import Dashboard from '../views/Dashboard.vue'
import Members from '../views/Members.vue'
import News from '../views/News.vue'
import About from '../views/About.vue'

const routes = [
  { path: '/', redirect: '/tasks' },
  { path: '/login', name: 'Login', component: Login, meta: { noAuth: true } },
  { path: '/tasks', name: 'TaskList', component: TaskList },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/members', name: 'Members', component: Members, meta: { role: 'TUTOR' } },
  { path: '/news', name: 'News', component: News },
  { path: '/about', name: 'About', component: About },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  if (!to.meta.noAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/tasks')
  } else if (to.meta.role && to.meta.role !== role) {
    next('/tasks')
  } else {
    next()
  }
})

export default router
