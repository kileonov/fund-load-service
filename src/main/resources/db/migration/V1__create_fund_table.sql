CREATE TABLE fund (
    id VARCHAR(255),
    customer_id VARCHAR(255),
    load_amount VARCHAR(255),
    "time" TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,

    PRIMARY KEY (id, customer_id)
);
