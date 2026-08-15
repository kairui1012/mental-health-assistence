<template>
    <div>
        <PageHead title="知识文章">
            <template #button>
                <el-button @click="handleEdit({})" type="primary">新增</el-button>
            </template>
        </PageHead>
        <TableSearch class="knowledge-search" :form-item="formItem" @search="handleSearch" />
        <el-table :data="tableData" style="width: 100%;margin-top: 25px;">
            <el-table-column width="300" label="文章标题" fixed="left">
                <template #default="scope">
                    <div style="display: flex;align-items: center;">
                        <el-icon>
                            <timer />
                        </el-icon>
                        <span>{{ scope.row.title }}</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="分类" width="200">
                <template #default="scope">
                    <div style="display: flex;align-items: center;">
                        <el-icon>
                            <timer />
                        </el-icon>
                        <span>{{ categoryMap[scope.row.categoryId] }}</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="作者" prop="authorName" width="150" />
            <el-table-column label="阅读量" prop="readCount" width="150" />
            <el-table-column label="发布时间" prop="updatedAt" width="150" />
            <el-table-column label="操作" width="240" fixed="right">
                <template #default="scope">
                    <el-button @click="handleEdit(scope.row)" text type="primary">
                        编辑
                    </el-button>
                    <el-button @click="handlePublish(scope.row)" text
                        v-if="scope.row.status === 0 || scope.row.status === 2" type="success">
                        发布
                    </el-button>
                    <el-button @click="handleUnpublish(scope.row)" text v-if="scope.row.status === 1" type="warning">
                        下线
                    </el-button>
                    <el-button @click="handleDelete(scope.row)" text type="danger">
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination style="margin-top: 25px;" :page-size="pagination.size" layout="prev, pager, next"
            :total="pagination.total" @change="handleChange" />
        <ArticleDialog v-model:modelValue="dialogVisible" :article="currentArticle" :categories="categories"
            @success="handleSuccess" />
    </div>
</template>

<script setup>
import PageHead from '@/components/PageHead.vue'
import TableSearch from '@/components/TableSearch.vue'
import { categoryTree, articlePage, getArticleDetail, changeArticleStatus, deleteArticle } from '@/api/admin'
import { onMounted, ref, reactive } from 'vue'
import ArticleDialog from '../components/ArticleDialog.vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const formItem = [
    {
        comp: 'input',
        prop: 'title',
        label: '文章标题',
        placeholder: '请输入文章标题'
    },
    {
        comp: 'select',
        prop: 'categoryId',
        label: '分类',
        placeholder: '请选择分类',
    },
    {
        comp: 'select',
        prop: 'status',
        label: '状态',
        placeholder: '请选择状态',
        options: [
            {
                label: '草稿',
                value: 0
            },
            {
                label: '已发布',
                value: 1
            },
            {
                label: '已下线',
                value: 2
            },
        ]
    }
]

const pagination = reactive({
    currentPage: 1,
    size: 10,
    total: 0
})

const handleSearch = async (formData) => {
    const params = {
        ...pagination, ...formData
    }

    const { records, total } = await articlePage(params)
    pagination.total = total
    tableData.value = records
}

const categoryMap = reactive({})

const categories = ref([])

const tableData = ref([])

const dialogVisible = ref(false)

const currentArticle = ref(null)

const handleSuccess = () => {
    dialogVisible.value = false
    handleSearch()
}

const handleEdit = async (row) => {
    if (!row.id) {
        currentArticle.value = null
        dialogVisible.value = true
    }
    else {
        try {
            const res = await getArticleDetail(row.id)
            currentArticle.value = res
            dialogVisible.value = true
        } catch (error) {
            // 错误提示由请求拦截器统一处理
        }
    }
}

const handlePublish = async (row) => {
    try {
        await ElMessageBox.confirm(
            `确认发布文章${row.title}吗?`,
            '确认',
            {
                confirmButtonText: '确认发布',
                cancelButtonText: '取消',
                type: 'info'
            }
        )
        await changeArticleStatus(row.id, { status: 1 })
        ElMessage.success('发布成功')
        await handleSearch()
    } catch (error) {
        // 取消确认或请求失败时不继续刷新列表；请求错误由拦截器提示。
    }
}


