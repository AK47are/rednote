<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <span>用户注册</span>
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
          <el-button type="primary" @click="handleRegister" style="width: 100%">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { registerApi } from '@/api/user.js'
import { ElMessage } from 'element-plus'
export default {
  name: 'RegisterPage',
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
    async handleRegister() {
      if (!this.$refs.formRef) return

      try {
        const valid = await this.$refs.formRef.validate()
        if (valid) {
          await registerApi(this.form)
          ElMessage.success('注册成功，即将跳转登录...')
          // this.$router.push('/login')
        }
      } catch (e) {
        ElMessage.error(e.response?.data?.msg || '注册失败，请稍后重试')
      }
    },
  },
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.register-card {
  width: 450px;
}

.card-header {
  font-weight: bold;
  font-size: 25px;
  text-align: center;
}
</style>
