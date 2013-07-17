use interoperability_layer;

ALTER TABLE transaction_log
MODIFY COLUMN error_stacktrace MEDIUMTEXT;
