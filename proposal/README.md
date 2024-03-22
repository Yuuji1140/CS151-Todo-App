# Project Title: Warehouse Management App (WaMa)
## Team 6
### Team Members: Hike Yegiyan, Viet Nguyen, Trevor Mathisen

# Problem/Issue to Resolve:
Managing a Warehouse can be complex and time-consuming. We aim to create a user-friendly Java GUI application that allows employees to manage inventory, process orders, and track shipments while enabling customers to view their inventory, create orders, and monitor shipments. 

# Assumptions
We assume users have a basic understanding of how to operate a desktop application and are familiar with warehouse management concepts. We also assume they have access to a company provided workstation which is configured to run Java applications and access the backend. The app will have daily use by warehouse employees and customers. We assume that the application will be connected to a central backend for real-time data storage and retrieval, requiring users to have LAN or VPN access to our network. We also assume the user can read English as we most likely won't have any further localization.

# Intended Usage
Employees will use the application to manage inventory, process customer requests, process orders, and track shipments. They can login to the system to view and update inventory levels, create and fulfill orders, and manage shipments. Customers can login to view their inventory levels, create new orders to increase inventory, and track the status of their shipments to their end-users/consumers. The application will provide real-time data and insights to help users make informed decisions and optimize warehouse operations.

# High-Level Solution:
A Java desktop application with a user-friendly GUI integrated with a backend capable of real-time data creation, retrieval, update and delation. The front-end will be intuitive, straight-forward and designed to aide warehouse management processes. We will utilize the Model-View-Controller (MVC) pattern to ensure modularity and scalability.

The Model component will reside in the backend and include entities such as products, orders, shipments, employees, and customers. The Model will interact with a database or filesystem for persistent data storage. The View component will be the Java GUI and all associated visual elements to handle user interactions. The Controller component will handle the communication between the View and the Model, processing user inputs and updating the View based on changes in the Model. This will allow further scaling in future iterations by allowing the Model to transition to various RDBM (Relational Database Management) systems, a distributed control approach (see: microservices) and further scaling of connected users. The application will follow a client-server architecture, with multiple client instances (Java GUI) connecting to a single server instance (backend). This architecture allows for centralized data management and enables multiple users to access the system concurrently.

# Functionality:
- User Management: Employee and customer login, authentication, and role-based access control
- Authorization: Manage access to various actions based on access level
- Inventory Management: Add, view, update, and delete products; track inventory levels; set reorder points
- Order Management: Create, view, update, and delete orders; process orders; generate invoices
- Shipment Management: Create, view, update, and delete shipments; track shipment status; generate shipping labels
- Reporting and Analytics: Generate reports on inventory levels, order history, and shipment status
- Real-time Updates: Reflect changes made by one user in real-time for all connected users

# Operations:
- Employee Login: Employees can securely login to the system using their credentials
- Customer Login: Customers can securely login to the system using their credentials
- Manage Products: Employees can add, view, update, and delete products in the inventory
- Manage Inventory: Employees can update inventory levels, set reorder points, and view low-stock alerts
- Create Order: Customers can create new orders by selecting products and quantities
- Process Order: Employees can view, edit, and process customer orders
- Create Shipment: Employees can create shipments for processed orders
- Track Shipment: Customers and employees can track the status of shipments
- Generate Reports: Employees can generate reports on inventory, orders, and shipments
- Real-time Updates: Changes made by one user are immediately visible to all connected users
