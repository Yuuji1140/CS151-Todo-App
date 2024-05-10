# WaMa: The Warehouse Management System

# Team #6:
- Trevor Mathisen
  - Proposal: Brainstormed ideas for TODO app and this one. Wrote initial draft, added lots of todo's. Reviewed teammate contributions, provided feedback.
  - Presentation: Formed two diagrams, gave feedback for others. Helped with forming the slides, worked with team to brainstorm and put together the flow of the demo. Presented slides.
  - Code: Focused on backend and linking frontend to backend. Refactored code as needed during integrations. Helped with general debugging for overall project and acted as team lead/project manager/product owner.
- Viet Nguyen
  - Proposal: 
  - Presentation: 
  - Code: 
- Hike Yegiyan
    - Proposal: Brainstormed ideas, created diagrams, worked on proposal, provided feedback, and eventually decided on and mapped out our Warehouse Manager application.
    - Presentation: Collaborated on the flow of the demo, gave feedback for the slides, and demonstrated the program and its frontend usage in-class during presentation.
    - Code: Focused mostly on the frontend work and bit a of linkage with the backend. Created the styling, most of the scenes, and eye-candy for the login, signup, and welcome pages. Helped debug in various places.

# Problem/Issue to Resolve:
Managing inventory, orders, and shipments in a warehouse setting can be complex and time-consuming. Most current solutions are expensive, complex, or lack the necessary features for efficient warehouse management. We aim to create a user-friendly, scalable, and efficient Java GUI application that allows employees to manage inventory, process orders, and track shipments while enabling customers to view their inventory, create orders, and monitor shipments. The application's UI will be designed with simplicity and ease of use in mind, ensuring all functionalities are accessible within a few clicks. We will keep a consistent layout with intuitive navigation and clear feedback on user actions.

# Assumptions
We assume users have a basic understanding of operating a desktop application and are familiar with warehouse management concepts. We also assume they have access to a company provided workstation which is configured to run Java applications and access the backend. The app will have daily use by warehouse employees and customers. We assume that the application will be connected to a central backend for real-time data storage and retrieval, requiring users to have LAN or VPN access to our network. We also assume the user can read English as we likely won't have further localization.

# Diagrams
 - [Class Diagram](diagrams/backend_class_diagram.drawio.png)
 - [Sequence Diagram](diagrams/sequence_diagram.drawio.png) (User flow)
 - [Sequence Diagram](diagrams/sequence_update.png) (Update flow)
 - [State Diagram](diagrams/StateDiagram.png)
 - [Use Case Diagram](diagrams/UseCaseDiagram.png)

# Functionality:
- User Management: Employee and customer login, authentication, and role-based access control
- Inventory Management: Add, view, update, and delete products; track inventory levels; set reorder points
- Order Management: Create, view, update, and delete orders; process orders; generate invoices
- Shipment Management: Create, view, update, and delete shipments; track shipment status; generate shipping labels
- Reporting and Analytics: Generate reports on inventory levels, order history, and shipment status; provide insights for optimization
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

# Solution
- A JavaFX GUI application which defaults to connecting to localhost's Server (separate modules)
- A SQLite database to hold state
- Common classes between frontend and backend to pass data
- Real-time updates by using a background thread to query the server
- User management including registration

# Steps to run
1. `mvn clean install` in the root directory
2. `java -jar backend/target/backend-1.0-SNAPSHOT-jar-with-dependencies.jar` in the root directory
3. `java -jar frontend/target/frontend-1.0-SNAPSHOT-jar-with-dependencies.jar` in the root directory
4. OR: `cd frontend && mvn javafx:run` in the root directory
5. Credentials
   - Customer: `asdf`/`asdf`
   - Employee: `emp1`/`asdf`

# Snapshots
- ![Splash](diagrams/app%20pics/splash.png)
- ![Register](diagrams/app%20pics/register.png)
- ![Login](diagrams/app%20pics/login.png)
- ![Customer Catalog](diagrams/app%20pics/customer_catalog.png)
- ![Customer Orders](diagrams/app%20pics/customer_orders.png)
- ![Customer Cart](diagrams/app%20pics/customer_cart.png)
- ![Employee Catalog](diagrams/app%20pics/employee_catalog.png)
- ![Employee New Product](diagrams/app%20pics/employee_newproduct.png)
- ![Employee Shipping](diagrams/app%20pics/employee_shipping.png)
