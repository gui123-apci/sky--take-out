-- =============================================================
-- 苍穹外卖 演示造数脚本（仅用于本地演示）
-- 作用：为营业额统计、用户统计、订单统计、销量 Top10、工作台生成演示数据
-- 前置：已导入课程资料的 sky_take_out 建表 SQL
-- 用法：mysql -uroot -p sky_take_out < sql/demo_data.sql
-- 说明：脚本可重复执行；已存在的演示数据不会重复插入；
--       脚本中的用户、手机号、地址均为虚构演示数据。
-- =============================================================
USE sky_take_out;

-- 1. 演示用户（用于用户统计：总用户数、新增用户数）
INSERT INTO user (openid, name, phone, sex, create_time)
SELECT 'demo_openid_01', '演示用户1', '13800000001', '1', DATE_SUB(NOW(), INTERVAL 9 DAY) FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM user WHERE openid = 'demo_openid_01');

INSERT INTO user (openid, name, phone, sex, create_time)
SELECT 'demo_openid_02', '演示用户2', '13800000002', '0', DATE_SUB(NOW(), INTERVAL 7 DAY) FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM user WHERE openid = 'demo_openid_02');

INSERT INTO user (openid, name, phone, sex, create_time)
SELECT 'demo_openid_03', '演示用户3', '13800000003', '1', DATE_SUB(NOW(), INTERVAL 5 DAY) FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM user WHERE openid = 'demo_openid_03');

INSERT INTO user (openid, name, phone, sex, create_time)
SELECT 'demo_openid_04', '演示用户4', '13800000004', '0', DATE_SUB(NOW(), INTERVAL 3 DAY) FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM user WHERE openid = 'demo_openid_04');

INSERT INTO user (openid, name, phone, sex, create_time)
SELECT 'demo_openid_05', '演示用户5', '13800000005', '1', DATE_SUB(NOW(), INTERVAL 2 DAY) FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM user WHERE openid = 'demo_openid_05');

INSERT INTO user (openid, name, phone, sex, create_time)
SELECT 'demo_openid_06', '演示用户6', '13800000006', '0', DATE_SUB(NOW(), INTERVAL 1 DAY) FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM user WHERE openid = 'demo_openid_06');

-- 2. 生成 1~14 天的序号
DROP TEMPORARY TABLE IF EXISTS demo_seq;
CREATE TEMPORARY TABLE demo_seq (n INT);
INSERT INTO demo_seq VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14);

-- 3. 每天一笔已完成的订单（营业额、订单统计、工作台、导出报表都依赖已完成订单）
INSERT INTO orders (number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status,
                    amount, phone, address, consignee, delivery_status, pack_amount,
                    tableware_number, tableware_status)
SELECT CONCAT('DEMO', DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL s.n DAY), '%Y%m%d')),
       5,
       (SELECT id FROM user ORDER BY id LIMIT 1),
       1,
       TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL s.n DAY), '12:00:00'),
       TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL s.n DAY), '12:35:00'),
       1, 1, 60.00 + s.n * 5,
       '13800138000', '演示地址1号', '演示用户', 1, 0, 1, 1
FROM demo_seq s
WHERE NOT EXISTS (
    SELECT 1 FROM orders o
    WHERE o.number = CONCAT('DEMO', DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL s.n DAY), '%Y%m%d'))
);

-- 4. 为演示订单补订单明细（销量 Top10 依赖订单明细；需要数据库中存在 id 为 65/66/67 的菜品）
INSERT INTO order_detail (name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount)
SELECT d.name, d.image, o.id, d.id, NULL, NULL, 1 + MOD(o.id, 3), d.price
FROM orders o
JOIN dish d ON d.id IN (65, 66, 67)
WHERE o.number LIKE 'DEMO%'
  AND NOT EXISTS (SELECT 1 FROM order_detail od WHERE od.order_id = o.id);

DROP TEMPORARY TABLE IF EXISTS demo_seq;

-- =============================================================
-- 如需清理演示数据，手动执行以下语句：
-- DELETE FROM order_detail WHERE order_id IN (SELECT id FROM orders WHERE number LIKE 'DEMO%');
-- DELETE FROM orders WHERE number LIKE 'DEMO%';
-- DELETE FROM user WHERE openid LIKE 'demo_openid_%';
-- =============================================================
