## Quick resume of the completed task

* Data persistence layer was added using the PostgresSQL database running in docker.


* Mapstruct was used to map entities for DTO purposes.


* A single update of an entity field is implemented using the @DynamicUpdate annotation ("It ensures that Hibernate uses only the modified columns in the SQL statement that it generates for the update of an entity").


* Some validations of entity fields, such as full age verification and others, are implemented using custom annotations. Additional constraints were also added, for example, for the uniqueness of the email and phone number.


* Exception handling was done using @ControllerAdvice, custom exceptions, and implementing a global exception handler.


* The tests consist of integration and regular unit tests.


* The rest of the implementation meets the basic requirements.


* All functionality was manually tested using Postman.