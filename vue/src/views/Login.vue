<template>
    <div class="login-page">
        <div class="ambient-blur"></div>

        <section class="login-hero">
            <p class="hero-eyebrow">DormLink Campus Suite</p>
            <h1>校园宿舍业务，统一在一个工作台里完成。</h1>
            <p class="hero-copy">学生入住、调宿审批、报修处理与访客登记协同管理，流程清晰，角色隔离，数据实时可见。</p>
            <div class="hero-tags">
                <span>学生端</span>
                <span>宿管端</span>
                <span>后勤管理端</span>
            </div>
        </section>

        <section class="login-panel">
            <h2>欢迎登录</h2>
            <p class="panel-subtitle">请输入账号信息并选择身份</p>

            <el-form ref="form" :model="form" :rules="rules" size="large" class="login-form">
                <el-form-item prop="username">
                    <el-input v-model="form.username" clearable placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="form.password" show-password placeholder="密码"></el-input>
                </el-form-item>
                <el-form-item :model="form" prop="identity" class="identity-wrap">
                    <el-radio-group v-model="form.identity" class="identity-group">
                        <el-radio label="stu">学生</el-radio>
                        <el-radio label="dormManager">宿舍管理员</el-radio>
                        <el-radio label="admin">后勤管理中心</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button :disabled="!disabled" class="submit-btn" type="primary" @click="login">登 录</el-button>
                </el-form-item>
            </el-form>
        </section>
    </div>

    <div class="footer">
        © Copyright 2021-2026 DormLink
    </div>
</template>

<script src="../assets/js/Login.js"></script>

<style scoped>
.login-page {
    position: fixed;
    inset: 0;
    display: grid;
    grid-template-columns: 1.2fr 0.9fr;
    align-items: center;
    gap: 8vw;
    padding: 7vh 8vw;
    overflow: hidden;
    background:
            radial-gradient(1200px 480px at 14% 14%, rgba(129, 217, 255, 0.45), transparent 58%),
            radial-gradient(840px 420px at 86% 86%, rgba(69, 131, 255, 0.36), transparent 62%),
            linear-gradient(140deg, #08172d 0%, #102a4b 52%, #17365f 100%);
}

.ambient-blur {
    position: absolute;
    width: 540px;
    height: 540px;
    border-radius: 50%;
    right: -120px;
    top: -120px;
    background: radial-gradient(circle, rgba(95, 215, 255, 0.48), rgba(95, 215, 255, 0));
    animation: drift 12s ease-in-out infinite alternate;
}

.login-hero {
    color: #fff;
    max-width: 620px;
    z-index: 2;
    animation: fadeUp 0.9s ease-out both;
}

.hero-eyebrow {
    letter-spacing: 0.14em;
    font-size: 12px;
    text-transform: uppercase;
    color: rgba(198, 232, 255, 0.88);
    margin-bottom: 18px;
    font-weight: 700;
}

.login-hero h1 {
    font-size: clamp(34px, 3.3vw, 54px);
    line-height: 1.12;
    font-weight: 800;
}

.hero-copy {
    margin-top: 18px;
    max-width: 560px;
    color: rgba(223, 240, 255, 0.9);
    font-size: 16px;
    line-height: 1.75;
}

.hero-tags {
    margin-top: 28px;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

.hero-tags span {
    padding: 8px 14px;
    border-radius: 999px;
    background: rgba(255, 255, 255, 0.14);
    border: 1px solid rgba(255, 255, 255, 0.22);
    color: #f4fbff;
    font-size: 13px;
    font-weight: 600;
}

.login-panel {
    z-index: 2;
    background: rgba(255, 255, 255, 0.96);
    border: 1px solid rgba(176, 208, 245, 0.62);
    border-radius: 26px;
    box-shadow: 0 20px 44px rgba(5, 30, 68, 0.22);
    padding: 34px 30px 24px;
    min-width: 360px;
    animation: fadeUp 1.05s ease-out both;
}

.login-panel h2 {
    color: #13376a;
    font-size: 28px;
    font-weight: 800;
}

.panel-subtitle {
    margin-top: 8px;
    color: #657a99;
    font-size: 14px;
}

.login-form {
    margin-top: 18px;
}

.identity-wrap :deep(.el-form-item__content) {
    line-height: 1.3;
}

.identity-group {
    width: 100%;
    display: grid;
    grid-template-columns: 1fr;
    gap: 8px;
    border: 1px solid #d6e3f4;
    border-radius: 12px;
    padding: 12px 12px 2px;
    background: #f8fbff;
}

.identity-group :deep(.el-radio) {
    margin-right: 0;
    color: #355174;
}

.submit-btn {
    width: 100%;
    height: 44px;
    border-radius: 11px;
    font-weight: 700;
    letter-spacing: 0.08em;
}

.footer {
    left: 0;
    right: 0;
    bottom: 14px;
    position: fixed;
    text-align: center;
    color: rgba(233, 245, 255, 0.8);
    font-size: 13px;
    z-index: 3;
}

@keyframes fadeUp {
    from {
        opacity: 0;
        transform: translateY(18px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes drift {
    from {
        transform: translate(0, 0) scale(1);
    }
    to {
        transform: translate(-32px, 24px) scale(1.05);
    }
}

@media (max-width: 1024px) {
    .login-page {
        grid-template-columns: 1fr;
        gap: 30px;
        padding: 60px 24px 90px;
        overflow-y: auto;
    }

    .login-hero {
        max-width: 100%;
    }

    .login-panel {
        min-width: auto;
        width: 100%;
        max-width: 560px;
    }
}

@media (max-width: 768px) {
    .login-hero h1 {
        font-size: 32px;
    }

    .hero-copy {
        font-size: 15px;
    }
}
</style>
