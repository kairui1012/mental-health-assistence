<template>
    <el-form :model="formData">
        <template v-for="item in formItem" :key="item.prop">
            <el-form-item :label="item.label" :prop="item.prop">
                <component v-model="formData[item.prop]" :is="isComp(item.comp)" :placeholder="item.placeholder">
                    <template v-if="item.comp === 'select'">
                        <el-option key="all" label="全部" value="" />
                        <el-option
                        v-for="option in item.options || []"
                        :key="option.value"
                        :label="option.label"
                        :value="option.value"
                        />
                    </template>
                    <el-button type="primary" @click="handleSearch">
                        查询
                    </el-button>
                    <el-button type="second" @click="handleReset">
                        重置
                    </el-button>
                </component>
            </el-form-item>
        </template>
    </el-form>
</template>
<script setup>
import { reactive,ref } from 'vue'
const formData = reactive({
})

const props = defineProps({
    formItem:{
        type: Array,
        default: () => []
    }
})
const emit = defineEmits(['search'])
const isComp = (comp) => {
    return{
        input: 'ElInput',
        select: 'ElSelect'
    }[comp]
}
const handleSearch = () => {
    emit('search',formData)
}
const handleReset = () => {

}
</script>
