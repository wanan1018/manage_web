/**
 * @project   task-management-system
 * @module    frontend
 * @author    wanan1018
 * @github    https://github.com/wanan1018/manage_web
 * @date      2026-05-17
 */
export function formatDate(date: string | number | Date, fmt = 'YYYY-MM-DD HH:mm:ss') {
  const d = new Date(date)
  const o: Record<string, number> = {
    'Y+': d.getFullYear(),
    'M+': d.getMonth() + 1,
    'D+': d.getDate(),
    'H+': d.getHours(),
    'm+': d.getMinutes(),
    's+': d.getSeconds(),
  }
  let result = fmt
  for (const [k, v] of Object.entries(o)) {
    result = result.replace(new RegExp(k, 'g'), String(v).padStart(2, '0'))
  }
  return result
}
