<template>
    <main class="home-container">
        <!-- 背景环境光 -->
        <div class="ambient ambient-left"></div>
        <div class="ambient ambient-right"></div>

        <!-- 首屏文案与操作入口 -->
        <section class="hero">
            <p class="eyebrow">心理健康 AI 助手</p>
            <h1>
                给心情一个<br />
                <span>被理解的地方。</span>
            </h1>
            <p class="description">
                安心记录每一个瞬间，随时开始一段温柔的对话。
                让陪伴与专业支持，离你更近一些。
            </p>
            <div class="hero-actions">
                <el-button class="primary-action" size="large" @click="goToConsultation">
                    开始倾诉
                </el-button>
                <el-button class="secondary-action" size="large" @click="goToDiary">
                    记录心情
                </el-button>
            </div>
        </section>

        <!-- AI 陪伴对话展示 -->
        <section class="visual" aria-label="AI 陪伴助手">
            <div class="device-card">
                <div class="device-header">
                    <span></span><span></span><span></span>
                </div>
                <div class="conversation">
                    <p class="greeting">今天，感觉怎么样？</p>
                    <p class="reply">有一点疲惫，但我愿意慢慢说。</p>
                    <div class="support-message">
                        我在这里。你不需要独自面对。
                    </div>
                </div>
                <el-image class="robot" :src="iconUrl" fit="contain" />
            </div>
        </section>

        <!-- 产品核心价值 -->
        <section class="principles">
            <article>
                <span class="principle-number">01</span>
                <h2>安心表达</h2>
                <p>留出属于自己的空间，记录真实感受。</p>
            </article>
            <article>
                <span class="principle-number">02</span>
                <h2>温柔陪伴</h2>
                <p>在需要的时候，随时开始一段对话。</p>
            </article>
            <article>
                <span class="principle-number">03</span>
                <h2>看见变化</h2>
                <p>通过情绪记录，慢慢理解自己。</p>
            </article>
        </section>
    </main>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

// 首页插画资源
const iconUrl = new URL('@/assets/images/robot-fill.png', import.meta.url).href

// 页面跳转
const goToConsultation = () => router.push('/consultation')
const goToDiary = () => router.push('/emotion-diary')
</script>

<style lang="scss" scoped>
// 页面基础布局
.home-container {
    position: relative;
    min-height: calc(100vh - 210px);
    overflow: hidden;
    padding: clamp(56px, 7vw, 92px) max(24px, calc((100vw - 1120px) / 2));
    color: #1d1d1f;
    background: #fbfbfd;
}

.hero,
.visual,
.principles {
    position: relative;
    z-index: 1;
}

.hero {
    max-width: 660px;
    text-align: center;
    margin: 0 auto;
}

// 首屏标题与操作按钮
.eyebrow,
.principle-number {
    color: #6e6e73;
    font-size: 14px;
    font-weight: 600;
    letter-spacing: .08em;
}

