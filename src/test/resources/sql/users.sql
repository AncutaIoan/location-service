TRUNCATE TABLE users;

INSERT INTO users (username, email, password, latitude, longitude, location)
VALUES (
           'nearby_user',
           'nearby@example.com',
           'securepassword',
           40.7128,
           -74.0060,
           ST_SetSRID(ST_MakePoint(-74.0060, 40.7128), 4326)
       );

-- Insert a user located in Los Angeles (outside 100 km)
INSERT INTO users (username, email, password, latitude, longitude, location)
VALUES (
           'far_user',
           'far@example.com',
           'securepassword',
           34.0522,
           -118.2437,
           ST_SetSRID(ST_MakePoint(-118.2437, 34.0522), 4326)
       );
