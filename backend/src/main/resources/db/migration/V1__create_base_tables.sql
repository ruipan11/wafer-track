-- 工站 （機台群組）
CREATE TABLE workstation (
    id              BIGINT       NOT NULL AUTO_INCREMENT, -- 避免溢位風險
    ws_code         VARCHAR(32)  NOT NULL,
    ws_name         VARCHAR(64)  NOT NULL,
    description     VARCHAR(255) NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_workstation_code (ws_code) -- 同一條 route 裡 step_seq 不能重複
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工站'; -- 完整的 UTF-8，能存中文和 emoji

-- 機台
CREATE TABLE equipment (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    eqp_code        VARCHAR(32)  NOT NULL,
    eqp_name        VARCHAR(64)  NOT NULL,
    workstation_id  BIGINT       NOT NULL,
    status          VARCHAR(16)  NOT NULL DEFAULT 'IDLE',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_equipment_code (eqp_code),
    KEY idx_equipment_ws (workstation_id),
    CONSTRAINT fk_equipment_ws FOREIGN KEY (workstation_id) REFERENCES workstation (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='機台';

-- 途程主檔
CREATE TABLE route (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    route_code      VARCHAR(32)  NOT NULL,
    route_name      VARCHAR(64)  NOT NULL,
    version         VARCHAR(16)  NOT NULL DEFAULT '1.0',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_route_code (route_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='途程主檔';

-- 產品
CREATE TABLE product (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    product_code    VARCHAR(32)  NOT NULL,
    product_name    VARCHAR(64)  NOT NULL,
    tech_node       VARCHAR(16)  NULL, -- 技術節點
    route_id        BIGINT       NOT NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_product_code (product_code),
    KEY idx_product_route (route_id),
    CONSTRAINT fk_product_route FOREIGN KEY (route_id) REFERENCES route (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='產品';

-- 途程步驟
CREATE TABLE route_step (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    route_id        BIGINT       NOT NULL,
    step_seq        INT          NOT NULL,
    step_name       VARCHAR(64)  NOT NULL,
    workstation_id  BIGINT       NOT NULL,
    layer_no        INT          NULL,
    std_cycle_time  INT          NULL, -- 標準工時（秒）
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_route_step (route_id, step_seq),
    KEY idx_route_step_ws (workstation_id),
    CONSTRAINT fk_route_step_route FOREIGN KEY (route_id) REFERENCES route (id),
    CONSTRAINT fk_route_step_ws FOREIGN KEY (workstation_id) REFERENCES workstation (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='途程步驟';