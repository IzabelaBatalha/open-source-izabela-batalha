CREATE TABLE workations (
    workation_id   VARCHAR(36)  NOT NULL,
    employee       VARCHAR(255) NOT NULL,
    origin         VARCHAR(255) NOT NULL,
    destination    VARCHAR(255) NOT NULL,
    start_date     DATE         NOT NULL,
    end_date       DATE         NOT NULL,
    working_days   INT          NOT NULL,
    risk           VARCHAR(16)  NOT NULL,
    PRIMARY KEY (workation_id),
    CONSTRAINT chk_workations_risk CHECK (risk IN ('HIGH', 'LOW', 'NO')),
    CONSTRAINT chk_workations_working_days CHECK (working_days >= 0),
    CONSTRAINT chk_workations_dates CHECK (end_date >= start_date)
);

CREATE INDEX idx_workations_employee ON workations (employee);
CREATE INDEX idx_workations_start_date ON workations (start_date);
