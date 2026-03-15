<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>用户登录</span>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%">登录</el-button>
        </el-form-item>

        <div class="register-link">
          还没有账号？
          <el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { loginApi } from '@/api/user'
import { ElMessage } from 'element-plus'

export default {
  name: 'LoginPage',
  data() {
    return {
      form: {
        username: '',
        password: '',
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '用户名长度需在 3 到 10 个字符之间', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度需在 6 到 20 个字符之间', trigger: 'blur' },
        ],
      },
    }
  },
  methods: {
    async handleLogin() {
      if (!this.$refs.formRef) return

      try {
        const valid = await this.$refs.formRef.validate()
        if (valid) {
          const res = await loginApi(this.form)
          ElMessage.success('登录成功')
          localStorage.setItem('token', res.data)
          this.$router.push('/')
        }
      } catch (e) {
        ElMessage.error(e.response?.data?.msg || '登录失败，请稍后重试')
      }
    },
  },
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.login-card {
  width: 450px;
}

.card-header {
  font-weight: bold;
  font-size: 25px;
  text-align: center;
}

.register-link {
  text-align: center;
  margin-top: 10px;
}
</style>
