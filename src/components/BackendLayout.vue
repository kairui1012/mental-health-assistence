<template>
    <div class="backend-layout">
        <el-container class="main-container">
            <Sidebar />
            <el-container>
                <el-header>
                    <Navbar class="navbar" />
                </el-header>
                <el-main class="main-container">
                    <router-view class="content-container"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Navbar from './Navbar.vue'
</script>

<style lang="scss" scoped>
.backend-layout {
    height: 100vh;
    .el-header {
        height: 71px !important;
    }
    .main-container {
        height: 100%;
    }
    .navbar {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 15px;
        background: white;
    }
    .main-content {
        flex: 1;
        .content-container {
            padding: 20px;
            background-color: #fff;
            min-height: calc(100% - 74px);
        }
    }
}
</style>
