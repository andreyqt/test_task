CREATE TABLE test_users (
                            id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                            username varchar(64) UNIQUE NOT NULL,
                            email varchar(64) UNIQUE NOT NULL,
                            phone varchar(32) UNIQUE,
                            about_me varchar(4096),
                            active boolean DEFAULT true NOT NULL,
                            city varchar(64),
                            experience int,
                            created_at timestamptz DEFAULT current_timestamp,
                            updated_at timestamptz DEFAULT current_timestamp
);