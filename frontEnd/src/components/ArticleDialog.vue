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
            <el-form-item label="所属分类" prop="categoryId">
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
            <el-form-item label="文章内容" prop="content">
                <RichTextEditor 
                    v-model="formData.content"
                    placeholder="请输入文章内容，支持富文本格式\n\n可以使用加粗、斜体、列表、标题等格式来丰富文章内容。"
                    :maxCharCount="5000"
                    @change="handleContentChange"
                    @created="handleEditorChange"
                    min-height="400px"
                />
            </el-form-item>
       </el-form>
       <div v-if="btnPreview">
            <h3>内容预览</h3>
            <div v-html="formData.content"></div>
       </div>
       <template #footer>
        <div>
            <el-button @click="btnPreview = !btnPreview">{{btnPreview?'隐藏预览':'预览效果'}}</el-button>
            <el-button @click="handleClose">取消</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="loading"> {{isEdit?'更新文章':'编辑文章'}}</el-button>
        </div>
       </template>
    </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus';
import { ref,reactive,computed,nextTick,watch, onMounted } from 'vue';
import { createArticle, updateArticle, uploadFile } from '../api/admin';
import { fileBaseUrl } from '../config';
import RichTextEditor from './RichTextEditor.vue';

const props = defineProps({
    modelValue:{ 
        type:Boolean,
        default:false
    },
    categories:{
        type:Array,
        default:()=>[]
    },
    article:{
        type:Object,
        default:null
    }
})

const emit = defineEmits(['update:modelValue','success'])

const isEdit = computed(()=>!!props.article?.id)

const dialogVisible = computed({
    get(){
        return props.modelValue
    },
    set(val){
        emit('update:modelValue',val)
    }
})

const handleClose = () => {
    formRef.value.resetFields()
    businessId.value = null
    handleRemove()
    formData.tagArray = []
    emit('update:modelValue',false)
}

const formData = reactive({
    "title": "",
    "content": "",
    "coverImage": "",
    "categoryId": 1,
    "summary": "",
    "tags": "",
    "tagArray": [],
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
        content:[
            {required:true, message:"请输入文章内容",trigger:'change'},
            {max:5000, message:"文章内容最多5000字符",trigger:'change'}
        ]
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
    const isLt5M = file.size / 1024 / 1024 < 5
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

const businessId = ref(null)

watch(() => props.article, (newVal) => {
    if (newVal) {
        nextTick(()=>{
            Object.assign(formData, newVal, {
                tagArray: newVal.tags ? newVal.tags.split(',').filter(Boolean) : []
            })
            businessId.value = newVal.id
            imgUrl.value = newVal.coverImage
                ? `${fileBaseUrl.replace(/\/$/, '')}/${newVal.coverImage.replace(/^\//, '')}`
                : ''
        })
    } else {
        Object.assign(formData, {
            title: '', content: '', coverImage: '', categoryId: 1,
            summary: '', tags: '', tagArray: [], id: ''
        })
        businessId.value = null
        imgUrl.value = ''
    }
})

const handleUploadRequest = async ({file}) =>
{
    //uuid generate
    businessId.value = crypto.randomUUID()

    try {
        const fileRes = await uploadFile(file, {
            businessId: businessId.value
        })

        imgUrl.value = `${fileBaseUrl.replace(/\/$/, '')}/${fileRes.filePath.replace(/^\//, '')}`
        formData.coverImage = fileRes.filePath
    } catch (error) {
        ElMessage.error('封面上传失败，请稍后重试')
        throw error
    }
}

const handleRemove = () =>
{
    imgUrl.value=''
    formData.coverImage=''
}

const handleContentChange = (data) =>
{
    formData.content = data.html
}

const editorInstance = ref(null)
const handleEditorChange = (editor) =>
{
    editorInstance.value = editor

    if (formData.content && editor){
        nextTick(()=>{
            editor.setHtml(formData.content)
        })
    }
}

const btnPreview = ref(false)


//upload
const formRef = ref()
const loading = ref(false)

const handleSubmit = async () => {
    try {
        await formRef.value.validate()
        loading.value = true
        const submitData = {
            ...formData,
            tags: formData.tagArray.join(',')
        }
        delete submitData.tagArray

        if (isEdit.value) {

            submitData.id = null
            createArticle(submitData).then(res => {
                loading.value = false
                emit('success')
            })
        } else {
            updateArticle(props.article.id,submitData).then(res =>{
                loading.value = false
                emit('success')
            })
        }
        emit('success')
    } catch (error) {
        // 表单与请求错误分别由表单和请求拦截器提示
    }
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
