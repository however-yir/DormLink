-- DormLink processing metrics SQL
-- 数据库：wms

-- 1) 报修平均处理时长（小时）
SELECT
  ROUND(AVG(TIMESTAMPDIFF(MINUTE,
    STR_TO_DATE(order_buildtime, '%Y-%m-%d %H:%i:%s'),
    STR_TO_DATE(order_finishtime, '%Y-%m-%d %H:%i:%s')))/60, 2) AS repair_avg_hours
FROM repair
WHERE state = '完成'
  AND order_buildtime IS NOT NULL
  AND order_finishtime IS NOT NULL;

-- 2) 调宿平均处理时长（小时）
SELECT
  ROUND(AVG(TIMESTAMPDIFF(MINUTE,
    STR_TO_DATE(apply_time, '%Y-%m-%d %H:%i:%s'),
    STR_TO_DATE(finish_time, '%Y-%m-%d %H:%i:%s')))/60, 2) AS adjust_avg_hours
FROM adjust_room
WHERE state IN ('通过', '完成')
  AND apply_time IS NOT NULL
  AND finish_time IS NOT NULL;

-- 3) 调宿通过率
SELECT
  COUNT(*) AS total_adjust,
  SUM(CASE WHEN state IN ('通过', '完成') THEN 1 ELSE 0 END) AS passed_adjust,
  ROUND(SUM(CASE WHEN state IN ('通过', '完成') THEN 1 ELSE 0 END) / COUNT(*), 4) AS pass_rate
FROM adjust_room;

-- 4) 报修完成率
SELECT
  COUNT(*) AS total_repair,
  SUM(CASE WHEN state = '完成' THEN 1 ELSE 0 END) AS finished_repair,
  ROUND(SUM(CASE WHEN state = '完成' THEN 1 ELSE 0 END) / COUNT(*), 4) AS completion_rate
FROM repair;
