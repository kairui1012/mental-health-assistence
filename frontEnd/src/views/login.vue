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
                color: #689f38;
            }
        }

        .title-text {
            text-align: center;

            h2 {
                margin: 0;
                font-size: 36px;
                font-weight: 700;
                letter-spacing: -.04em;
                margin-bottom: 10px;
                color: #1d1d1f;
            }

            p {
                font-size: 18px;
                letter-spacing: -.02em;
                color: #6b7280;
            }
        }
    }

    .form-container {
        margin-top: 30px;

        :deep(.el-form-item) {
            margin-bottom: 22px;
        }

        :deep(.el-form-item__label) {
            padding-bottom: 8px;
            color: #1d1d1f;
            font-size: 14px;
            font-weight: 600;
            line-height: 1.4;
        }

        :deep(.el-input) {
            --el-input-focus-border-color: #a8cf48;
            --el-input-focus-color: #a8cf48;
            --el-input-hover-border-color: #c7df7a;
        }

        :deep(.el-input__wrapper) {
            min-height: 50px;
            padding: 1px 16px;
            border: 1px solid transparent;
            border-radius: 14px;
            background: #f5f5f7;
            box-shadow: none;
            transition: border-color .2s ease, background-color .2s ease, box-shadow .2s ease;

            &:hover {
                background: #fff;
                border-color: #c7df7a;
            }

            &.is-focus {
                background: #fff;
                border-color: #a8cf48;
                box-shadow: none;
            }
        }

        :deep(.el-input.is-active .el-input__wrapper) {
            box-shadow: none !important;
        }

        :deep(.el-input__inner) {
            color: #1d1d1f;
            font-size: 16px;

            &::placeholder {
                color: #86868b;
            }
        }

        :deep(.el-input__inner:-webkit-autofill),
        :deep(.el-input__inner:-webkit-autofill:hover),
        :deep(.el-input__inner:-webkit-autofill:focus) {
            -webkit-box-shadow: 0 0 0 1000px #f5f5f7 inset !important;
            -webkit-text-fill-color: #1d1d1f !important;
            caret-color: #1d1d1f;
        }

        :deep(.el-input.is-active .el-input__inner:-webkit-autofill) {
            -webkit-box-shadow: 0 0 0 1000px #fff inset !important;
        }

        .btn {
            margin-top: 40px;
            width: 100%;
            min-height: 52px;
            border: 0;
            border-radius: 14px;
            font-size: 17px;
            font-weight: 600;
            letter-spacing: -.01em;
            color: #fff;
            background: #a8cf48;
            box-shadow: 0 8px 18px rgba(139, 195, 74, .16);
            transition: transform .2s ease, background-color .2s ease, box-shadow .2s ease;

            &:hover {
                color: #fff;
                background: #9bc545;
                box-shadow: 0 10px 22px rgba(139, 195, 74, .22);
                transform: translateY(-1px);
            }

            &:active {
                transform: translateY(0);
                box-shadow: 0 5px 12px rgba(139, 195, 74, .16);
            }
        }

        .footer {
            padding: 30px;
            text-align: center;

            p {
                color: #6e6e73;
                font-size: 14px;
            }

            :deep(a) {
                color: #689f38;
                font-weight: 600;

                &:hover {
                    color: #4f7d2a;
                }
            }
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
