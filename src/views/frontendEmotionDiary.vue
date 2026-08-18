<template>
    <div class="emotionDiary-container">
        <div class="header-section">
            <div class="header-content">
                <el-image style="width: 50px; height: 50px" :src="imageUrl" />
                <h1>情绪日记</h1>
            </div>
        </div>
        <div class="content">
            <!-- 情绪评分 -->
            <div class="diary-card">
                <div class="title">今日情绪评分</div>
                <div class="section">
                    <p>您今天的整体情绪状态如何</p>
                    <div class="rate">
                        <el-rate v-model="diaryForm.moodScore" :texts="emotionStatus" show-text :max="10"
                            :icons="heartIcons" :void-icon="HeartIcon" :colors="['#719f2f', '#719f2f', '#719f2f']"
                            void-color="#dcebb2" size="large" />
                    </div>
                </div>
            </div>
            <!-- 主要情绪 -->
            <div class="diary-card">
                <div class="title">主要情绪</div>
                <div class="emotion-grid">
                    <div v-for="emotion in emotionOptions" :key="emotion.name" class="emotion-card"
                        :class="{ 'selected': emotion.name === diaryForm.domainEmotion }"
                        @click="selectEmotion(emotion.name)">
                        <el-image style="width: 50px; height: 50px" :src="emotion.url" />
                        <div class="emotion-name">{{ emotion.name }}</div>
                    </div>
                </div>

            </div>
            <!-- 详细记录 -->
            <div class="diary-card">
                <div class="title">详细记录</div>
                <div class="detail-form">
                    <div class="form-group">
                        <div class="formlabel">情绪触发因素</div>
                        <el-input v-model="diaryForm.emotionTriggers" placeholder="今天什么事情影响您的情绪？" type="textarea"
                            :rows="3" maxlength="1000" :show-word-limit="true" />
                    </div>
                    <div class="form-group">
                        <div class="formlabel">今日感想</div>
                        <el-input v-model="diaryForm.diaryContent" placeholder="今天发生什么有趣或难过的事情？" type="textarea"
                            :rows="5" maxlength="2000" :show-word-limit="true" />
                    </div>
                    <!-- 生活指标 -->
                    <div class="life-indicators">
                        <div class="indicators-group">
                            <div class="form-label">睡眠质量</div>
                            <el-select v-model="diaryForm.sleepQuality" placeholder="请选择">
                                <el-option label="很差" :value="1" />
                                <el-option label="较差" :value="2" />
                                <el-option label="一般" :value="3" />
                                <el-option label="良好" :value="4" />
                                <el-option label="优秀" :value="5" />
                            </el-select>
                        </div>
                        <div class="indicators-group">
                            <div class="form-label">压力水平</div>
                            <el-select v-model="diaryForm.stressLevel" placeholder="请选择">
                                <el-option label="很低" :value="1" />
                                <el-option label="较低" :value="2" />
                                <el-option label="中等" :value="3" />
                                <el-option label="较高" :value="4" />
                                <el-option label="很高" :value="5" />
                            </el-select>
                        </div>
                    </div>
                    <!-- 按钮区 -->
                    <div class="action-buttons">
                        <el-button @click="resetForm">重置</el-button>
                        <el-button type="primary" @click="submit">提交记录</el-button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, h } from 'vue'
import { dayjs, ElMessage } from 'element-plus'
import { addEmotionDiary } from '@/api/frontend'

//情绪评分
const emotionStatus = ['绝望崩溃', '消沉抑郁', '焦虑烦躁', '低落不悦', '平静淡然', '轻松惬意', '愉悦舒心', '欢欣满足', '兴奋欣喜', '极致幸福']

// 固定尺寸的爱心 SVG，评分变化时只切换颜色，不会产生图标跳动。
const HeartIcon = {
    render() {
        return h('svg', { viewBox: '0 0 24 24', fill: 'currentColor', 'aria-hidden': 'true' }, [
            h('path', {
                d: 'M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z'
            })
        ])
    }
}
const heartIcons = [HeartIcon, HeartIcon, HeartIcon]

//情绪选项
const emotionOptions = [
    { name: '开心', url: new URL('@/assets/images/开心.png', import.meta.url).href },
    { name: '平静', url: new URL('@/assets/images/平静.png', import.meta.url).href },
    { name: '焦虑', url: new URL('@/assets/images/焦虑.png', import.meta.url).href },
    { name: '悲伤', url: new URL('@/assets/images/悲伤.png', import.meta.url).href },
    { name: '兴奋', url: new URL('@/assets/images/兴奋.png', import.meta.url).href },
    { name: '疲惫', url: new URL('@/assets/images/疲惫.png', import.meta.url).href },
    { name: '惊讶', url: new URL('@/assets/images/惊讶.png', import.meta.url).href },
    { name: '困惑', url: new URL('@/assets/images/困惑.png', import.meta.url).href }
]

