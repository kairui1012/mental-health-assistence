
<template>
    <el-aside width="200px">
        <el-menu
        default-active="2"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        >
        <el-menu-item v-for="item in router.options.routes[0].children" :key="item.path" :index="item.path">
            <el-icon><location /></el-icon>
            <span>Navigator One</span>
        </el-menu-item>
        </el-menu>
    </el-aside>
    
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>
