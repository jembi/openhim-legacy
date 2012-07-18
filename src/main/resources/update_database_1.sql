use interoperability_layer;

ALTER TABLE transaction_log
MODIFY COLUMN body MEDIUMTEXT;

ALTER TABLE transaction_log
MODIFY COLUMN resp_body MEDIUMTEXT;
