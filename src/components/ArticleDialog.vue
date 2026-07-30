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
            <el-form-item label="所属分类" prop="category">
                <el-select v-model="formData.categoryId" placeholder="请选择分类">
                    <el-option v-for="item in props.categories" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="文章摘要" prop="summary">
                <el-input type="textarea" v-model="formData.summary" placeholder="请输入文章摘要(可选)" maxlength="1000" show-word-limit :rows="4"></el-input>
            </el-form-item>
            <el-form-item label="标签" prop="tags">
                <el-select v-model="formData.tagArray" placeholder="请输入文章标签" multiple filterable allow-create style="width: 100%;">
                    <el-option v-for="item in commonTags" :label="item" :value="item" :key="item"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="封面图片">
                <div class="cove-upload">
                    <el-upload 
                        class="avatar-uploader"
                        action="#" 
                        :before-upload="beforeUpload"
                        :http-request="handleUploadRequest"
                        :show-file-list="false"
                        accept="image/*"
                        :file-list="[{name: 'name', url: 'url'}]">
                        <div v-if="!imgUrl"  class="cover-placerholder">
                            <p>点击上传封面</p>
                        </div>
                        <img v-else :src="imgUrl" class="cover-image" alt="封面图片">
                    </el-upload>
                    <div v-if="imgUrl" class="cover-remove">
                        <el-button type="danger" @click="handleRemove" size="mini">
                            移除封面
                        </el-button>
                    </div>
                </div>
            </el-form-item>
       </el-form>
    </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus';
import { ref,reactive,computed } from 'vue';
import { uploadFile } from '../api/admin';
import { fileBaseUrl } from '../config';

const props = defineProps({
    modelValue:{ 
        type:Boolean,
        default:false
    },
    categories:{
        type:Array,
        default:()=>[]
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
    "categoryId": 1,
    "summary": "",
    "tags": "",
    "id": ""
})

const rules = reactive(
    {
        title:[
            {required:true, message:"请输入文本标题",trigger:'blur'},
            {max:200, message:"文本标题最多200字符",trigger:'blur'}
        ],
        categoryId:[
            {required:true, message:"请选择分类",trigger:'change'}
        ],
    }
)

const commonTags = [
  '情绪管理', '焦虑', '抑郁', '压力', '睡眠', 
  '冥想', '正念', '放松', '心理健康', '自我成长',
  '人际关系', '工作压力', '学习方法', '生活技巧'
]

const imgUrl = ref('')

const beforeUpload = (file) => 
{
    const isImage = file.type.startsWith('image/')
    const isLt5M = file.size /1024 /1012 <5
    if(!isImage)
    {
        ElMessage.error('上传图片失败，请检查图片格式')
        return false
    }
    if(!isLt5M)
    {
        ElMessage.error('上传图片失败，图片大小不能超过5MB')
        return false
    }
    return true
}

const handleUploadRequest = async ({file}) =>
{
    //uuid generate
    const businessId = crypto.randomUUID()

    const fileRes = await uploadFile(file,{
        businessId:businessId
    })

    //combine the fully image address
    imgUrl.value = fileBaseUrl + fileRes.filePath
    formData.coverImage = fileRes.filePath
}

const handleRemove = () =>
{
    imgUrl.value=''
    formData.coverImage=''
}
</script>

<style lang="scss" scoped>
.cover-placerholder{
    width: 200px;
    height: 120px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #8b949e;
    background: #f6f8fa;
}
.cover-image{
    width: 200px;
    height: 120px;
    display: block;
}
</style>