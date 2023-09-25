# JMeter Load Test Guide

## 1. 目标接口

- `POST /admin/login`
- `GET /repair/find?pageNum=1&pageSize=10`
- `GET /adjustRoom/find?pageNum=1&pageSize=10`

## 2. 采样建议

- 线程数：`50`
- Ramp-Up：`20s`
- 持续时间：`5m`
- 结果导出：CSV（JTL）

## 3. 指标提取

```bash
python3 scripts/evaluation/jmeter_summary.py /path/to/result.jtl
```

输出指标：
- `SuccessRate`
- `AvgMs`
- `P95Ms`
- `P99Ms`

## 4. 基准建议

- 登录接口：`P95 < 300ms`
- 查询接口：`P95 < 500ms`
- 成功率：`>= 99%`
