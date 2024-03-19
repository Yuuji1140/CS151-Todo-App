# Project Title: Todo-App
## Team 6 
### Team Members: Hike Yegiyan, Viet Nguyen, Trevor Mathisen

# Problem/Issue to Resolve:
Everyday tasks are difficult to manage. Most current applications are feature-bloated, hidden behind a paywall, or lack flexibility. We aim to create a complete, simple, and easy-to-use GUI application to track todo items, their frequency, and due dates and allow grouping into various lists and sorting them in meaningful ways.

# Assumptions 
We have the assumption that users have a basic understanding of how to operate a desktop application but may not be technically inclined, suggesting our application's design to be relatively simple and straightforward. We also assume they have access to a personal computer with Java installed and use either Windows, macOS, or a Linux operating system. Given that our application is a Todo app, it is designed for daily personal task management, which suggests frequent usage. Currently, we plan for user data to be stored locally on their machines, rather than on the cloud and thus don't assume the user will require internet access after initially downloading the application. We also assume the user can read English as we most likely won't have any further localization.

# Intended Usage
Usage of the application will consist of managing daily tasks which include creating, updating, and tracking to-do items with specific due dates and/or frequencies, with the ability to group tasks into lists based on categories. Users can sort tasks by due date, priority, or custom filters, as well as mark tasks as active, overdue, or complete. Each task will have some customizability, such as having a description, timer, or icon. The user may also have the option to view analytics and insights regarding their task management habits, such as completed tasks over time, most active categories, or time spent on tasks. 

# High-Level Solution:
A Java desktop application with a user-friendly GUI integrated with a backend capable of long-term data storage, retrieval, and manipulation. The front-end will be simple, intuitive, and slightly customizable by allowing users to define views of their todo’s. We will utilize the Model-View-Controller model to allow for modularity and scaling. 

The Model component will be comprised of mostly backend components such as the todo items themselves, their associations, and any associated disk management to persist the model. The View component will be the Java GUI and all associated visual elements to handle user interactions. The Controller component will be all methods within the GUI to interact with the Model, such as creating new todo items, setting saved filters to display the Model in a custom way, or grouping todo’s into a list. 


# Functionality:
Persistent storage
Create, Read, Update, Delete individual items
For each item (todo, list), store a name, frequency, description, and due date.
Group items into lists capable of filters/sorting.
Show the status of items: active, overdue, complete, etc

# Operations: 
### Individuals
- Create a new Todo item: Allow users to add tasks, along with a description, its frequency (daily, weekly, monthly, annually, etc.), and due date
- Read Todo items: Display an organized list of todo items
- Update Todo items: Users can edit the name of the task, its description, frequency, and due date
- Delete Todo items: Offer an option for the user to remove unwanted todo items from the list
- Filter and sort Todo items: Users can filter and sort items based on criteria such as status, frequency, and due date
- Create a new list: Allow separate lists to be created to group todo items based on different categories
- Read lists: Present all created lists and todo items within each list to users
- Update lists: Users can rename and change how a list is filtered/sorted
- Delete lists: Remove entire lists along with all associated todo items
