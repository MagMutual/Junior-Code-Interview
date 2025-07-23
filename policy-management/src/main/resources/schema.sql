CREATE TABLE IF NOT EXISTS policies (
    id UUID PRIMARY KEY,
    business_name VARCHAR(255) NOT NULL,
    coverage_period DATE NOT NULL,
    coverage_amount DOUBLE PRECISION NOT NULL
); 