<template>
    <div class="home-page">
        <section class="stats-shell">
            <el-row :gutter="18" class="topInfo">
                <el-col :md="6" :sm="12" :xs="24">
                    <div id="stuNumDiv" class="metric-card">
                        <div id="ssv1-main-text" class="nowDiv">实时</div>
                        <span class="title">学生统计</span>
                        <span class="digital">{{ this.studentNum }}</span>
                        <span class="last-span">当前分类总记录数</span>
                    </div>
                </el-col>
                <el-col :md="6" :sm="12" :xs="24">
                    <div id="haveRoomDiv" class="metric-card">
                        <div id="ssv2-main-text" class="nowDiv">实时</div>
                        <span class="title">住宿人数</span>
                        <span class="digital">{{ this.haveRoomStudentNum }}</span>
                        <span class="last-span">当前分类总记录数</span>
                    </div>
                </el-col>
                <el-col :md="6" :sm="12" :xs="24">
                    <div id="repairNum" class="metric-card">
                        <div id="ssv3-main-text" class="nowDiv">实时</div>
                        <span class="title">报修统计</span>
                        <span class="digital">{{ this.repairOrderNum }}</span>
                        <span class="last-span">当前分类总记录数</span>
                    </div>
                </el-col>
                <el-col :md="6" :sm="12" :xs="24">
                    <div id="emptyRoom" class="metric-card">
                        <div id="ssv4-main-text" class="nowDiv">实时</div>
                        <span class="title">空宿舍统计</span>
                        <span class="digital">{{ this.noFullRoomNum }}</span>
                        <span class="last-span">当前分类总记录数</span>
                    </div>
                </el-col>
            </el-row>
        </section>

        <section class="workspace-shell">
            <article class="notice-panel">
                <h3>宿舍通告</h3>
                <el-timeline>
                    <el-timeline-item
                        v-for="(activity, index) in activities.slice(0, 8)"
                        :key="index"
                        :timestamp="activity.releaseTime"
                    >
                        <span class="notice-title">{{ activity.title }}</span>
                    </el-timeline-item>
                </el-timeline>
            </article>

            <article class="chart-panel">
                <h3>宿舍学生人数分布</h3>
                <div class="chart-container">
                    <home_echarts/>
                </div>
            </article>

            <aside class="right-shell">
                <weather/>
                <div class="calendar-panel">
                    <Calender/>
                </div>
            </aside>
        </section>
    </div>
</template>

<script src="../assets/js/Home.js"></script>
<style scoped>@import '../assets/css/Home.css';</style>
