import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/components/Layout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Layout,
      children: [
        {
          path: '/profiles/:userId(\\d+)',
          name: 'profiles',
          component: () => import('@/views/ProfileView.vue'),
        },
        {
          path: '/settings',
          name: 'settings',
          component: () => import('@/views/UserSettingView.vue'),
        },
      ],
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
    },
  ],
})

export default router
