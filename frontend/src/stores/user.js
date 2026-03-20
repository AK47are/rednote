import { defineStore } from 'pinia'
import { getCurrApi } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: null,
    initialized: false,
  }),

  actions: {
    async init() {
      if (this.initialized) return

      try {
        const res = await getCurrApi()
        this.userId = res.data.userId
      } catch (e) {
        this.userId = null
        localStorage.removeItem('token')
      } finally {
        this.initialized = true
      }
    },
  },
})
