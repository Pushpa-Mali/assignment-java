solution to - https://gist.github.com/ashwin-dailype/b2f26c7f4ca37304c921b4ef582b75c3#3-delete_u

API documentation -  http://localhost:8080/swagger-ui.html

Source code contains below endpoints

1. /create_user
Method: POST
Description: endpoint to create new user
![image](https://github.com/Pushpa-Mali/assignment-java/assets/94977416/c00db125-e21a-4204-bdaf-611b2d7197a4)



3. /get_users
Method: POST
Description: endpoint to get user details.
![image](https://github.com/Pushpa-Mali/assignment-java/assets/94977416/7e1c979b-f6b0-4eb2-888d-326509ddc4b1)

################################
![image](https://github.com/Pushpa-Mali/assignment-java/assets/94977416/afab2c5a-cc34-4d45-87b1-6446f1217293)



4. /delete_user
Method: POST
Description: endpoint to delete user.

![image](https://github.com/Pushpa-Mali/assignment-java/assets/94977416/404061db-f581-4057-b43d-bbdbe48e8214)


6. /update_user
Method: POST
Description: endpoint to update user details.
![image](https://github.com/Pushpa-Mali/assignment-java/assets/94977416/d4282366-4db1-4075-94b3-30bc8df42a81)




manager table
![image](https://github.com/Pushpa-Mali/assignment-java/assets/94977416/5bb5d3c8-8d88-47ae-afca-920696504250)


sql query to insert managers data to manager table

    INSERT INTO manager (manager_id, full_name, is_active) VALUES 
    (UNHEX('9a4f2a5af38b4f8eb3c352e28a7d1b8a'), 'John Doe', true),
    (UNHEX('bc07dbd61c94497fb1062d91dbce775b'), 'Alice Smith', false),
    (UNHEX('d4a2b585d1f74c279d533056b23b13e5'), 'Bob Johnson', true); 

