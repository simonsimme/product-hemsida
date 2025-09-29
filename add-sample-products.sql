
--psql -h localhost -p 8081 -U postgres -d product_db -f add-sample-products.sql

INSERT INTO products (id, title, description, price, quantity, image_url, created_at) VALUES
(gen_random_uuid(), 'The Vim Boat', 'Escape from JavaScript island with this legendary vessel. Warning: May cause uncontrollable urges to use hjkl for navigation. No LSP required.', 420.69, 1, '/images/boat.png', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Chat Dumb Certificate', 'Official certificate proving you survived a Primeagen roast session. Frame not included because frames are bloat. React developers need not apply.', 69.99, 999, '/images/chat_dumb.png', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'JavaScript Jail Pass', 'Get out of JavaScript jail FREE card. One-time use only. Cannot be used for TypeScript - that is a different kind of prison entirely.', 1337.00, 5, '/images/jail.png', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'King Shii Crown', 'Become the monarch of your terminal. Includes built-in Rust compiler and automatic npm uninstaller. Warning: May cause excessive use of the word "bro".', 199.99, 10, '/images/king_shii.png', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Thumb of Approval', 'The legendary thumb that has given approval to exactly zero web frameworks. Comes with lifetime warranty against JavaScript contamination.', 42.00, 100, '/images/thumb.png', CURRENT_TIMESTAMP);