const handleUnpublish = async (row) => {
    try {
        await ElMessageBox.confirm(
            `确认下线文章${row.title}吗?`,
            '确认',
            {
                confirmButtonText: '确认下线',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
        await changeArticleStatus(row.id, { status: 2 })
        ElMessage.success('下线成功')
        await handleSearch()
    } catch (error) {
        // 取消确认或请求失败时不继续刷新列表；请求错误由拦截器提示。
    }
}

const handleDelete = async (row) => {
    try {
        await ElMessageBox.confirm(
            `确认删除文章${row.title}吗?`,
            '确认',
            {
                confirmButtonText: '确认删除',
                cancelButtonText: '取消',
                type: 'danger'
            }
        )
        await deleteArticle(row.id)
        ElMessage.success('删除成功')
        await handleSearch()
    } catch (error) {
        // 取消确认或请求失败时不继续刷新列表；请求错误由拦截器提示。
    }
}

onMounted(async () => {
    const data = await categoryTree()
    categories.value = data.map(item => {
        categoryMap[item.id] = item.categoryName
        return {
            label: item.categoryName,
            value: item.id
        }
    })
    formItem[1].options = categories.value
    handleSearch()
})


const handleChange = (page) => {
    pagination.currentPage = page
    handleSearch()
}


</script>


<style lang="scss" scoped>
.knowledge-search {
    margin-top: 24px;
}

.articleDetail-container {
    background: linear-gradient(135deg, #fafbfc 0%, #f7f9fc 50%, #f2f6fa 100%);

    .flex-box {
        display: flex;
        align-items: center;

        .item {
            margin-right: 20px;

            span {
                margin-left: 5px;
            }
        }
    }

    .header-section {
        background: linear-gradient(135deg, #f59e0b 0%, #8b5cf6 100%);
        color: white;
        padding: 48px;

        .header-content {
            display: flex;
            align-items: center;
            gap: 12px;
        }
    }

    .content {
        margin: 0 auto;
        width: 980px;
        padding: 20px;

        .diary-card {
            margin-bottom: 20px;
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);

            .title {
                margin-bottom: 15px;
                font-size: 20px;
                font-weight: 600;
                color: #374151;
            }

            .sub-title {
                margin-top: 20px;
                display: flex;
                align-items: center;

                .category-tag {
                    margin-right: 20px;
                }
            }

            .article-title {
                font-size: 28px;
                font-weight: bold;
                color: #111827;
                margin-top: 30px;
                margin-bottom: 10px;
            }

            .summary-content {
                background: rgba(126, 211, 33, 0.1);
                border-left: 4px solid #7ED321;
                padding: 10px 15px;
                border-radius: 0 8px 8px 0;
                position: relative;
            }

            .content-wrapper {
                font-size: 15px;
                color: #374151;

                :deep(p) {
                    margin-bottom: 10px;
                }

                :deep(h1),
                :deep(h2),
                :deep(h3),
                :deep(h4),
                :deep(h5),
                :deep(h6) {
                    margin: 15px 0 10px;
                    color: #111827;
                    font-weight: 600;
                }

                :deep(h2) {
                    font-size: 15px;
                    border-bottom: 2px solid #e5e7eb;
                    padding-bottom: 5px;
                }

                :deep(h3) {
                    font-size: 13px;
                }

                :deep(ul),
                :deep(ol) {
                    padding-left: 15px;
                    margin-bottom: 10px;
                }

                :deep(li) {
                    margin-bottom: 5px;
                }
            }

            .tags-content {
                margin-top: 20px;
                padding-top: 15px;
                border-top: 1px solid #e5e7eb;

                .tags-title {
                    margin-bottom: 10px;
                    font-size: 14px;
                    font-weight: 600;
                    color: #374151;
                }

                .tags-list {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 10px;
                }
            }
        }
    }
}
</style>