h1 {
    margin: 18px 0 22px;
    font-size: clamp(46px, 7vw, 80px);
    line-height: 1.06;
    letter-spacing: -.065em;
    font-weight: 700;

    span {
        background: linear-gradient(100deg, #e5c84b, #a8cf48 48%, #65ba63);
        -webkit-background-clip: text;
        background-clip: text;
        color: transparent;
    }
}

.description {
    max-width: 500px;
    margin: 0 auto;
    color: #6e6e73;
    font-size: clamp(17px, 2vw, 21px);
    line-height: 1.55;
    letter-spacing: -.02em;
}

.hero-actions {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-top: 32px;

    :deep(.el-button) {
        min-height: 46px;
        padding: 0 22px;
        border-radius: 980px;
        font-size: 16px;
    }
}

.primary-action {
    border: 0;
    color: #fff;
    background: #8bc34a;

    &:hover {
        color: #fff;
        background: #9ccc65;
    }
}

.secondary-action {
    color: #689f38;
    border-color: #689f38;
    background: transparent;

    &:hover {
        color: #fff;
        border-color: #8bc34a;
        background: #8bc34a;
    }
}

.visual {
    width: min(720px, 100%);
    margin: 46px auto 0;
}

// 对话卡片
.device-card {
    position: relative;
    min-height: 285px;
    overflow: hidden;
    padding: 18px 24px;
    border: 1px solid rgba(255, 255, 255, .65);
    border-radius: 34px;
    background: rgba(255, 255, 255, .74);
    box-shadow: 0 24px 80px rgba(40, 78, 128, .15), inset 0 1px rgba(255, 255, 255, .92);
    backdrop-filter: blur(20px);
}

.device-header {
    display: flex;
    gap: 7px;

    span {
        width: 9px;
        height: 9px;
        border-radius: 50%;
        background: #d2d2d7;
    }
}

.conversation {
    width: 56%;
    padding-top: 48px;
    font-size: 16px;
    line-height: 1.4;
}

.greeting {
    margin: 0 0 14px;
    font-size: 24px;
    font-weight: 650;
    letter-spacing: -.04em;
}

.reply,
.support-message {
    width: fit-content;
    max-width: 270px;
    margin: 0 0 12px;
    padding: 11px 14px;
    border-radius: 18px 18px 18px 4px;
    background: #f1f1f4;
}

.support-message {
    margin-left: 30px;
    color: white;
    border-radius: 18px 18px 4px;
    background: linear-gradient(135deg, #b3d34e, #65b965);
}

.robot {
    position: absolute;
    right: 5%;
    bottom: 3%;
    width: min(38vw, 250px);
    z-index: 1;
    filter: drop-shadow(0 20px 24px rgba(38, 59, 94, .28)) saturate(1.15) contrast(1.08);
}

// 背景环境光与漂浮动画
.ambient {
    position: absolute;
    border-radius: 50%;
    pointer-events: none;
    filter: blur(18px);
}

.ambient-left {
    top: 210px;
    left: -160px;
    width: 420px;
    height: 420px;
    background: rgba(255, 239, 128, .42);
    animation: ambient-drift-left 16s cubic-bezier(.45, 0, .55, 1) infinite alternate;
}

.ambient-right {
    top: 330px;
    right: -180px;
    width: 430px;
    height: 430px;
    background: rgba(145, 231, 166, .38);
    animation: ambient-drift-right 19s cubic-bezier(.45, 0, .55, 1) infinite alternate;
}

@keyframes ambient-drift-left {
    from {
        transform: translate3d(0, 0, 0) scale(1);
    }

    to {
        transform: translate3d(56px, -34px, 0) scale(1.06);
    }
}

@keyframes ambient-drift-right {
    from {
        transform: translate3d(0, 0, 0) scale(1);
    }

    to {
        transform: translate3d(-52px, 38px, 0) scale(1.05);
    }
}

// 尊重系统“减少动态效果”偏好
@media (prefers-reduced-motion: reduce) {
    .ambient-left,
    .ambient-right {
        animation: none;
    }
}

// 核心价值说明
.principles {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 32px;
    max-width: 960px;
    margin: 62px auto 0;
    padding-top: 30px;
    border-top: 1px solid #d2d2d7;

    h2 {
        margin: 10px 0 8px;
        font-size: 21px;
        letter-spacing: -.03em;
    }

    p {
        margin: 0;
        color: #6e6e73;
        line-height: 1.5;
    }
}

@media (max-width: 640px) {
    .home-container {
        padding-top: 54px;
    }

    .hero-actions {
        flex-direction: column;
        padding: 0 24px;
    }

    .conversation {
        width: 70%;
        padding-top: 34px;
    }

    .robot {
        right: -2%;
        width: 180px;
        opacity: .85;
    }

    .principles {
        grid-template-columns: 1fr;
        gap: 26px;
        margin-top: 64px;
    }
}

</style>
