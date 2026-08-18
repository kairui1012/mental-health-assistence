<template>
    <div>
        <div class="flex-box">
            <el-button @click="handleCollapse">
                <el-icon>
                    <Expand />
                </el-icon>
            </el-button>
            <p class="page-title">
                {{route.meta.title}}
            </p>
        </div>
        <div class="flex-box">
            <el-dropdown @command="handleCommand">
                <div class="flex-box user-menu">
                    <el-avatar
                        class="user-avatar"
                        src="https://static.wikia.nocookie.net/doraemon/images/d/d1/Doraemon_2005_Anime_Remake.png/revision/latest?cb=20241126194535&path-prefix=en"
                        :size="32"
                    />
                    <p class="user-name">admin</p>
                    <el-icon class="arrow-icon"><ArrowDown /></el-icon>
                </div>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item command="logout">
                            退出登录
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAdminStore } from '@/stores/admin';
import { useRouter,useRoute } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { logout } from '../api/admin';

const router = useRouter()
const route = useRoute()

const handleCollapse = ()=>
{
    useAdminStore().toggleCollapse()
}

const handleCommand = (command)=>
{
    if (command === 'logout')
    {
        ElMessageBox.confirm('确定退出登录吗?','提示',{
            confirmButtonText:'确定',
            cancelButtonText:'取消',
            type:'warning'
        }).then(async () => {
            try {
                await logout()
                localStorage.removeItem('token')
                localStorage.removeItem('userInfo')
                router.push('/auth/login')
            } catch (error) {
                ElMessage.error('退出登录失败，请稍后重试')
            }
        }).catch(() => {})
    }
}
</script>
<style lang="scss" >
.flex-box {
    display: flex;
    align-items: center;
    .page-title {
        margin-left: 20px;
        font-size: 26px;
        font-weight: bold;
        color: #1f2937;
    }
    .user-name {
        margin: 0 8px;
        font-weight: bold;
        font-size: 14px;
        color: #374151;
    }
    .user-menu {
        padding: 5px 8px;
        border-radius: 8px;
        cursor: pointer;
        transition: background-color 0.2s ease;

        &:hover {
            background-color: #f3f4f6;
        }
    }
    .arrow-icon {
        color: #9ca3af;
        font-size: 14px;
    }
}
</style>
