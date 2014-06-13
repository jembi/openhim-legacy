use interoperability_layer;

alter table transaction_log add index `uuid` (`uuid`);
