<template>
    <div class="container">
        <div class="title">
            <h2>
                创建您的账户
            </h2>
            <p>
                请填写注册信息
            </p>
        </div>
        <div class="form-container">
            <el-form label-position="top" :model="formData" :rules="rules" ref="formRef">
                <el-form-item label="用户名或邮箱" prop="username">
                    <el-input v-model="formData.username" placeholder="请输入用户名" size="large" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="formData.email" placeholder="请输入邮箱" size="large" />
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="formData.nickname" placeholder="请输入昵称(可选项)" size="large" />
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="formData.phone" placeholder="请输入手机号(可选项)" size="large" />
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="formData.password" placeholder="请输入密码" size="large" type="password"
                        show-password />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input v-model="formData.confirmPassword" placeholder="请再次输入密码" size="large" type="password"
                        show-password />
                </el-form-item>
                <el-form-item>
                    <el-button class="btn" type="primary" size="large" @click="submitForm(formRef)">注册</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { register } from '../api/frontend';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';


// 注册接口提交的数据模型
const formData = reactive({
    username: '',
    email: '',
    nickname: '',
    phone: '',
    password: '',
    confirmPassword: '',
    gender: 0,
    userType: 1 //普通用户是1
})

// 确认密码需与密码字段一致
const validateConfirmPassword = (rule, value, callback) => {
    if (!value) {
        callback(new Error('请再次输入密码'))
    } else if (value !== formData.password) {
        callback(new Error('两次输入的密码不一致'))
    } else {
        callback()
    }
}

// Element Plus 表单校验规则
const rules = reactive({
    // 用户名：必填，长度 3 至 20 个字符
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度为 3 至 20 个字符', trigger: 'blur' }
    ],
    // 邮箱：必填，且需符合邮箱格式
    email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
    ],
    // 密码：必填，长度 6 至 20 个字符
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度为 6 至 20 个字符', trigger: 'blur' }
    ],
    // 确认密码：必填，并校验两次输入是否一致
    confirmPassword: [
        { required: true, message: '请输入确认密码', validator: validateConfirmPassword, trigger: 'blur' }
    ],
})

// 注册成功后跳转到登录页
const router = useRouter()
const formRef = ref(null)

// 通过表单校验后提交注册信息
const submitForm = async (formEl) => {
    if (!formEl) return

    try {
        await formEl.validate()
        await register(formData)
        ElMessage.success('注册成功')
        router.push('/auth/login')
    } catch (error) {
        // 表单校验失败或接口请求失败时，由表单及请求拦截器提示错误。
    }
}
</script>

<style lang="scss" scoped>
.container {
    width: 384px;

    .flex-box {
        display: flex;
        align-items: center;
    }

    .title {
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
