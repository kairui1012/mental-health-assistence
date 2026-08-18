<template>
    <!-- 咨询页面 -->
    <div class="consultation-container">
        <div class="sidebar">
            <!-- 助手信息 -->
            <div class="ai-assistant-info">
                <div class="breathing-circle">
                    <el-image style="width: 25px; height: 25px" :src="iconURL" />
                </div>
                <div class="assistant-name">心理健康 AI 助手</div>
                <div class="online-status">
                    <div class="status-dot"></div>
                    在线服务中
                </div>
            </div>
            <!-- 情绪花园 -->
            <div class="emotion-garden">
                <div class="garden-header">
                    <div class="garden-title">情绪花园</div>
                </div>
                <div class="emotion-info" :class="`emotion-level-${getIntensityClass(currentEmotion.emotionScore)}`">
                    <div class="emotion-name">{{ currentEmotion.primaryEmotion }}</div>
                    <div class="emotion-score">{{ currentEmotion.emotionScore }}</div>
                </div>
                <div class="warm-tips">
                    <div class="emotion-status-text">
                        <span class="status-label">
                            今天感觉
                        </span>
                        <span class="status-emotion" :class="`emotion-level-${getIntensityClass(currentEmotion.emotionScore)}`">
                            {{ currentEmotion.isNegative ? '需要关注' : '很不错' }}
                        </span>
                    </div>
                    <div class="emotion-intensity" :class="`emotion-level-${getIntensityClass(currentEmotion.emotionScore)}`">
                        <span class="intensity-dots">
                            <span v-for="dot in 3" :key="dot" class="dot"
                                :class="{ active: getIntensityClass(currentEmotion.emotionScore) >= dot }"></span>
                        </span>
                            <span class="intensity-text">
                                {{ getRiskText(currentEmotion.riskLevel) }}
                            </span>
                    </div>
                        <!-- 温暖建议卡片 -->
                        <div class="warm-suggestion" v-if="currentEmotion.suggestion">
                            <div class="suggestion-icon">
                                💝
                            </div>
                            <div class="suggestion-content">
                                <div class="suggestion-title">
                                    给你的小建议
                                </div>
                                <div class="suggestion-text">
                                    {{ currentEmotion.suggestion }}
                                </div>
                            </div>
                        </div>
                        <!-- 治愈行动 -->
                         <div class="healing-actions" v-if="currentEmotion.improvementSuggestions?.length > 0">
                             <div class="actions-title">
                                治愈小行动
                             </div>
                             <div class="actions-list">
                                <div v-for="action in currentEmotion.improvementSuggestions" :key="action" class="action-item">
                                    <div class="action-icon">✨</div>
                                    <div class="action-text">{{ action }}</div>
                                </div>
                             </div>
                         </div>
                          <!-- 风险提示 -->
                          <div class="risk-notice" v-if="currentEmotion.isNegative && currentEmotion.riskLevel > 1">
                            <div class="notice-icon">🤗</div>
                            <div class="notice-content">
                                <div class="notice-title">温馨提示</div>
                                <div class="notice-text">{{ currentEmotion.riskDescription }}</div>
                            </div>
                          </div>
                </div>
            </div>
            <!-- 会话区域：历史信息 -->
            <div class="session-history">
                <h4 class="session-title">会话列表</h4>
                <div class="session-list">
                    <div v-for="session in sessionList" :key="session.id" @click="handleSessionClick(session)"
                        class="session-item">
                        <div class="session-info">
                            <div class="session-title">
                                <span>{{ session.sessionTitle }}</span>
                                <div class="session-meta">
                                    <span class="session-time">{{ session.startedAt }}</span>
                                </div>
                                <div class="session-preview">
                                    {{ session.lastMessageContent }}
                                </div>
                                <div class="session-stats">
                                    <span>
                                        <el-icon>
                                            <ChatRound />
                                        </el-icon>
                                        {{ session.messageCount || 0 }}
                                    </span>
                                    <span>
                                        <el-icon>
                                            <Clock />
                                        </el-icon>
                                        {{ session.durationMinutes || 0 }} 分钟
                                    </span>
                                </div>
                            </div>
                            <div class="session-actions">
                                <el-button text type="danger" size="small" @click.stop="handleDeleteSession(session.id)">
                                    <el-icon>
                                        <DeleteFilled />
                                    </el-icon>
                                </el-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 聊天区域 -->
        <div class="chat-main">
            <div class="chat-header">
                <div class="header-left">
                    <div class="chat-avatar">
                        <el-image style="width: 30px; height: 30px" :src="iconUrl1" />
                    </div>
                    <div class="chat-info">
                        <h2>心理健康 AI 助手</h2>
                        <p>陪伴您梳理情绪与心事</p>
                    </div>
                </div>
                <el-button circle @click="createNewFrontendSession" title="新建会话">
                    <el-icon size="20px" color="#00f">
                        <Plus />
                    </el-icon>
                </el-button>
            </div>
            <!-- 历史消息 -->
            <div class="chat-messages">
                <!-- 初始欢迎语 -->
                <div class="message-item ai-message" v-if="messages.length === 0">
                    <div class="message-avatar">
                        <el-image style="width: 18px; height: 18px" :src="iconURL" />
                    </div>
                    <div class="message-content">
                        <div class="message-bubble">
                            <p>您好，我是您的心理健康 AI 助手。很高兴在这里陪伴您。</p>
                        </div>
                        <div class="message-time">
                            刚刚
                        </div>
                    </div>
                </div>
                <!-- 消息列表 -->
                <div v-for="msg in messages" :key="msg.id" class="message-item"
                    :class="msg.senderType === 1 ? 'user-message' : 'ai-message'">
                    <div class="message-avatar">
                        <el-image v-if="msg.senderType === 1" style="width: 18px; height: 18px" :src="iconUrl2" />
                        <el-image v-if="msg.senderType === 2" style="width: 18px; height: 18px" :src="iconURL" />
                    </div>
                    <div class="message-content">
                        <div class="message-bubble">
                            <!-- AI正在思考中 -->
                            <div v-if="msg.senderType === 2 && isAiTyping && !msg.content" class="typing-indicator">
                                <div class="typing-dot"></div>
                                <div class="typing-dot"></div>
                                <div class="typing-dot"></div>
                            </div>
                            <!-- AI错误提示 -->
                            <div v-else-if="msg.isError" class="error-message">
                                <p>{{ msg.content }}</p>
                            </div>
                            <!-- AI正常的返回消息 -->
                            <MarkdownRenderer v-else-if="msg.senderType === 2 && !msg.isError" :content="msg.content"
                                :is-ai-message="true" />

                            <p v-else-if="msg.content" v-html="formatMessageContent(msg.content)">

                            </p>
                        </div>
                        <div class="message-time">{{ msg.senderType === 2 && isAiTyping ? '正在输入中...' : msg.createdAt }}
                        </div>
                    </div>
                </div>
            </div>
            <!-- 消息输入 -->
            <div class="chat-input">
                <div class="input-container">
                    <el-input v-model="userMessage" placeholder="请输入您想分享的内容" type="textarea" :rows="3"
                        :disabled="isAiTyping" @keydown="handleKeyDown" class="message-input" clearable />
                    <div class="input-footer">
                        <span>按enter发送,shift + enter换行</span>
                        <span>{{ userMessage.length }}/ 500</span>
                    </div>
                </div>
                <el-button :disabled="!userMessage.trim() || userMessage.length > 500" type="primary" class="send-btn"
                    @click="sendMessage">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                </el-button>

            </div>
        </div>
    </div>
