<template>
  <el-container class="app-shell">
    <el-aside width="220px" class="shell-aside">
      <Aside/>
    </el-aside>
    <el-container>
      <el-header class="shell-header">
        <Header/>
      </el-header>
      <el-main class="shell-main">
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Header from "@/components/Header";
import Aside from "@/components/Aside";

export default {
  name: "layout",
  components: {
    Header,
    Aside,
  },
}
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
  background: transparent;
}

.shell-aside {
  background: linear-gradient(200deg, #11203a 0%, #162b49 52%, #183a68 100%);
  box-shadow: 8px 0 28px rgba(16, 40, 76, 0.18);
  z-index: 5;
}

.shell-header {
  height: 64px !important;
  padding: 0;
  border-bottom: 1px solid var(--line-soft);
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(8px);
}

.shell-main {
  padding: 16px;
}

@media (max-width: 960px) {
  .shell-aside {
    width: 76px !important;
  }
}
</style>
