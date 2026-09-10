INSERT INTO roles (name, description)
VALUES
    ('SUPERUSER', 'Full and unrestricted access to the system'),
    ('CUSTOMER', 'Client with standard usage permissions')
    ON CONFLICT (name) DO NOTHING;