</template>

<script setup>
// Element Plus 图标、Vue 响应式 API 与咨询会话接口。
import { ChatRound, Clock, DeleteFilled, Plus, Promotion } from '@element-plus/icons-vue';
import { ref, onMounted } from 'vue';
import { startSession, getSessionList, deleteSession, getSessionDetail, getSessionEmotion } from '../api/frontend';
import { ElMessage } from 'element-plus';
import MarkdownRenderer from '../components/MarkdownRenderer.vue';
import { fetchEventSource } from '@microsoft/fetch-event-source';

// 左侧助手信息和欢迎消息使用的机器人头像。
const iconURL = new URL('@/assets/images/robot-fill.png', import.meta.url).href

// 聊天顶部区域使用的点赞图标。
const iconUrl1 = new URL('@/assets/images/like.png', import.meta.url).href

const iconUrl2 = new URL('@/assets/images/users.png', import.meta.url).href

// 顶部“新建会话”按钮的事件入口。
const createNewFrontendSession = () => {
    if (isAiTyping.value) {
        ElMessage.error('AI 助手正在整理回复，请稍候。')
        return
    }
    createNewSession()
}

// 创建仅用于前端交互的临时会话，首次发送消息后会由接口转为正式会话。
const createNewSession = () => {
    const newSession = {
        sessionId: `temp_${Date.now()}`,
        status: `TEMP`,
        sessionTitle: `新对话`
    }
    currentSession.value = newSession
    messages.value = []
    userMessage.value = ''
    resetCurrentEmotion()
}

