<template>
    <div>
        <PageHead title="情绪日志"></PageHead>
        <TableSearch class="knowledge-search" :formItem="formItem" @search="handleSearch"/>
        <el-table :data="data" style="width: 100%">
            <el-table-column prop="prop" label="label" width="width">
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import PageHead from '@/components/PageHead.vue'
import TableSearch from '@/components/TableSearch.vue'
import emotionalPage from '@/api/admin.js'
import { ref } from 'vue';

const formItem = [
    {comp:'input',prop='userId',label='用户ID',placeholder='请输入用户ID'},
    {omp:'select',prop='moodScreRange',label='情绪评分',placeholder='请选择情绪评分范围',options:[
        {label:'低分(1-3)',value:'1-3'},
        {label:'中分(4-6)',value:'4-6'},
        {label:'低分(7-10)',value:'7-10'}]}
]

const tableData = ref([])

const pagination = reactive({
    currentPage:1,
    size:10,
    total:0
})

const handleSearch = async () => {
    const params ={
        ...pagination,...formData
    } 

    const {records,total} = await emotionalPage(params)
    pagination.total = total
    tableData.value = records
}

onMounted(()=>{
    handleSearch()
})

</script>

        
