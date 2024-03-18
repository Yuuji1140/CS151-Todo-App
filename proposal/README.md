# Project Title: Todo-App
## Team 6 
### Team Members: Hike Yegiyan, Viet Nguyen, Trevor Mathisen

# Problem/Issue to Resolve:
Everyday tasks are difficult to manage. Most current applications are feature-bloated, hidden behind a paywall or lack flexibility. Our aim is to create a complete, simple and easy to use GUI application to track todo items, their frequency and due date and allow grouping into various lists and sorting them in meaningful ways.

# Assumptions / Intended Usage 
#                    **TODO**

# High-Level Solution:
A Java desktop application with a user-friendly GUI integrated with a backend capable of long-term data storage, retrieval and manipulation. The frontend will be simple, intuitive and slightly customizable by allowing users to define views of their todo’s. We will utilize the Model-View-Controller model to allow for modularity and scaling. 

The Model component will be comprised of mostly backend components such as the todo items themselves, their associations and any associated disk management to persist the model. The View component will be the Java GUI and all associated visual elements to handle user interactions. The Controller component will be all methods within the GUI to interact with the Model, such as creating new todo items, setting saved filters to display the Model in a custom way, or grouping todo’ s into a list. 


# Functionality:
Persistent storage
Create, Read, Update, Delete individual items
For each item (todo, list), store a name, frequency, description and due date.
Group items into lists capable of filters/sorting.
Show status of items: active, overdue, complete, etc

# Operations: 
### Individuals
- Create a new Todo item: Allow users to add tasks, along with a description, its frequency (daily, weekly, monthly, annually, etc.) and due date
- Read Todo items: Display an organized list of todo items
- Update Todo items: Users are able to edit the name of the task, its description, frequency, and due date
- Delete Todo items: Offer an option for user to remove unwanted todo items from the list
- Filter and sort Todo items: Users can filter and sort items based on criteria such as status, frequency, and due date
- Create a new list: Allow separate lists to be created to group todo items based on different categories
- Read lists: Present all created lists and todo items within each list to users
- Update lists: Users are able to rename and change how a list is filter/sorted
- Delete lists: Remove entire lists along with all associated todo items
