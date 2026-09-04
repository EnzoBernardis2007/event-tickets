CREATE TABLE permissions (
          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

          name VARCHAR(100) NOT NULL UNIQUE,

          description VARCHAR(255),

          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);