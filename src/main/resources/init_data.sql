INSERT INTO EabaxInLog (process_time, instrm_set_id, mm_activity_id, eabax_apply_id)
VALUES  (getDate(), 0, 0, 0);

INSERT INTO EabaxOutLog (process_time, department_id, disposible_item_id, supplier_id, activity_id)
VALUES (getDate(), 0, 0, 0, 0);
