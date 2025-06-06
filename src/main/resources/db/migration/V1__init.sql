CREATE TABLE IF NOT EXISTS places (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      description TEXT,
                                      latitude DOUBLE PRECISION NOT NULL,
                                      longitude DOUBLE PRECISION NOT NULL,
                                      created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      location GEOGRAPHY(POINT, 4326)
);


CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     latitude DOUBLE PRECISION NOT NULL,
                                     longitude DOUBLE PRECISION NOT NULL,
                                     location GEOGRAPHY(Point, 4326)
);
