import sqlite3
import random
from faker import Faker
import uuid

# Usage:
# python3 -m venv venv
# source venv/bin/activate
# pip install faker
# python fakerscript.py

# Connect to the SQLite database (creates it if it doesn't exist)
conn = sqlite3.connect('database.db')
cursor = conn.cursor()

# Seed the database with fake data
fake = Faker()

# Users, UserPasswords, AuthTokens, Customers, Employees
for _ in range(30):
    user_id = str(uuid.uuid4())
    username = fake.user_name()
    email = fake.email()
    password = fake.password()
    auth_token = str(uuid.uuid4())
    expires_at = fake.future_datetime()

    cursor.execute("INSERT INTO Users (id, username, email) VALUES (?, ?, ?)", (user_id, username, email))
    cursor.execute("INSERT INTO UserPasswords (user_id, password) VALUES (?, ?)", (user_id, password))
    cursor.execute("INSERT INTO AuthTokens (user_id, auth_token, expires_at) VALUES (?, ?, ?)", (user_id, auth_token, expires_at))

    # Determine if the user should be a Customer or an Employee
    is_employee = random.choice([True, False])

    if is_employee:
        name = fake.name()
        position = fake.job()
        salary = round(random.uniform(30000, 100000), 2)
        cursor.execute("INSERT INTO Employees (user_id, name, email, position, salary) VALUES (?, ?, ?, ?, ?)", (user_id, name, email, position, salary))
    else:
        name = fake.name()
        address = fake.address()
        phone = fake.phone_number()
        cursor.execute("INSERT INTO Customers (user_id, name, email, address, phone) VALUES (?, ?, ?, ?, ?)", (user_id, name, email, address, phone))
conn.commit()

# Products
products = [
    ('Pencil', 'A simple pencil', 0.99, 100, 500),
    ('Notebook', 'A spiral notebook', 2.99, 50, 200),
    ('Eraser', 'A rubber eraser', 0.49, 200, 1000),
    ('Pen', 'A blue ballpoint pen', 1.49, 75, 300),
    ('Marker', 'A black permanent marker', 1.99, 60, 250),
    ('Ruler', 'A 12-inch ruler', 1.29, 80, 400),
    ('Scissors', 'A pair of scissors', 2.49, 60, 250),
    ('Glue Stick', 'A glue stick', 0.79, 120, 600),
    ('Highlighter', 'A yellow highlighter', 1.19, 90, 350),
    ('Stapler', 'A desktop stapler', 3.99, 40, 150),
    ('Staples', 'A box of staples', 0.99, 100, 500),
    ('Paper Clips', 'A box of paper clips', 0.69, 150, 750),
    ('Binder', 'A 3-ring binder', 4.99, 30, 100),
    ('Sticky Notes', 'A pad of sticky notes', 1.79, 70, 300),
    ('File Folders', 'A pack of file folders', 2.99, 50, 200),
    ('Envelope', 'A pack of envelopes', 1.49, 80, 400),
    ('Index Cards', 'A pack of index cards', 0.99, 100, 500),
    ('Sharpener', 'A pencil sharpener', 0.79, 120, 600),
    ('Calculator', 'A basic calculator', 4.99, 40, 150),
    ('Desk Organizer', 'A desk organizer', 6.99, 20, 80),
    ('Whiteboard', 'A small whiteboard', 9.99, 15, 50),
    ('Markers', 'A pack of whiteboard markers', 3.49, 30, 120),
    ('Easel', 'A portable easel', 19.99, 10, 30),
    ('Projector', 'A multimedia projector', 99.99, 5, 10),
    ('Laptop', 'A budget laptop', 399.99, 3, 5),
    ('Tablet', 'A tablet computer', 249.99, 4, 8),
    ('Printer', 'A laser printer', 129.99, 6, 12),
    ('Scanner', 'A document scanner', 79.99, 8, 16),
    ('Headphones', 'A pair of headphones', 24.99, 20, 60),
    ('Speakers', 'A set of computer speakers', 19.99, 15, 45)
]

for product in products:
    product_id = str(uuid.uuid4())
    try:
        name, description, price, reorder_point, current_stock = product
        cursor.execute("INSERT INTO Products (id, name, description, price, reorder_point, current_stock) VALUES (?, ?, ?, ?, ?, ?)",
                   (product_id, name, description, price, reorder_point, current_stock))
    except:
        continue
conn.commit()

# Orders, OrderItems, Shipments
customer_ids = [row[0] for row in cursor.execute("SELECT user_id FROM Customers")]
for customer_id in customer_ids:
    num_orders = random.randint(2, 6)  # Each customer has 2 to 6 orders
    for _ in range(num_orders):
        order_id = str(uuid.uuid4())
        employee_id = fake.random_element([row[0] for row in cursor.execute("SELECT user_id FROM Employees")])
        order_date = fake.past_datetime()
        status = fake.random_element(['Pending', 'Processing', 'Shipped', 'Delivered'])
        total = 0

        cursor.execute("INSERT INTO Orders (id, customer_id, employee_id, order_date, status, total) VALUES (?, ?, ?, ?, ?, ?)",
                       (order_id, customer_id, employee_id, order_date, status, total))

        # OrderItems
        num_items = random.randint(1, 10)
        for _ in range(num_items):
            item_id = str(uuid.uuid4())
            product_id = fake.random_element([row[0] for row in cursor.execute("SELECT id FROM Products")])
            quantity = random.randint(1, 10)
            price = cursor.execute("SELECT price FROM Products WHERE id = ?", (product_id,)).fetchone()[0]
            total += quantity * price

            cursor.execute("INSERT INTO OrderItems (id, order_id, product_id, quantity, price) VALUES (?, ?, ?, ?, ?)",
                           (item_id, order_id, product_id, quantity, price))

        # Update the total in the Orders table
        cursor.execute("UPDATE Orders SET total = ? WHERE id = ?", (total, order_id))

        # Shipments
        if status in ['Shipped', 'Delivered']:
            shipment_id = str(uuid.uuid4())
            shipment_date = fake.date_between_dates(date_start=order_date)
            tracking_number = str(uuid.uuid4())

            cursor.execute("INSERT INTO Shipments (id, order_id, shipment_date, status, tracking_number) VALUES (?, ?, ?, ?, ?)",
                           (shipment_id, order_id, shipment_date, status, tracking_number))
conn.commit()

# Commit the changes and close the connection
conn.commit()
conn.close()
