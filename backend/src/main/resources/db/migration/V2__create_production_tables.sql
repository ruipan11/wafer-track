-- 批號
CREATE TABLE lot (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    lot_no              VARCHAR(32)  NOT NULL,
    product_id          BIGINT       NOT NULL,
    route_id            BIGINT       NOT NULL,
    current_step_seq    INT          NULL, -- 目前停在第幾站
    status              VARCHAR(16)  NOT NULL DEFAULT 'WAITING',
    qty                 INT          NOT NULL,
    priority            INT          NOT NULL DEFAULT 5,
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_lot_no (lot_no),
    KEY idx_lot_product (product_id),
    KEY idx_lot_route (route_id),
    KEY idx_lot_status (status),
    CONSTRAINT fk_lot_product FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT fk_lot_route FOREIGN KEY (route_id) REFERENCES route (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批號';

-- 晶圓
CREATE TABLE wafer (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    wafer_no        VARCHAR(32)  NOT NULL,
    lot_id          BIGINT       NOT NULL,
    slot_no         INT          NOT NULL,
    status          VARCHAR(16)  NOT NULL DEFAULT 'NORMAL',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_wafer_no (wafer_no),
    UNIQUE KEY uk_wafer_slot (lot_id, slot_no),
    CONSTRAINT fk_wafer_lot FOREIGN KEY (lot_id) REFERENCES lot (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='晶圓';

-- 過站紀錄
CREATE TABLE lot_history (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    lot_id              BIGINT       NOT NULL,
    step_seq            INT          NOT NULL,
    step_name           VARCHAR(64)  NOT NULL, -- 反正規化，直接存入 step_name
    workstation_id      BIGINT       NOT NULL,
    equipment_id        BIGINT       NOT NULL,
    track_in_time       DATETIME     NOT NULL,
    track_in_qty        INT          NOT NULL,
    track_in_operator   VARCHAR(32)  NOT NULL,
    track_out_time      DATETIME     NULL, -- NULL代表 lot 現在還在機台上
    track_out_qty       INT          NULL,
    track_out_operator  VARCHAR(32)  NULL,
    scrap_qty           INT          NOT NULL DEFAULT 0,
    defect_code         VARCHAR(32)  NULL,
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_history_lot (lot_id),
    KEY idx_history_eqp (equipment_id),
    KEY idx_history_lot_open (lot_id, track_out_time),
    CONSTRAINT fk_history_lot FOREIGN KEY (lot_id) REFERENCES lot (id),
    CONSTRAINT fk_history_ws FOREIGN KEY (workstation_id) REFERENCES workstation (id),
    CONSTRAINT fk_history_eqp FOREIGN KEY (equipment_id) REFERENCES equipment (id)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='過站紀錄';

-- Hold紀錄
CREATE TABLE lot_hold (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    lot_id              BIGINT       NOT NULL,
    hold_reason_code    VARCHAR(32)  NOT NULL,
    hold_comment        VARCHAR(255) NULL,
    hold_step_seq       INT          NULL,
    hold_by             VARCHAR(32)  NOT NULL,
    hold_time           DATETIME     NOT NULL,
    release_by          VARCHAR(32)  NULL,
    release_time        DATETIME     NULL,
    release_comment     VARCHAR(255) NULL,
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_hold_lot (lot_id),
    KEY idx_hold_active (lot_id, release_time),
    CONSTRAINT fk_hold_lot FOREIGN KEY (lot_id) REFERENCES lot (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Hold紀錄';