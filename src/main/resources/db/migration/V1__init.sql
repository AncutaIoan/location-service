CREATE TABLE place (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       latitude DOUBLE PRECISION NOT NULL,
                       longitude DOUBLE PRECISION NOT NULL,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       location GEOGRAPHY(POINT, 4326)  -- Store latitude and longitude as a PostGIS point
);