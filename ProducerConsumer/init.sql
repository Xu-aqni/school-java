CREATE TABLE IF NOT EXISTS product (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    creation_date TIMESTAMPTZ NOT NULL
);

INSERT INTO product (id, name, description, price, category, quantity, creation_date) 
VALUES
(1, 'Laptop Pro 15', 'Professional notebook with Intel Core i7 processor, 16 GB RAM, and 512 GB SSD.', 1299.90, 'Computers', 15, '2026-09-01 09:00:00+02'),
(2, 'Laptop Air 13', 'Compact and lightweight notebook with 8 GB RAM and 256 GB SSD.', 899.00, 'Computers', 22, '2026-09-02 10:15:00+02'),
(3, '27'' 4K Monitor', 'Professional 27-inch monitor with 4K UHD resolution.', 449.90, 'Computers', 12, '2026-09-03 11:30:00+02'),
(4, 'RGB Mechanical Keyboard', 'Mechanical keyboard with RGB backlighting and programmable switches.', 89.90, 'Accessories', 35, '2026-09-04 14:00:00+02'),
(5, 'Wireless Mouse Pro', 'Ergonomic wireless mouse with high-precision sensor.', 59.90, 'Accessories', 48, '2026-09-05 09:45:00+02'),
(6, 'Bluetooth Headphones', 'Wireless headphones with active noise cancellation.', 149.90, 'Audio', 18, '2026-09-06 15:20:00+02'),
(7, 'Full HD Webcam', 'Full HD 1080p webcam with built-in microphone.', 69.90, 'Accessories', 27, '2026-09-07 10:10:00+02'),
(8, 'USB Studio Microphone', 'USB condenser microphone for streaming, podcasting, and video calls.', 119.00, 'Audio', 14, '2026-09-08 13:40:00+02'),
(9, '1TB External SSD', 'USB 3.2 external solid-state drive with 1 TB capacity.', 109.90, 'Storage', 31, '2026-09-09 16:00:00+02'),
(10, '4TB External Hard Drive', 'USB external hard drive with 4 TB capacity.', 129.90, 'Storage', 19, '2026-09-10 09:30:00+02'),
(11, '11'' Tablet', '11-inch tablet with 128 GB of internal storage.', 399.90, 'Mobile', 16, '2026-09-11 11:00:00+02'),
(12, 'Smartphone X', 'Smartphone with OLED display, 256 GB storage, and advanced camera system.', 799.90, 'Mobile', 20, '2026-09-12 12:15:00+02'),
(13, 'Smartwatch Active', 'Smartwatch for fitness tracking and notifications.', 249.90, 'Wearables', 25, '2026-09-13 14:30:00+02'),
(14, 'WiFi 6 Router', 'WiFi 6 wireless router for home and small office networks.', 159.90, 'Networking', 17, '2026-09-14 10:00:00+02'),
(15, '8-Port Gigabit Switch', '8-port Gigabit Ethernet switch.', 49.90, 'Networking', 40, '2026-09-15 15:45:00+02'),
(16, 'Laser Printer', 'Monochrome laser printer for professional use.', 219.90, 'Office', 9, '2026-09-16 09:20:00+02'),
(17, 'Ergonomic Chair', 'Ergonomic office chair with adjustable lumbar support.', 329.90, 'Office', 11, '2026-09-17 13:10:00+02'),
(18, 'Adjustable Standing Desk', 'Electric height-adjustable standing desk.', 549.90, 'Office', 7, '2026-09-18 10:30:00+02'),
(19, '20000mAh Power Bank', 'High-capacity portable battery pack with fast charging.', 39.90, 'Accessories', 55, '2026-09-19 16:20:00+02'),
(20, 'USB-C Hub', 'Multiport USB-C hub with HDMI, USB 3.0, and SD card reader.', 74.90, 'Accessories', 29, '2026-09-20 11:45:00+02'),
(21, '3D Printer Pro', 'High-precision 3D printer with heated bed.', 499.90, 'Computers', 8, '2026-10-06 16:38:49.297221+02')
ON CONFLICT (id) DO NOTHING;