<template>
  <el-menu
      :default-active="this.path"
      router
      class="nav-menu"
      unique-opened
  >
    <div class="brand-block">
      <img alt="" class="brand-logo" src="@/assets/logo.png">
      <div class="brand-copy">
        <span class="brand-title">DormLink</span>
        <span class="brand-subtitle">校园宿舍系统</span>
      </div>
    </div>
    <el-menu-item index="/home">
      <el-icon>
        <house/>
      </el-icon>
      <span>首页</span>
    </el-menu-item>
    <el-sub-menu v-if="this.judgeIdentity()!==0" index="2">
      <template #title>
        <el-icon>
          <user/>
        </el-icon>
        <span>用户管理</span>
      </template>
      <el-menu-item v-if="this.judgeIdentity()!==0" index="/stuInfo">学生信息</el-menu-item>
      <el-menu-item v-if="this.judgeIdentity()===2" index="/dormManagerInfo">宿管信息</el-menu-item>
    </el-sub-menu>
    <el-sub-menu v-if="this.judgeIdentity()!==0" index="3">
      <template #title>
        <el-icon>
          <coin/>
        </el-icon>
        <span>宿舍管理</span>
      </template>
      <el-menu-item v-if="this.judgeIdentity()!==0" index="/buildingInfo">楼宇信息</el-menu-item>
      <el-menu-item v-if="this.judgeIdentity()!==0" index="/roomInfo">房间信息</el-menu-item>
    </el-sub-menu>
    <el-sub-menu v-if="this.judgeIdentity()!==0" index="4">
      <template #title>
        <el-icon>
          <message/>
        </el-icon>
        <span>信息管理</span>
      </template>
      <el-menu-item v-if="this.judgeIdentity()===2" index="/noticeInfo">公告信息</el-menu-item>
      <el-menu-item v-if="this.judgeIdentity()!==0" index="/repairInfo">报修信息</el-menu-item>
    </el-sub-menu>
    <el-sub-menu v-if="this.judgeIdentity()!==0" index="5">
      <template #title>
        <el-icon>
          <pie-chart/>
        </el-icon>
        <span>申请管理</span>
      </template>
      <el-menu-item v-if="this.judgeIdentity()!==0" index="/adjustRoomInfo">调宿申请</el-menu-item>
    </el-sub-menu>
    <el-menu-item v-if="this.judgeIdentity()!==0" index="/visitorInfo">
      <svg class="icon" data-v-042ca774="" style="height: 18px; margin-right: 11px;"
           viewBox="0 0 1024 1024"
           xmlns="http://www.w3.org/2000/svg">
        <path
            d="M512 160c320 0 512 352 512 352S832 864 512 864 0 512 0 512s192-352 512-352zm0 64c-225.28 0-384.128 208.064-436.8 288 52.608 79.872 211.456 288 436.8 288 225.28 0 384.128-208.064 436.8-288-52.608-79.872-211.456-288-436.8-288zm0 64a224 224 0 110 448 224 224 0 010-448zm0 64a160.192 160.192 0 00-160 160c0 88.192 71.744 160 160 160s160-71.808 160-160-71.744-160-160-160z"
            fill="currentColor"></path>
      </svg>
      <span>访客管理</span>
    </el-menu-item>
    <el-menu-item v-if="this.judgeIdentity()===0" index="/myRoomInfo">
      <el-icon>
        <school/>
      </el-icon>
      <span>我的宿舍</span>
    </el-menu-item>
    <el-menu-item v-if="this.judgeIdentity()===0" index="/applyChangeRoom">
      <el-icon>
        <takeaway-box/>
      </el-icon>
      <span>申请调宿</span>
    </el-menu-item>
    <el-menu-item v-if="this.judgeIdentity()===0" index="/applyRepairInfo">
      <el-icon>
        <set-up/>
      </el-icon>
      <span>报修申请</span>
    </el-menu-item>
    <el-menu-item index="/selfInfo">
      <el-icon>
        <setting/>
      </el-icon>
      <span>个人信息</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";

export default {
  name: "Aside",
  data() {
    return {
      user: {},
      identity: '',
      path: this.$route.path
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      request.get("/main/loadIdentity").then((res) => {
        if (res.code !== "0") {
          ElMessage({
            message: '用户会话过期',
            type: 'error',
          });
          sessionStorage.clear()
          request.get("/main/signOut");

        }
        window.sessionStorage.setItem("identity", JSON.stringify(res.data));
        this.identity = res.data
      });
      request.get("/main/loadUserInfo").then((result) => {
        if (result.code !== "0") {
          ElMessage({
            message: '用户会话过期',
            type: 'error',
          });
          request.get("/main/signOut");
          sessionStorage.clear()
          this.$router.replace({path: "/login"});
        }
        window.sessionStorage.setItem("user", JSON.stringify(result.data));
        this.user = result.data
      });
    },
    judgeIdentity() {
      if (this.identity === 'stu') {
        return 0
      } else if (this.identity === 'dormManager') {
        return 1
      } else
        return 2
    }
  },
}
</script>

<style scoped>
.nav-menu {
  width: 220px;
  height: 100%;
  min-height: 100vh;
  border-right: 0;
  background: transparent;
}

.nav-menu :deep(.el-menu-item),
.nav-menu :deep(.el-sub-menu__title) {
  color: rgba(237, 245, 255, 0.86);
  border-radius: 10px;
  margin: 4px 10px;
  height: 46px;
  line-height: 46px;
}

.nav-menu :deep(.el-sub-menu .el-menu-item) {
  margin: 2px 10px;
  min-width: 180px;
  height: 42px;
  line-height: 42px;
  padding-left: 58px !important;
}

.nav-menu :deep(.el-menu) {
  background: transparent;
  border-right: 0;
}

.nav-menu :deep(.el-menu-item:hover),
.nav-menu :deep(.el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.nav-menu :deep(.el-menu-item.is-active) {
  color: #fff;
  background: linear-gradient(120deg, rgba(48, 135, 255, 0.9), rgba(67, 202, 255, 0.85));
  box-shadow: 0 8px 18px rgba(44, 136, 255, 0.3);
}

.brand-block {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 14px 12px;
  margin-bottom: 10px;
}

.brand-logo {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.18);
  padding: 6px;
}

.brand-copy {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.brand-title {
  font-size: 16px;
  font-weight: 800;
  color: #fff;
}

.brand-subtitle {
  font-size: 12px;
  color: rgba(221, 236, 255, 0.82);
}

.icon {
  margin-right: 6px;
}

@media (max-width: 960px) {
  .nav-menu {
    width: 76px;
  }

  .brand-copy {
    display: none;
  }
}
</style>