const selectEmotion = (emotion) => {
    diaryForm.domainEmotion = emotion
}

const diaryForm = reactive({
    diaryDate: dayjs().format('YYYY-MM-DD'),
    moodScore: null,
    domainEmotion: '',
    emotionTriggers: '',
    diaryContent: '',
    sleepQuality: null,
    stressLevel: null
})

const resetForm = () => {
  Object.assign(diaryForm, {
    diaryDate: dayjs().format('YYYY-MM-DD'),
    moodScore: null,
    domainEmotion: '',
    emotionTriggers: '',
    diaryContent: '',
    sleepQuality: null,
    stressLevel: null
  })
}

const submit = () => {
    if(!diaryForm.moodScore){
        ElMessage.error('请选择情绪评分')
    }
    addEmotionDiary(diaryForm).then(()=>{
        ElMessage.success('提交成功')
        resetForm()
    })
}

const imageUrl = new URL('@/assets/images/like.png', import.meta.url).href
</script>

<style lang="scss" scoped>
.emotionDiary-container {
    --el-color-primary: #689f38;
    --el-color-primary-light-3: #8fbd4a;
    --el-color-primary-light-5: #b7d874;
    --el-color-primary-light-9: #f1f8df;
    background: #fbfbfd;

    .header-section {
        background: linear-gradient(135deg, #689f38 0%, #9ec846 48%, #d6dc59 100%);
        color: white;
        padding: 14px 32px;

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
            border: 1px solid #ebeef5;
            box-shadow: 0 4px 10px rgba(104, 159, 56, 0.08);

            .title {
                margin-bottom: 20px;
                font-size: 25px;
                font-weight: 600;
                color: #557a25;
            }

            &:first-child .title {
                color: #1d1d1f;
            }

            &:nth-child(2) .title {
                color: #1d1d1f;
            }

            &:nth-child(3) .title {
                color: #1d1d1f;
            }

            &:first-child {
                padding: 14px 20px;

                .title {
                    margin-bottom: 12px;
                }

                .section {
                    margin-bottom: 0;

                    p {
                        margin-bottom: 8px;
                    }
                }
            }

            .section {
                margin-bottom: 20px;

                p {
                    font-size: 15px;
                    color: #58752c;
                    margin-bottom: 15px;
                }

                .rate {
                    :deep(.el-rate__item) {
                        width: 28px;
                        height: 28px;
                        display: inline-flex;
                        align-items: center;
                        justify-content: center;
                    }

                    :deep(.el-rate__icon),
                    :deep(.el-rate__icon svg) {
                        width: 28px;
                        height: 28px;
                        font-size: 28px;
                        margin: 0;
                    }
                }
            }

            &:first-child .section p {
                color: #1d1d1f;
            }

            .emotion-grid {
                display: flex;
                flex-wrap: wrap;
                gap: 10px;

                .emotion-card {
                    padding: 15px;
                    border: 2px solid #ebeef5;
                    border-radius: 15px;
                    text-align: center;
                    cursor: pointer;
                    background: #fff;

                    .emotion-name {
                        margin-top: 10px;
                        padding: 0 75px;
                        color: #557a25;
                    }

                    &.selected {
                        border-color: #a6c83b;
                        background: #f1f8df;
                        transform: none;
                    }
                }
            }

            .detail-form {
                .form-group {
                    margin-bottom: 18px;

                    .formlabel {
                        margin-bottom: 8px;
                        color: #1d1d1f;
                        font-size: 15px;
                        font-weight: 600;
                    }

                    :deep(.el-textarea__inner) {
                        background: #fbfbfd;
                        border: 1px solid #e3e6ec;
                        border-radius: 14px;
                        box-shadow: none;
                        color: #1d1d1f;
                        padding: 12px 14px;
                        line-height: 1.55;
                        transition: border-color 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;

                        &:hover {
                            border-color: #c8cbd1;
                        }

                        &:focus {
                            background: #fff;
                            border-color: #8e8e93;
                            box-shadow: 0 0 0 4px rgba(60, 60, 67, 0.1);
                        }
                    }

                    :deep(.el-input__count) {
                        background: transparent;
                        color: #8e8e93;
                        right: 12px;
                        bottom: 8px;
                    }
                }

                .form-label {
                    margin: 10px 0;
                    color: #1d1d1f;
                    font-weight: 600;
                }

                .life-indicators {
                    display: grid;
                    grid-template-columns: repeat(2, minmax(0, 1fr));
                    gap: 20px;

                    .indicators-group {
                        min-width: 0;

                        :deep(.el-select) {
                            width: 100%;
                        }
                    }
                }

                .action-buttons {
                    margin-top: 40px
                }
            }
        }
    }
}
</style>
