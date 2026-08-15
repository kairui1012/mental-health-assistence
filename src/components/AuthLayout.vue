<template>
    <div class="auth-layout">
        <div class="left-section">
            <div class="content">
                <h2 class="title">
                    <span class="title-highlight">心理AI</span><span class="title-dark">助手</span>
                </h2>
                <p class="text">
                    陪你聊聊<span class="highlight">心事</span>，在每一个需要<span class="highlight">被理解的时刻</span>，给自己一份温柔而安心的<span class="highlight">陪伴</span>。
                </p>
                <div class="robot">
                    <el-image style="width: 90px; height: 90px;" :src="imageURL" alt="心理AI助手" />
                </div>
            </div>
        </div>
        <div class="right-section">
            <router-view />
        </div>
    </div>
</template>

<style lang="scss" scoped>
.auth-layout {
    display: flex;

    .left-section {
        display: flex;
        justify-content: center;
        align-items: center;
        flex: 1;
        background: #fff;
        height: 100vh;

        .content {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            transform: translateY(0);
            font-family: Inter, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", "Noto Sans SC", "Microsoft YaHei", sans-serif;

            .title {
                margin-bottom: 20px;
                font-size: 48px;
                font-weight: 800;
                letter-spacing: .02em;
                color: #1d1d1f;

                .title-highlight {
                    background: none;
                    color: #1d1d1f;
                }

                .title-dark {
                    color: #1d1d1f;
                }
            }

            .text {
                margin-bottom: 70px;
                width: 460px;
                font-size: 22px;
                font-weight: 600;
                letter-spacing: .02em;
                color: #1d1d1f;
                text-align: center;

                .highlight {
                    background: linear-gradient(100deg, #d4b93e, #9bc545 48%, #689f38);
                    -webkit-background-clip: text;
                    background-clip: text;
                    color: transparent;
                }
            }

            .robot {
                position: relative;
                display: flex;
                justify-content: center;
                align-items: center;
                width: 364px;
                height: 364px;
                border-radius: 50%;
                transform: translateY(-5%);
                box-shadow: none;

                &::before {
                    position: absolute;
                    inset: 8px;
                    border-radius: inherit;
                    background: radial-gradient(circle, #8dca1c 0%, rgba(145, 199, 30, .92) 24%, rgba(180, 220, 73, .56) 47%, rgba(220, 241, 159, .2) 70%, rgba(255, 255, 255, 0) 100%);
                    content: '';
                    filter: blur(10px);
                    transform: scale(1.08);
                    animation: logo-glow-breathe 6.5s ease-in-out infinite;
                }

                :deep(.el-image) {
                    position: relative;
                    z-index: 1;
                    width: 171px !important;
                    height: 171px !important;
                }
            }
        }
    }

    .right-section {
        display: flex;
        justify-content: center;
        align-items: center;
        flex: 1;
        height: 100vh;
        background-color: #fff;
    }
}

@keyframes logo-glow-breathe {
    0%, 100% {
        filter: blur(10px) brightness(1) saturate(1);
        opacity: .6;
        transform: scale(1.06);
    }

    50% {
        filter: blur(10px) brightness(.92) saturate(1.08);
        opacity: 1;
        transform: scale(1.12);
    }
}

@media (prefers-reduced-motion: reduce) {
    .auth-layout .robot::before {
        animation: none;
    }
}
</style>

<script setup>
const imageURL = new URL("@/assets/images/robot-fill.png", import.meta.url).href
</script>
