<script setup>
import { ref } from 'vue'

const tableData = ref([
  {
    id: 1,
    name: '张三',
    age: 25,
    email: 'zhangsan@example.com'
  },
  {
    id: 2,
    name: '李四',
    age: 30,
    email: 'lisi@example.com'
  },
  {
    id: 3,
    name: '王五',
    age: 28,
    email: 'wangwu@example.com'
  }
])
</script>

<template>
  <el-table
    :data="tableData"
    border
    style="width: 100%"
  >
    <el-table-column
      prop="id"
      label="ID"
      width="80"
    />

    <el-table-column
      prop="name"
      label="姓名"
    />

    <el-table-column
      prop="age"
      label="年龄"
      width="100"
    />

    <el-table-column
      prop="email"
      label="邮箱"
    />
  </el-table>
</template>

