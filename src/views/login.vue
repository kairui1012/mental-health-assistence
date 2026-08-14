<template>
    <div class="container">
        <div class="flex-box">
        </div>
        <div class="title">
            <div class="back-home">
                <el-icon>
                    <Back />
                </el-icon>
                <span>返回首页</span>
            </div>
            <div class="title-text">
                <h2>
                    登陆您的账户
                </h2>
                <p>
                    请输入您的登陆信息
                </p>
            </div>
        </div>
        <div class="form-container">
            <el-form ref="ruleFormRef" :model="formData" :rules="rules" label-position="top">
                <el-form-item label="用户名或邮箱" prop="username">
                    <el-input v-model="formData.username" size="large" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="formData.password" size="large" show-password type="password"
                        placeholder="请输入密码"></el-input>
                </el-form-item>
                <el-button class="btn" type="primary" size="large" @click="submitForm(ruleFormRef)">登陆</el-button>
            </el-form>
            <div class="footer">
                <p>还没有账户？<RouterLink to="/auth/register">去注册</RouterLink>
                </p>
            </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.container {
    width: 384px;

    .flex-box {
        display: flex;
        align-items: center;
    }

    .title {
        .back-home {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 60px;
            color: #6b7280;
            font-size: 14px;
            line-height: 1.5;
            cursor: pointer;
            transition: color 0.2s ease;

            :deep(.el-icon) {
                font-size: 16px;
            }

            &:hover {
                color: #4f46e5;
            }
        }

        .title-text {
            text-align: center;

            h2 {
                font-size: 36px;
                margin-bottom: 10px;
            }

            p {
                font-size: 18px;
                color: #6b7280;
            }
        }
    }

    .form-container {
        margin-top: 30px;

        .btn {
            margin-top: 40px;
            width: 100%;
        }

        .footer {
            padding: 30px;
            text-align: center;
        }
    }
}
</style>

<script setup>
import { reactive, ref } from 'vue';
import { login } from '@/api/admin';
import { RouterView, useRoute, useRouter } from 'vue-router';

const ruleFormRef = ref()

const formData = reactive({
    username: '',
    password: ''
})

const rules = reactive({
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
})

const router = useRouter()
const submitForm = async (formEl) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (valid) {
            login(formData).then((data) => {
                if (!data.token) {
                    return console.error('登陆失败')
                }
                else {
                    localStorage.setItem('token', data.token)
                    localStorage.setItem('userInfo', JSON.stringify(data.userInfo))
                }
                if (data.userInfo.userType === 2) {
                    router.push('/back/dashboard')
                }
                else {
                    router.push('/')
                }
            }
            );
        }
    })
}

</script>
