# Project Title: Todo-App
## Team 6 
### Team Members: Hike Yegiyan, Viet Nguyen, Trevor Mathisen

# Problem/Issue to Resolve:
Everyday tasks are difficult to manage. Most current applications are feature-bloated, hidden behind a paywall or lack flexibility. Our aim is to create a complete, simple and easy to use GUI application to track todo items, their frequency and due date and allow grouping into various lists and sorting them in meaningful ways.

# Assumptions 
We have the assumption that users have a basic understanding of how to operate a desktop application but may not be technically inclined, suggesting our application's design to be relatively simple and straightforward. We also assume they have access to a personal computer with Java installed and use either Windows, macOS, or a Linux operating system. Given that our application is a Todo-app, it is designed for daily personal task management, which suggests frequent usage. Currently, we plan for user data to be stored locally on their machines, rather than on the cloud.

# Intended Usage
Usage of the application will consist of managing daily tasks which includes creating, updating, and tracking to-do items with specific due dates and/or frequencies, with the ability to group tasks together into lists based on categories. Users can sort tasks by due date, priority, or custom filters, as well as mark tasks as active, overdue, or complete. Each task will have some customizability, such as having a description, timer, or icon. The user may also have the option to view analytics and insights regarding their task management habits, such as completed tasks over time, most active categories, or time spent on tasks. 

# High-Level Solution:
A Java desktop application with a user-friendly GUI integrated with a backend capable of long-term data storage, retrieval and manipulation. The frontend will be simple, intuitive and slightly customizable by allowing users to define views of their todo’s. We will utilize the Model-View-Controller model to allow for modularity and scaling. 

The Model component will be comprised of mostly backend components such as the todo items themselves, their associations and any associated disk management to persist the model. The View component will be the Java GUI and all associated visual elements to handle user interactions. The Controller component will be all methods within the GUI to interact with the Modle, such as creating new todo items, setting saved filters to display the Model in a custom way, or grouping todo’s into a list. 


# Functionality:
Persistent storage
Create, Read, Update, Delete individual items
For each item (todo, list), store a name, frequency and due date.
Group items into lists capable of filters/sorting.
Show status of items: active, overdue, complete, etc

# Operations: 
#                   **TODO**
