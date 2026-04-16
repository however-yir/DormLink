<template>
  <div class="header-shell">
    <div class="header-title-group">
      <p class="header-title">宿舍管理工作台</p>
      <p class="header-subtitle">DormLink Campus Operations</p>
    </div>

    <div class="header-center">
      <Clock class="clock-widget"/>
    </div>

    <div class="right-info">
      <el-dropdown trigger="click">
        <span class="user-trigger">
          <el-avatar :size="30" :src="avatarUrl"/>
          <span class="user-name">{{ displayName }}</span>
          <el-icon class="el-icon--right"><arrow-down/></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="selfInfoManage">个人信息</el-dropdown-item>
            <el-dropdown-item @click="SignOut">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";
import Clock from "@/components/Clock";

const {ElMessage} = require("element-plus");

export default {
  name: "Header",
  components: {
    Clock
  },
  data() {
    return {
      name: '',
      displayName: "个人中心",
      avatarUrl: "",
    }
  },
  created() {
    const cache = JSON.parse(window.sessionStorage.getItem("user") || "null");
    if (cache) {
      const user = cache.user || cache;
      this.displayName = cache.displayName || user.name || user.username || "个人中心";
      this.avatarUrl = cache.avatar || user.avatar || "";
    }
  },
  methods: {
    SignOut() {
      sessionStorage.clear()
      request.get("/main/signOut");
      ElMessage({
        message: '用户退出登录',
        type: 'success',
      });
      this.$router.replace({path: '/login'});
    },
    selfInfoManage() {
      this.$router.push("/selfInfo")
    }
  },
}
</script>

<style scoped>
.header-shell {
  width: 100%;
  height: 64px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  position: relative;
}

.header-title-group {
  min-width: 236px;
}

.header-title {
  font-size: 18px;
  font-weight: 800;
  color: #17427b;
  line-height: 1.1;
}

.header-subtitle {
  margin-top: 4px;
  font-size: 12px;
  color: #6f86a5;
}

.header-center {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.clock-widget {
  font-size: 18px;
}

.right-info {
  margin-left: auto;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-trigger {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  background: #fff;
  border: 1px solid var(--line-soft);
  border-radius: 999px;
  padding: 6px 10px 6px 6px;
  box-shadow: 0 8px 20px rgba(17, 57, 106, 0.07);
}

.user-name {
  color: #2d3f5f;
  font-weight: 700;
  max-width: 128px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.right-info:hover {
  cursor: pointer;
}

@media (max-width: 960px) {
  .header-title-group {
    display: none;
  }

  .header-center {
    position: static;
    transform: none;
    margin-right: auto;
  }
}
</style>
