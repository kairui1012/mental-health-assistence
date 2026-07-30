<template>
    <el-dialog 
        title="文章详情" 
        :model-value="dialogVisible" 
        width="50%" 
        @close="handleClose" 
        >
       <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
            <el-form-item label="文章标题" prop="title">
                <el-input v-model="formData.title" placeholder="请输入文章标题 " maxlength="200" show-word-limit clearable/>
                
            </el-form-item>
       </el-form>
    </el-dialog>
</template>

<script setup>
import { ref,reactive,computed } from 'vue';
const props = defineProps({
    modelValue:{ 
        type:Boolean,
        default:false
    }
})

const emit = defineEmits(['update:modelValue'])

const dialogVisible = computed({
    get(){
        return props.modelValue
    },
    set(val){
        emit('update:modelValue',val)
    }
})

const handleClose = () =>{

}

const formData = reactive({
    "title": "",
    "content": "",
    "coverImage": "",
    "categoryId": 0,
    "summary": "",
    "tags": "",
    "id": ""
})

const rules = reactive(
    {
        title:[
            {required:true, message:"请输入文本标题",trigger:'blur'},
            {max:200, message:"文本标题最多200字符",trigger:'blur'}
        ]
    }
)
</script>