CREATE TABLE fund (
    id VARCHAR(255) PRIMARY KEY,
    customer_id VARCHAR(255),
    load_amount VARCHAR(255),
    "time" TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);
