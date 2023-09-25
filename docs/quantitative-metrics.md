# DormLink 可量化指标获取路径

## 1. 响应时间指标（JMeter）

### 1.1 压测范围

- 登录：`POST /admin/login`
- 报修分页：`GET /repair/find`
- 调宿分页：`GET /adjustRoom/find`

### 1.2 数据产出路径

1. 使用 JMeter 执行压测，导出 JTL（CSV）结果。
2. 运行脚本汇总响应时间指标：

```bash
python3 scripts/evaluation/jmeter_summary.py /path/to/result.jtl
```

3. 输出指标用于简历与报告：
- `SuccessRate`
- `AvgMs`
- `P95Ms`
- `P99Ms`

## 2. 报修/调宿平均处理时长

### 2.1 SQL 指标脚本

- 指标脚本：`evaluation/sql/processing_time_metrics.sql`
- 统计口径：
- 报修平均处理时长：`order_buildtime -> order_finishtime`
- 调宿平均处理时长：`apply_time -> finish_time`

### 2.2 建议基准（可按项目复盘调整）

- 报修平均处理时长：`<= 24h`
- 调宿平均处理时长：`<= 48h`
- 报修完成率：`>= 90%`
- 调宿通过率：`>= 85%`

## 3. ECharts 展示指标维度

当前首页柱状图和统计卡可沉淀为以下可量化维度：

- `building_student_distribution`
- 含义：每栋楼在住人数分布
- 接口：`GET /building/getBuildingName` + `GET /room/getEachBuildingStuNum/{num}`

- `student_total`
- 含义：系统学生总量
- 接口：`GET /stu/stuNum`

- `repair_total`
- 含义：报修单总数
- 接口：`GET /repair/orderNum`

- `room_not_full_total`
- 含义：未满房间数量
- 接口：`GET /room/noFullRoom`

## 4. 证据链建议

简历或项目说明中建议给出一条完整证据链：

1. 压测记录（JTL + 汇总指标）。
2. 业务处理时长 SQL 统计结果截图。
3. ECharts 指标维度与接口映射说明。