// 保存当前正在进行或被用户选中的会话对象。
const currentSession = ref(null)

// 保存接口返回的历史会话列表，供左侧会话栏循环展示。
const sessionList = ref([])

// 保存当前会话中的消息数据，用于判断是否显示欢迎语。
const messages = ref([])

// 双向绑定消息输入框，保存用户正在输入的文字。
const userMessage = ref('')

// 标记 AI 是否正在生成回复，用来禁用输入并避免重复发送。
const isAiTyping = ref(false)

const createDefaultEmotion = () => ({
    primaryEmotion: `中性`,
    emotionScore: 50,
    isNegative: false,
    riskLevel: 0,
    suggestion: '情绪状态平稳',
    improvementSuggestions: [],
    riskDescription: ''
})

const currentEmotion = ref(createDefaultEmotion())

const resetCurrentEmotion = () => {
    currentEmotion.value = createDefaultEmotion()
}

const loadSessionEmotion = async (sessionId) => {
    if (!sessionId) return

    const normalizedSessionId = String(sessionId).startsWith('session_')
        ? String(sessionId)
        : `session_${sessionId}`

    try {
        const res = await getSessionEmotion(normalizedSessionId)
        currentEmotion.value = {
            ...createDefaultEmotion(),
            ...res,
            improvementSuggestions: res?.improvementSuggestions || []
        }
    } catch {
        resetCurrentEmotion()
    }
}

const getIntensityClass = (score) => {
    if (score >= 61) {
        return 3
    }
    else if (score >= 31) {
        return 2
    }
    return 1
}

const getRiskText = (level) => {
    switch (level) {
        case 0:
            return '正常'
        case 1:
            return '关注'
        case 2:
            return '预警'
        case 3:
            return '危机'
        default:
            return '正常'
    }
}

// Enter 单独按下时阻止默认换行；Shift + Enter 仍可保留为换行输入。
const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault()
        sendMessage()
    }
}

// 校验输入与助手状态；临时会话发送首条消息时创建正式会话。
const sendMessage = () => {
    const message = userMessage.value.trim()
    if (!message) return

    if (isAiTyping.value) {
        ElMessage.error('AI 助手正在整理回复，请稍候。')
        return
    }

    userMessage.value = ''
    if (currentSession.value.status === 'TEMP') {
        startNewSession(message)
    } else {
        messages.value.push({
            id: Date.now(),
            senderType: 1,
            content: message,
            createdAt: new Date().toISOString()
        })
        startAIResponse(currentSession.value.sessionId, message)
    }
}

