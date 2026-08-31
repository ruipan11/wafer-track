-- 工站
INSERT INTO workstation (ws_code, ws_name, description) VALUES
('CLEAN',   'Wet clean',        '濕式清洗'),
('FURNACE', 'Diffusion furnace','擴散爐管'),
('LITHO',   'Photolithography', '微影'),
('ETCH',    'Dry etch',         '乾式蝕刻'),
('CVD',     'Metal deposition', '金屬沉積'),
('CMP',     'Planarization',    '化學機械研磨'),
('METRO',   'Metrology',        '量測');

-- 機台
INSERT INTO equipment (eqp_code, eqp_name, workstation_id, status) VALUES
('EQP-CLEAN-01', 'Wet bench 1',   (SELECT id FROM workstation WHERE ws_code='CLEAN'),   'IDLE'),
('EQP-CLEAN-02', 'Wet bench 2',   (SELECT id FROM workstation WHERE ws_code='CLEAN'),   'IDLE'),
('EQP-FURN-01',  'Furnace 1',     (SELECT id FROM workstation WHERE ws_code='FURNACE'), 'IDLE'),
('EQP-LITHO-01', 'Scanner 1',     (SELECT id FROM workstation WHERE ws_code='LITHO'),   'IDLE'),
('EQP-LITHO-02', 'Scanner 2',     (SELECT id FROM workstation WHERE ws_code='LITHO'),   'PM'),
('EQP-ETCH-01',  'Etcher 1',      (SELECT id FROM workstation WHERE ws_code='ETCH'),    'IDLE'),
('EQP-ETCH-02',  'Etcher 2',      (SELECT id FROM workstation WHERE ws_code='ETCH'),    'DOWN'),
('EQP-CVD-01',   'CVD chamber 1', (SELECT id FROM workstation WHERE ws_code='CVD'),     'IDLE'),
('EQP-CMP-01',   'Polisher 1',    (SELECT id FROM workstation WHERE ws_code='CMP'),     'IDLE'),
('EQP-METRO-01', 'Ellipsometer 1',(SELECT id FROM workstation WHERE ws_code='METRO'),   'IDLE');

-- 途程
INSERT INTO route (route_code, route_name, version) VALUES
('ROUTE-A-V1', 'Product A standard flow', '1.0');

-- 途程步驟
INSERT INTO route_step (route_id, step_seq, step_name, workstation_id, layer_no, std_cycle_time) VALUES
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  10, 'Initial clean',    (SELECT id FROM workstation WHERE ws_code='CLEAN'),   NULL, 1800),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  20, 'Oxidation',        (SELECT id FROM workstation WHERE ws_code='FURNACE'), 1,    7200),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  30, 'Photo layer 1',    (SELECT id FROM workstation WHERE ws_code='LITHO'),   1,    3600),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  40, 'Etch layer 1',     (SELECT id FROM workstation WHERE ws_code='ETCH'),    1,    2700),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  50, 'Post-etch clean',  (SELECT id FROM workstation WHERE ws_code='CLEAN'),   1,    1800),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  60, 'Metal deposition', (SELECT id FROM workstation WHERE ws_code='CVD'),     2,    5400),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  70, 'Photo layer 2',    (SELECT id FROM workstation WHERE ws_code='LITHO'),   2,    3600),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  80, 'Etch layer 2',     (SELECT id FROM workstation WHERE ws_code='ETCH'),    2,    2700),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'),  90, 'Post-etch clean',  (SELECT id FROM workstation WHERE ws_code='CLEAN'),   2,    1800),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'), 100, 'CMP',              (SELECT id FROM workstation WHERE ws_code='CMP'),     2,    3600),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'), 110, 'Metrology',        (SELECT id FROM workstation WHERE ws_code='METRO'),   2,     900),
((SELECT id FROM route WHERE route_code='ROUTE-A-V1'), 120, 'Final clean',      (SELECT id FROM workstation WHERE ws_code='CLEAN'),   NULL, 1800);

-- 產品
INSERT INTO product (product_code, product_name, tech_node, route_id) VALUES
('PROD-A', 'Logic device A', '28nm', (SELECT id FROM route WHERE route_code='ROUTE-A-V1'));

-- 批號
INSERT INTO lot (lot_no, product_id, route_id, current_step_seq, status, qty, priority) VALUES
('LOT-20260801-001',
 (SELECT id FROM product WHERE product_code='PROD-A'),
 (SELECT id FROM route WHERE route_code='ROUTE-A-V1'),
 10, 'WAITING', 25, 5),
('LOT-20260801-002',
 (SELECT id FROM product WHERE product_code='PROD-A'),
 (SELECT id FROM route WHERE route_code='ROUTE-A-V1'),
 50, 'WAITING', 25, 5),
('LOT-20260801-003',
 (SELECT id FROM product WHERE product_code='PROD-A'),
 (SELECT id FROM route WHERE route_code='ROUTE-A-V1'),
 70, 'WAITING', 25, 1);

-- 晶圓（每個 lot 25 片）
INSERT INTO wafer (wafer_no, lot_id, slot_no, status)
SELECT CONCAT(l.lot_no, '-', LPAD(s.slot, 2, '0')), l.id, s.slot, 'NORMAL'
FROM lot l
CROSS JOIN (
    SELECT 1 AS slot UNION ALL SELECT 2  UNION ALL SELECT 3  UNION ALL SELECT 4  UNION ALL SELECT 5
    UNION ALL SELECT 6  UNION ALL SELECT 7  UNION ALL SELECT 8  UNION ALL SELECT 9  UNION ALL SELECT 10
    UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
    UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
    UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25
) s;