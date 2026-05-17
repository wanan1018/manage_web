/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.mount('#app')