// 将首条消息提交给接口，并用接口返回的数据更新当前会话。
const startNewSession = (message) => {
    const sessionParams = {
        initialMessage: message
    }
    if (currentSession.value.sessionTitle === '新对话') {
        sessionParams.sessionTitle = `AI助手 - ${new Date().toLocaleString()}`
    }
    else {
        sessionParams.sessionTitle = currentSession.value.sessionTitle
    }

    startSession(sessionParams).then(res => {
        if (!res?.sessionId) {
            ElMessage.error('创建会话失败：接口未返回会话 ID')
            return
        }

        const sessionData = {
            sessionId: res.sessionId,
            status: res.status,
            sessionTitle: sessionParams.sessionTitle
        }
        if (currentSession.value && currentSession.value.status === 'TEMP') {
            // 更新为正式会话
            Object.assign(currentSession.value, sessionData)
        } else {
            currentSession.value = sessionData
        }
        getSessionPage()

        //添加用户消息
        messages.value.push({
            id: Date.now(),
            senderType: 1,
            content: message,
            createdAt: new Date().toString()
        })

        startAIResponse(sessionData.sessionId, message)
    }).catch(() => { })
}

const startAIResponse = (sessionId, userMessage) => {

    if (isAiTyping.value) {
        ElMessage.error(' AI助手正在输入')
        return
    }

    isAiTyping.value = true

    const aiMessage = {
        id: `ai_${Date.now}_${Math.random().toString(36).substr(2, 9)}`,
        senderType: 2,
        content: '',
        createdAt: new Date().toISOString()
    }
    messages.value.push(aiMessage)

    const controller = new AbortController()

    // 调用流式接口
    fetchEventSource('/api/psychological-chat/stream', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('token'),
            Accept: 'text/event-stream'
        },
        body: JSON.stringify({
            sessionId,
            userMessage
        }),
        signal: controller.signal,
        onopen: (response) => {
            const contentType = response.headers.get('content-type') || ''
            if (!contentType.includes('text/event-stream')) {
                throw new Error('服务器返回非流式数据')
            }
        },
        onmessage: (event) => {
            const raw = event.data.trim()
            if (!raw) return
            const eventName = event.event
            // 当前会话最后一条 AI 消息
            const aiMessage = messages.value[messages.value.length - 1]

            if (eventName === 'done') {
                isAiTyping.value = false
                // 收到完成事件后再刷新情绪分析；不能依赖 abort 后未必触发的 onclose。
                setTimeout(() => loadSessionEmotion(sessionId), 800)
                controller.abort()
                return
            }
            const payload = JSON.parse(raw)
            const ok = String(payload.code) === '200'

            if (ok && payload.data && payload.data.content) {
                aiMessage.content += payload.data.content
            } else if (!ok) {
                handleError(payload.msg || payload.message || 'AI回复失败')
            }
        },
        onerror: (err) => {
            handleError(err || 'AI回复失败')
            throw err
        }
    })
}

const handleError = (errorMessage = 'AI回复失败，请重试') => {
    const aiMessage = messages.value[messages.value.length - 1]
    if (aiMessage) {
        aiMessage.content = errorMessage
    }
    isAiTyping.value = false
    ElMessage.error(errorMessage)
}

// 获取第一页会话记录，供左侧会话列表展示。
const getSessionPage = () => {
    return getSessionList({
        pageNum: 1,
        pageSize: 10
    }).then(res => {
        sessionList.value = res.records
    })
}

// 从历史记录中切换当前会话。
const handleSessionClick = (session) => {
    const sessionData = {
        sessionId: session.id,
        status: 'ACTIVE',
        sessionTitle: session.sessionTitle
    }
    currentSession.value = sessionData

    getSessionDetail(session.id).then(res => {
        messages.value = res
    }).catch(() => {})
    loadSessionEmotion(session.id)
}

// 请求删除指定会话。
const handleDeleteSession = async (sessionId) => {
    try {
        await deleteSession(sessionId)
        if (currentSession.value?.sessionId === sessionId) {
            createNewSession()
        }
        await getSessionPage()
        ElMessage.success('删除成功')
    } catch {
        // 请求拦截器会显示服务端返回的错误信息。
    }
}

// 简单的换行逻辑
const formatMessageContent = (content) => {
    return content.replace(/\n/g, '<br>')
}

// 页面加载后初始化历史会话列表和一个新的临时会话。
onMounted(() => {
    getSessionPage()
    createNewSession()
})



</script>

<style lang="scss" scoped>
/* 页面整体布局：固定宽度的侧栏与聊天主区域并排显示。 */
.consultation-container {
    margin: 0 auto;
    width: min(1277px, 106.4%);
    height: calc((100dvh - 88px) / 0.94);
    zoom: 0.94;
    min-height: 0;
    box-sizing: border-box;
    background: #fbfbfd;
    display: flex;
    gap: 20px;
    padding: 20px;

    .sidebar {
        width: 320px;
        min-height: 0;
        overflow-y: auto;

        .ai-assistant-info {
            margin-bottom: 20px;
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 252, 248, 0.95) 100%);
            border-radius: 16px;
            padding: 16px;
            box-shadow: none;
            border: 2px solid #b5d65a;
            backdrop-filter: blur(10px);
            transition: all 0.3s ease;

            .breathing-circle {
                width: 60px;
                height: 60px;
                background: linear-gradient(135deg, #689f38 0%, #b7d84b 100%);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 12px;
                animation: breathing 4s ease-in-out infinite;
                box-shadow: none;
                position: relative;
            }

            .assistant-name {
                font-size: 16px;
                font-weight: 700;
                background: linear-gradient(135deg, #689f38, #b7d84b);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                text-align: center;
                background-clip: text;
                margin: 0 0 12px;
            }

            .online-status {
                display: flex;
                align-items: center;
                justify-content: center;
                color: #059669;
                font-size: 12px;
                font-weight: 600;

                .status-dot {
                    width: 8px;
                    height: 8px;
                    background: #059669;
                    border-radius: 50%;
                    margin-right: 8px;
                    animation: pulse 2s infinite;
                    box-shadow: none;
                }
            }
        }

        /* 左侧可滚动的历史会话卡片。 */
        .session-history {
            background: white;
            border-radius: 16px;
            padding: 16px;
            box-shadow: none;
            border: 2px solid #b5d65a;
            margin-bottom: 20px;
            min-height: 250px;
            display: flex;
            flex-direction: column;

            > .session-title {
                font-size: 20px;
                font-weight: 700;
                color: #557a25;
                margin: 0;
                display: flex;
                align-items: center;
                justify-content: space-between;

            }

            .session-list {
                flex: 1;
                display: flex;
                flex-direction: column;
                justify-content: flex-start;
                margin-top: 20px;
                overflow-y: auto;
                max-height: 200px;
                scrollbar-width: thin;
                scrollbar-color: rgba(64, 150, 255, 0.3) transparent;

                .session-item {
                    position: relative;
                    display: flex;
                    align-items: flex-start;
                    gap: 12px;
                    padding: 12px;
                    margin-bottom: 8px;
                    border-radius: 12px;
                    cursor: pointer;
                    flex-shrink: 0;
                    transition: all 0.3s ease;
                    border: 2px solid transparent;

                    &:hover {
                        background: #f8f9ff;
                        border-color: #e6f0ff;
                    }

                    &.active {
                        background: #e6f0ff;
                        border-color: #4096ff;
                    }

                    .session-info {
                        flex: 1;

                        .session-title {
                            font-weight: 500;
                            font-size: 14px;
                            color: #333;
                            margin-bottom: 4px;
                            white-space: nowrap;
                            overflow: hidden;
                            text-overflow: ellipsis;

                            .session-meta {
                                display: flex;
                                align-items: center;
                                gap: 8px;
                                margin-bottom: 6px;

                                .session-time {
                                    font-size: 12px;
                                    color: #999;
                                }
                            }

                            .session-preview {
                                width: 200px;
                                font-size: 12px;
                                color: #666;
                                margin-bottom: 6px;
                                white-space: nowrap;
                                overflow: hidden;
                                text-overflow: ellipsis;
                            }

                            .session-stats {
                                display: flex;
                                align-items: center;
                                gap: 12px;

                                span {
                                    font-size: 12px;
                                    color: #999;
                                    display: flex;
                                    align-items: center;
                                    gap: 4px;
                                }
                            }
                        }

                        .session-actions {
                            position: absolute;
                            top: 10px;
                            right: 12px;
                        }
                    }
                }

                .no-sessions-text {
                    text-align: center;
                    font-size: 14px;
                    color: #999;
                }
            }
        }

        .emotion-garden {
            background: #f3f9dc;
            border-radius: 20px;
            padding: 16px;
            margin-bottom: 20px;
            box-shadow: none;
            border: 2px solid #b5d65a;
            position: relative;
            overflow: hidden;
            min-height: 300px;

            .garden-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 20px;
                position: relative;
                z-index: 2;

                .garden-title {
                    display: flex;
                    align-items: center;
                    gap: 8px;
                    font-size: 20px;
                    font-weight: 700;
                    color: #557a25;
                }
            }

            .emotion-info {
                margin: 0 auto 20px;
                width: 80px;
                height: 80px;
                border-radius: 50%;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                z-index: 10;
                box-shadow: none;
                border: 3px solid #d7e99c;
                background: #689f38;
                color: #fff;

                &.emotion-level-1 {
                    background: #dfb633;
                }

                &.emotion-level-2 {
                    background: #a6c83b;
                }

                &.emotion-level-3 {
                    background: #5d9634;
                }

                .emotion-name {
                    font-size: 19px;
                    font-weight: 600;
                    line-height: 1;
                    margin-bottom: 2px;
                }

                .emotion-score {
                    font-size: 17px;
                    font-weight: 700;
                    opacity: 0.9;
                }
            }

            .warm-tips {
                text-align: center;
                margin-bottom: 16px;

                .emotion-status-text {
                    margin-bottom: 12px;

                    .status-label {
                        font-size: 17px;
                        color: #58752c;
                        margin-right: 8px;
                    }

                    .status-emotion {
                        font-size: 16px;
                        font-weight: 600;
                        padding: 3px 10px;
                        border-radius: 16px;
                        display: inline-block;
                        color: #fff;

                        &.emotion-level-1 {
                            background: #d9ac2f;
                        }

                        &.emotion-level-2 {
                            background: #a6c83b;
                        }

                        &.emotion-level-3 {
                            background: #689f38;
                        }
                    }
                }

                .emotion-intensity {
                    margin-bottom: 16px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    gap: 8px;

                    .intensity-dots {
                        display: flex;
                        gap: 4px;

                        .dot {
                            width: 8px;
                            height: 8px;
                            border-radius: 50%;
                            background: #dce8bd;
                            transition: all 0.3s ease;

                            &.active {
                                background: #689f38;
                                transform: scale(1.2);
                                box-shadow: none;
                            }
                        }
                    }

                    &.emotion-level-1 .dot.active {
                        background: #d9ac2f;
                        box-shadow: none;
                    }

                    &.emotion-level-2 .dot.active {
                        background: #a6c83b;
                        box-shadow: none;
                    }

                    &.emotion-level-3 .dot.active {
                        background: #689f38;
                        box-shadow: none;
                    }

                    .intensity-text {
                        font-size: 15px;
                        color: #58752c;
                        font-weight: 500;
                    }
                }

                .warm-suggestion {
                    background: #ffffff;
                    border-radius: 16px;
                    padding: 10px;
                    margin-bottom: 16px;
                    display: flex;
                    align-items: flex-start;
                    gap: 8px;
                    border: 2px solid #c6dd79;
                    box-shadow: none;

                    .suggestion-icon {
                        font-size: 18px;
                        flex-shrink: 0;
                        margin-top: 2px;
                    }

                    .suggestion-content {
                        text-align: left;
                        flex: 1;

                        .suggestion-title {
                            font-size: 15px;
                            font-weight: 600;
                            color: #58752c;
                            margin-bottom: 4px;
                        }

                        .suggestion-text {
                            font-size: 14px;
                            color: #465c26;
                            line-height: 1.4;
                        }
                    }
                }

                .healing-actions {
                    margin-bottom: 16px;

                    .actions-title {
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        gap: 8px;
                        font-size: 18px;
                        font-weight: 600;
                        color: #58752c;
                        margin-bottom: 16px;
                    }

                    .actions-list {
                        display: flex;
                        flex-direction: column;
                        gap: 10px;

                        .action-item {
                            background: #ffffff;
                            border-radius: 12px;
                            padding: 12px;
                            display: flex;
                            align-items: center;
                            gap: 10px;
                            border: 2px solid #c6dd79;
                            box-shadow: none;
                            text-align: left;

                            .action-icon {
                                font-size: 14px;
                                color: #86ad34;
                                flex-shrink: 0;
                            }

                            .action-text {
                                font-size: 15px;
                                color: #465c26;
                                line-height: 1.4;
                                flex: 1;
                            }
                        }
                    }
                }

                .risk-notice {
                    background: #f4f9df;
                    border-radius: 16px;
                    padding: 16px;
                    display: flex;
                    align-items: flex-start;
                    gap: 12px;
                    border: 2px solid #b5d65a;
                    box-shadow: none;

                    .notice-icon {
                        font-size: 20px;
                        flex-shrink: 0;
                        margin-top: 2px;
                    }

                    .notice-content {
                        flex: 1;

                        .notice-title {
                            font-size: 17px;
                            font-weight: 600;
                            color: #58752c;
                            margin-bottom: 6px;
                        }

                        .notice-text {
                            font-size: 16px;
                            color: #465c26;
                            line-height: 1.5;
                        }
                    }
                }
            }
        }
    }

    /* 右侧当前咨询会话及消息输入区域。 */
    .chat-main {
        background: #fbfbfd;
        border-radius: 24px;
        box-shadow: 0 12px 32px rgba(39, 56, 23, 0.08), 0 2px 8px rgba(39, 56, 23, 0.04);
        border: 1px solid rgba(60, 60, 67, 0.1);
        backdrop-filter: blur(10px);
        display: flex;
        flex-direction: column;
        overflow: hidden;
        flex: 1;
        min-height: 0;

        .chat-header {
            background: linear-gradient(135deg, #689f38 0%, #b7d84b 100%);
            color: #fff;
            padding: 18px 24px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            position: relative;
            flex-shrink: 0;
            border-bottom: none;
            box-shadow: 0 5px 18px rgba(104, 159, 56, 0.2);
            backdrop-filter: blur(20px);

            .header-left {
                display: flex;
                align-items: center;

                .chat-avatar {
                    width: 48px;
                    height: 48px;
                    background: linear-gradient(135deg, #d9ee97, #a9ce4c);
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    margin-right: 16px;
                    box-shadow: 0 5px 14px rgba(108, 147, 43, 0.2);
                    position: relative;
                    z-index: 1;
                }

                .chat-info {
                    h2 {
                        font-size: 22px;
                        font-weight: 600;
                        margin-bottom: 4px;
                        color: #fff;
                    }

                    p {
                        font-size: 16px;
                        color: #fff;
                    }
                }
            }
        }

        .chat-messages {
            flex: 1;
            overflow-y: auto;
            padding: 24px;
            display: flex;
            flex-direction: column;
            gap: 16px;
            background: #fbfbfd;
            min-height: 0;
            max-height: none;
            scrollbar-width: thin;
            scrollbar-color: rgba(104, 159, 56, 0.28) transparent;

            .message-item {
                display: flex;
                align-items: flex-start;
                gap: 12px;

                .message-avatar {
                    width: 32px;
                    height: 32px;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 14px;
                    color: white;
                    flex-shrink: 0;
                }

                &.ai-message {
                    .message-avatar {
                        background: linear-gradient(135deg, #d9ee97, #9ec340);
                        box-shadow: 0 4px 12px rgba(108, 147, 43, 0.2);
                    }
                }

                &.user-message {
                    flex-direction: row-reverse;

                    .message-avatar {
                        background: #e5e5ea;
                        box-shadow: none;
                    }

                    .message-content {
                        display: flex;
                        flex-direction: column;
                        align-items: flex-end;

                        .message-bubble {
                            background: linear-gradient(135deg, #b3d34e, #65b965);
                            border: 1px solid #a2c948;
                            border-radius: 18px 18px 5px 18px;
                            color: #fff;
                            box-shadow: none;
                            padding: 10px 16px;
                            line-height: 1.45;
                        }
                    }
                }

                .message-content {
                    max-width: 70%;

                    .message-bubble {
                        background: rgba(255, 255, 255, 0.94);
                        border-radius: 20px;
                        padding: 13px 16px;
                        position: relative;
                        animation: fadeInUp 0.4s ease-out;
                        border: 1px solid rgba(60, 60, 67, 0.08);
                        box-shadow: 0 3px 12px rgba(29, 29, 31, 0.05);
                        font-size: 16px;
                        line-height: 1.6;

                        .typing-indicator {
                            display: flex;
                            gap: 4px;
                            padding: 8px 0;

                            .typing-dot {
                                width: 8px;
                                height: 8px;
                                background: #ccc;
                                border-radius: 50%;
                                animation: typing 1.5s ease-in-out infinite;

                                &:nth-child(2) {
                                    animation-delay: 0.2s;
                                }

                                &:nth-child(3) {
                                    animation-delay: 0.4s;
                                }
                            }
                        }

                        /* 错误消息样式 */
                        .error-message {
                            background: linear-gradient(135deg, #FEF2F2 0%, #FECACA 100%);
                            border: 1px solid #F87171;
                            border-radius: 12px;
                            padding: 12px 16px;
                            color: #991B1B;
                            font-weight: 500;
                            display: flex;
                            align-items: center;
                            gap: 8px;
                        }
                    }

                    .message-time {
                        font-size: 13px;
                        color: #8e8e93;
                        margin: 6px 4px 0;
                    }
                }
            }
        }

        .chat-input {
            border-top: 1px solid rgba(60, 60, 67, 0.08);
            padding: 16px 20px 18px;
            display: flex;
            gap: 12px;
            align-items: flex-end;
            background: rgba(255, 255, 255, 0.78);
            backdrop-filter: blur(20px);
            flex-shrink: 0;

            .input-container {
                flex: 1;

                :deep(.message-input) {
                    --el-input-focus-border-color: #8e8e93;
                    --el-input-focus-color: #8e8e93;
                    --el-input-hover-border-color: #c8cbd1;
                }

                :deep(.el-textarea__inner) {
                    background: #f2f2f7;
                    border: 1px solid transparent;
                    border-radius: 16px;
                    box-shadow: none;
                    color: #1d1d1f;
                    padding: 12px 14px;
                    font-size: 16px;
                    line-height: 1.6;
                    transition: border-color 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;

                    &:focus {
                        background: #fff;
                        border-color: #8e8e93;
                        box-shadow: 0 0 0 4px rgba(60, 60, 67, 0.1);
                    }
                }
            }

            .input-footer {
                margin-top: 8px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                font-size: 13px;
                color: #78716c;
                color: #8e8e93;
                font-weight: 400;
            }

            .send-btn {
                height: 76px;
                width: 76px;
                border-radius: 16px;
                align-self: flex-start;
                background: linear-gradient(135deg, #a7ce42 0%, #719f2f 100%) !important;
                border: none !important;
                box-shadow: 0 5px 15px rgba(104, 159, 56, 0.25);
                transition: all 0.3s ease;

                :deep(.el-icon) {
                    font-size: 26px;
                }
            }

        }

    }
}
</style>
