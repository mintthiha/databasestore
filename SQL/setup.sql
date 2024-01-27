SET SERVEROUTPUT ON;

--CREATE TABLE

CREATE TABLE Cities (
    city_id NUMBER(6)   GENERATED ALWAYS AS IDENTITY CONSTRAINT city_id_pk PRIMARY KEY,
    name VARCHAR2(50)   NOT NULL UNIQUE
);
/

CREATE TABLE Provinces (
    province_id NUMBER(6)   GENERATED ALWAYS AS IDENTITY CONSTRAINT province_id_pk PRIMARY KEY,
    name VARCHAR2(50)       NOT NULL UNIQUE
);
/

CREATE TABLE Addresses (
    address_id  NUMBER(6) GENERATED ALWAYS AS IDENTITY CONSTRAINT address_id_pk PRIMARY KEY,
    street      VARCHAR2(100),
    district    VARCHAR2(100),
    city_id     NUMBER(6),
    province_id NUMBER(6),
    
    CONSTRAINT Addresses_city_id_fk
        FOREIGN KEY (city_id)
        REFERENCES Cities (city_id),
        
    CONSTRAINT Addresses_province_id_fk
        FOREIGN KEY (province_id)
        REFERENCES Provinces (province_id)
        ON DELETE CASCADE
);
/

CREATE TABLE Products (
    product_id  NUMBER(6)    GENERATED ALWAYS AS IDENTITY CONSTRAINT product_id_pk PRIMARY KEY,
    name        VARCHAR2(50) NOT NULL UNIQUE,
    category    VARCHAR2(30),
    price       NUMBER(9,2)
);
/

CREATE TABLE Stores (
    store_id    NUMBER(6)     GENERATED ALWAYS AS IDENTITY CONSTRAINT store_id_pk PRIMARY KEY,
    name        VARCHAR2(100) NOT NULL UNIQUE
);
/

CREATE TABLE Stores_Products (
    store_id    NUMBER (6)  REFERENCES Stores (store_id),
    product_id  NUMBER (6)  REFERENCES Products (product_id) 
);
/

CREATE TABLE Warehouses (
    warehouse_id    NUMBER(6)    GENERATED ALWAYS AS IDENTITY CONSTRAINT warehouse_id_pk PRIMARY KEY,
    name            VARCHAR2(50) NOT NULL UNIQUE,    
    address_id     NUMBER(6),
    
    CONSTRAINT Warehouses_address_id_fk
        FOREIGN KEY (address_id)
        REFERENCES Addresses (address_id)
        ON DELETE CASCADE
);
/

CREATE TABLE Inventory (
    product_id      NUMBER(6),
    warehouse_id    NUMBER(6),
    quantity        NUMBER(7) NOT NULL,
    
    CONSTRAINT Inventory_pk
        PRIMARY KEY (product_id, warehouse_id),
        
    CONSTRAINT Inventory_product_id_fk
        FOREIGN KEY (product_id)
        REFERENCES Products (product_id)
        ON DELETE CASCADE,
        
    CONSTRAINT Inventory_warehouse_id
        FOREIGN KEY (warehouse_id)
        REFERENCES Warehouses (warehouse_id)
        ON DELETE CASCADE
);
/

CREATE TABLE Customers (
    customer_id     NUMBER(6)    GENERATED ALWAYS AS IDENTITY CONSTRAINT customer_id_pk PRIMARY KEY,
    first_name      VARCHAR2(50) NOT NULL,
    last_name       VARCHAR2(50),
    email           VARCHAR2(50) UNIQUE,
    address_id     NUMBER(6),
    
    CONSTRAINT Customers_address_id_fk
        FOREIGN KEY (address_id)
        REFERENCES Addresses (address_id)
        ON DELETE CASCADE
);
/

CREATE TABLE Reviews (
    review_id       NUMBER(6)    GENERATED ALWAYS AS IDENTITY CONSTRAINT review_id_pk PRIMARY KEY,
    customer_id     NUMBER(6),
    product_id      NUMBER(6),
    grade           NUMBER (1),
    description     VARCHAR2(400),
    flagged         NUMBER(2),
    
    CONSTRAINT Reviews_customer_id_fk
        FOREIGN KEY (customer_id)
        REFERENCES Customers (customer_id)  ON DELETE CASCADE,
        
    CONSTRAINT Reviews_product_id_fk
        FOREIGN KEY (product_id)
        REFERENCES Products (product_id)
        ON DELETE CASCADE 
);
/

CREATE TABLE Orders (
    order_id    NUMBER(6) GENERATED ALWAYS AS IDENTITY CONSTRAINT order_id_pk PRIMARY KEY,
    customer_id NUMBER(6),
    product_id  NUMBER(6),
    quantity    NUMBER(5),
    order_date  DATE,
    
    
    CONSTRAINT Orders_customer_id_fk
        FOREIGN KEY (customer_id)
        REFERENCES Customers (customer_id)  ON DELETE CASCADE   
);

/

--Audit Log

CREATE TABLE AuditLog (
    auditLog_id      NUMBER(6) GENERATED ALWAYS AS IDENTITY CONSTRAINT auditLog_id PRIMARY KEY,
    action_performed VARCHAR2(20),
    action_time      DATE,
    table_modified   VARCHAR2(20),
    id               VARCHAR2(100)
);

/
--Creating object types

-- City
CREATE OR REPLACE TYPE cities_typ AS OBJECT(
    city_id     NUMBER(6),
    name        VARCHAR2(50)
);
/

-- Province
CREATE OR REPLACE TYPE provinces_typ AS OBJECT(
    province NUMBER(6),
    name VARCHAR2(50)
);
/

-- Address
CREATE OR REPLACE TYPE addresses_typ AS OBJECT(
    address_id NUMBER(6),
    street VARCHAR2(100),
    district VARCHAR(100),
    city_id NUMBER(6),
    province_id NUMBER(6)
);
/

-- Product
CREATE OR REPLACE TYPE products_typ AS OBJECT(
    product_id  NUMBER(6),
    name        VARCHAR2(50),
    category    VARCHAR2(30),
    price       NUMBER(9,2)
);
/

-- Store
CREATE OR REPLACE TYPE stores_typ AS OBJECT(
    store_id    NUMBER(6),
    name     VARCHAR2(50)
);
/

-- StoreProduct
CREATE OR REPLACE TYPE stores_products_typ AS OBJECT(
    store_id    NUMBER (6),
    product_id  NUMBER (6) 
);
/

-- Warehouse
CREATE OR REPLACE TYPE warehouses_typ AS OBJECT(
    warehouse_id   NUMBER(6),
    name            VARCHAR2(50),
    address_id      NUMBER(6)
);
/

-- Inventory
CREATE OR REPLACE TYPE inventory_typ AS OBJECT(
    product_id NUMBER(6),
    warehouse_id NUMBER(6),
    quantity NUMBER(7)
);
/

-- Customer
CREATE OR REPLACE TYPE customers_typ AS OBJECT(
    customer_id NUMBER(6),
    first_name  VARCHAR2(50),
    last_name   VARCHAR2(50),
    email       VARCHAR2(50),
    address_id NUMBER(6)
);
/

-- Review
CREATE OR REPLACE TYPE reviews_typ AS OBJECT(
    review_id       NUMBER(6),
    customer_id     NUMBER(6),
    product_id      NUMBER(6),
    grade           NUMBER(1),
    description     VARCHAR2(200),
    flagged         NUMBER(2)
);
/

-- Orders
CREATE OR REPLACE TYPE orders_typ AS OBJECT(
    order_id    NUMBER(6),
    customer_id NUMBER(6),
    product_id  NUMBER(6),
    quantity    NUMBER(5),
    order_date  DATE
);

/

--Find my primary key package 

CREATE OR REPLACE PACKAGE findPrimKey AS
    --City
    FUNCTION forCity(p_name VARCHAR2)
    RETURN NUMBER;   
    
    --Province
    FUNCTION forProvince(p_name VARCHAR2)
    RETURN NUMBER;
    
    --Address
    FUNCTION forAddress(p_address VARCHAR2)
    RETURN NUMBER;
    
    --Product
    FUNCTION forProduct(p_name VARCHAR2)
    RETURN NUMBER;
    
    --Store
    FUNCTION forStore(p_name VARCHAR2)
    RETURN NUMBER;
    
    --Warehouse
    FUNCTION forWarehouse(p_name VARCHAR2)
    RETURN NUMBER;
    
    --Customer
    FUNCTION forCustomer(p_email VARCHAR2)
    RETURN NUMBER;
END findPrimKey;
/

CREATE OR REPLACE PACKAGE BODY findPrimKey AS

    -- City
    FUNCTION forCity(p_name VARCHAR2)
    RETURN NUMBER IS
    primaryKey NUMBER;
    BEGIN
        SELECT c.city_id INTO primaryKey FROM Cities c 
        WHERE c.name = p_name
        FETCH FIRST ROW ONLY;
        RETURN(primaryKey);
    END;
    
    -- Province
    FUNCTION forProvince(p_name VARCHAR2)
    RETURN NUMBER IS
    primaryKey NUMBER;
    BEGIN    
        SELECT p.province_id INTO primaryKey FROM Provinces p
        WHERE UPPER(p.name) = UPPER(p_name);
        RETURN(primaryKey);
    END;
    
    -- Address
    FUNCTION forAddress(p_address VARCHAR2)
    RETURN NUMBER IS
    primaryKey NUMBER;
    BEGIN
        SELECT a.address_id INTO primaryKey FROM Addresses a
        WHERE (UPPER(a.street) = UPPER(p_address)) OR 
        (UPPER(a.district) = UPPER(p_address)) OR 
        (a.city_id = findPrimKey.forCity(p_address)) OR 
        (a.province_id = findPrimKey.forProvince(p_address))
        FETCH FIRST ROW ONLY;
        RETURN(primaryKey);
    END;
    
    -- Product
    FUNCTION forProduct(p_name VARCHAR2)
    RETURN NUMBER IS
    primaryKey NUMBER;
    BEGIN
        SELECT p.product_id INTO primaryKey FROM Products p
        WHERE UPPER(p.name) = UPPER(p_name)
        FETCH FIRST ROW ONLY;
        RETURN(primaryKey);
    END;

    -- Store
    FUNCTION forStore(p_name VARCHAR2)
    RETURN NUMBER IS
    primaryKey NUMBER;
    BEGIN
        SELECT s.store_id INTO primaryKey FROM Stores s
        WHERE UPPER(s.name) = UPPER(p_name)
        FETCH FIRST ROW ONLY;
        RETURN(primaryKey);
    END;
    
    FUNCTION forWarehouse(p_name VARCHAR2)
    RETURN NUMBER IS
    primaryKey NUMBER;
    BEGIN
        SELECT w.warehouse_id INTO primaryKey FROM Warehouses w
        WHERE UPPER(w.name) = UPPER(p_name)
        FETCH FIRST ROW ONLY;
        RETURN(primaryKey);
    END;

    -- Customer
    FUNCTION forCustomer(p_email VARCHAR2)
    RETURN NUMBER IS
    primaryKey NUMBER;
    BEGIN
        SELECT c.customer_id INTO primaryKey FROM Customers c
        WHERE UPPER(c.email) = UPPER(p_email)
        FETCH FIRST ROW ONLY;
        RETURN(primaryKey);
    END;
    
END findPrimKey;

-- Insert as fields package

/

CREATE OR REPLACE PACKAGE InsertFields AS
-- Address
   PROCEDURE intoAddresses (p_street VARCHAR2, p_district VARCHAR2, p_city_name VARCHAR2, p_province_name VARCHAR2);
   
-- StoresProducts
   PROCEDURE intoStoresProducts (p_store_name VARCHAR2, p_product_name VARCHAR2);
   
-- Warehouses
   PROCEDURE intoWarehouses (p_name VARCHAR2, p_address VARCHAR2);
   
-- Inventory
   PROCEDURE intoInventory (p_product_name VARCHAR2, p_warehouseName VARCHAR2, p_quantity NUMBER);
   
-- Customers
   PROCEDURE intoCustomers (p_firstName VARCHAR2, p_lastName VARCHAR2, p_Email VARCHAR2, p_address VARCHAR2);
   
-- Reviews
   PROCEDURE intoReviews (p_customerEmail VARCHAR2, p_product_name VARCHAR2, p_grade NUMBER, p_description VARCHAR2, p_flagged NUMBER);
   
-- Orders
   PROCEDURE intoOrders (p_customerEmail VARCHAR2, p_product_name VARCHAR2, p_quantity NUMBER, p_order_date VARCHAR2);
   
-- Products
   PROCEDURE intoProducts (p_name VARCHAR2, p_category VARCHAR2, p_price NUMBER);
END InsertFields;
/

CREATE OR REPLACE PACKAGE BODY InsertFields AS
    -- Addresses
   PROCEDURE intoAddresses (p_street VARCHAR2, p_district VARCHAR2, p_city_name VARCHAR2, p_province_name VARCHAR2) AS
   BEGIN
    INSERT INTO Addresses (street, district, city_id, province_id) 
    VALUES (p_street, p_district, findPrimKey.forCity(p_city_name), findPrimKey.forProvince(p_province_name));
   END;
   
   -- Stores Products
   PROCEDURE intoStoresProducts (p_store_name VARCHAR2, p_product_name VARCHAR2) AS
    BEGIN
    INSERT INTO Stores_Products (store_id, product_id) VALUES (findPrimKey.forStore (p_store_name), findPrimKey.forProduct (p_product_name));
   END;
   
   -- Warehouses
   PROCEDURE intoWarehouses (p_name VARCHAR2, p_address VARCHAR2) AS
   BEGIN
    INSERT INTO Warehouses (name, address_id)
    VALUES (p_name, findPrimKey.forAddress (p_address));
   END;
   
    -- Inventory
   PROCEDURE intoInventory (p_product_name VARCHAR2, p_warehouseName VARCHAR2, p_quantity NUMBER) AS
   BEGIN
    INSERT INTO Inventory ( product_id, warehouse_id, quantity)
    VALUES (findPrimKey.forProduct(p_product_name), findPrimKey.forWarehouse(p_warehouseName), p_quantity);
   END;
   
   -- Customers
   PROCEDURE intoCustomers (p_firstName VARCHAR2, p_lastName VARCHAR2, p_email VARCHAR2, p_address VARCHAR2) AS
   BEGIN
    INSERT INTO Customers (first_name, last_name, email, address_id) 
    VALUES (p_firstName, p_lastName, p_email, findPrimKey.forAddress(p_address));
   END;
   
   -- Reviews
   PROCEDURE intoReviews (p_customerEmail VARCHAR2, p_product_name VARCHAR2, p_grade NUMBER, p_description VARCHAR2, p_flagged NUMBER) AS
   BEGIN
    INSERT INTO Reviews (customer_id, product_id, grade, description, flagged) 
    VALUES (findPrimKey.forCustomer(p_customerEmail), findPrimKey.forProduct(p_product_name), p_grade, p_description, p_flagged);
   END;
   
   -- Orders
   PROCEDURE intoOrders (p_customerEmail VARCHAR2, p_product_name VARCHAR2, p_quantity NUMBER, p_order_date VARCHAR2) AS
   BEGIN
    INSERT INTO Orders (customer_id, product_id, quantity, order_date) 
    VALUES (findPrimKey.forCustomer(p_customerEmail), findPrimKey.forProduct(p_product_name), p_quantity, TO_DATE(p_order_date, 'MM/DD/YYYY'));
   END;
   
   --Products 
   PROCEDURE intoProducts (p_name VARCHAR2, p_category VARCHAR2, p_price NUMBER) AS
   BEGIN
    INSERT INTO Products (name, category, price) 
        VALUES (p_name, p_category, p_price);
    END;
END InsertFields;

/

--Insert as objects package

CREATE OR REPLACE PACKAGE insertObject AS
--  City
    PROCEDURE add_city(p_cities IN cities_typ);
--  Province
    PROCEDURE add_province(p_provinces IN provinces_typ);
--  Address
    PROCEDURE add_address(p_addresses IN addresses_typ);
--  Product
    PROCEDURE add_product(p_products IN products_typ);
--  Store
    PROCEDURE add_store(p_stores IN stores_typ);
--  StoreProduct
    PROCEDURE add_store_product(p_stores_products IN stores_products_typ);
--  Warehouse
    PROCEDURE add_warehouse(p_warehouses IN warehouses_typ);
--  Inventory
    PROCEDURE add_inventory(p_inventory IN inventory_typ);
--  Customer
    PROCEDURE add_customer(p_customers IN customers_typ);
--  Review
    PROCEDURE add_review(p_reviews IN reviews_typ);
--  Order
    PROCEDURE add_order(p_orders IN orders_typ);
END insertObject;
/
CREATE OR REPLACE PACKAGE BODY insertObject AS
--  City
    PROCEDURE add_city(p_cities IN cities_typ) AS
    BEGIN
        INSERT INTO Cities (name)
            VALUES (p_cities.name);
    END;
--  Province
    PROCEDURE add_province(p_provinces IN provinces_typ) AS
    BEGIN
        INSERT INTO Provinces (name) VALUES (p_provinces.name);
    END;
--  Address
    PROCEDURE add_address(p_addresses IN addresses_typ) AS
    BEGIN
        INSERT INTO Addresses (street, district, city_id, province_id)
            VALUES (p_addresses.street, p_addresses.district, p_addresses.city_id, p_addresses.province_id);
    END;
--  Product
    PROCEDURE add_product(p_products IN products_typ) AS
    BEGIN
        INSERT INTO Products (name, category, price)
            VALUES (p_products.name, p_products.category, p_products.price);
    END;
--  Store 
    PROCEDURE add_store(p_stores IN stores_typ) AS
    BEGIN
        INSERT INTO Stores (name)
            VALUES (p_stores.name);
    END;
--  StoreProduct
    PROCEDURE add_store_product(p_stores_products IN stores_products_typ) AS
    BEGIN
        INSERT INTO Stores_Products (store_id, product_id)
        VALUES (p_stores_products.store_id, p_stores_products.product_id);
    END;
--  Warehouse
    PROCEDURE add_warehouse(p_warehouses IN warehouses_typ) AS
    BEGIN
        INSERT INTO Warehouses (name, address_id)
            VALUES (p_warehouses.name, p_warehouses.address_id);
    END;
--  Inventory
    PROCEDURE add_inventory(p_inventory IN inventory_typ) AS
    BEGIN
        INSERT INTO Inventory 
            VALUES (p_inventory.quantity, p_inventory.product_id, p_inventory.warehouse_id);
    END;
--  Customer
    PROCEDURE add_customer(p_customers IN customers_typ) AS
    BEGIN
        INSERT INTO Customers (first_name, last_name, email, address_id)
            VALUES (p_customers.first_name, p_customers.last_name, p_customers.email, p_customers.address_id);
    END;
--  Review
    PROCEDURE add_review(p_reviews IN reviews_typ) AS
    BEGIN
        INSERT INTO Reviews  (description, flagged, grade, customer_id, product_id)
            VALUES (p_reviews.description, p_reviews.flagged, p_reviews.grade, p_reviews.customer_id, p_reviews.product_id);
    END;
--  Order
    PROCEDURE add_order(p_orders IN orders_typ) AS
    BEGIN
        INSERT INTO Orders (customer_id, product_id, quantity, order_date)
            VALUES (p_orders.customer_id, p_orders.product_id, p_orders.quantity, p_orders.order_date);
    END;
    
END insertObject;

/

-- Retrieving data from SQL, to Java, package.

CREATE OR REPLACE PACKAGE retrieveData AS 
    FUNCTION getAllProducts RETURN SYS_REFCURSOR;
    FUNCTION getAllInventory RETURN SYS_REFCURSOR;
    FUNCTION getAllWarehouses RETURN SYS_REFCURSOR;
    FUNCTION getAllReviews RETURN SYS_REFCURSOR;
    FUNCTION getAllCustomers RETURN SYS_REFCURSOR;
    FUNCTION getAllOrders RETURN SYS_REFCURSOR;
    FUNCTION getAllAuditLogs RETURN SYS_REFCURSOR;
    FUNCTION getAllStores RETURN SYS_REFCURSOR;
    FUNCTION getAllOrdersByStoreName(storeName VARCHAR2) RETURN SYS_REFCURSOR;
END retrieveData;
/
CREATE OR REPLACE PACKAGE BODY retrieveData AS
    FUNCTION getAllProducts RETURN SYS_REFCURSOR AS
        prodCursor SYS_REFCURSOR;
    BEGIN
        OPEN prodCursor FOR
            SELECT p.product_id, p.category, p.name, p.price FROM Products p;
            RETURN prodCursor;
    END;
    
    FUNCTION getAllInventory RETURN SYS_REFCURSOR AS
        inventCursor SYS_REFCURSOR;
    BEGIN
        OPEN inventCursor FOR
            SELECT P.name AS "productname", W.name AS "warehousename", I.quantity 
            FROM Inventory I
            INNER JOIN Products P On I.product_id = P.product_id
            INNER JOIN Warehouses W ON I.warehouse_id = W.warehouse_id;
            RETURN inventCursor;
    END;
    
    FUNCTION getAllWarehouses RETURN SYS_REFCURSOR AS
        warehouseCursor SYS_REFCURSOR;
    BEGIN
        OPEN warehouseCursor FOR 
            SELECT warehouse_id, name, address_id FROM Warehouses;
            RETURN warehouseCursor;
    END;
    
    FUNCTION getAllReviews RETURN SYS_REFCURSOR AS
        reviewCursor SYS_REFCURSOR;
    BEGIN
        OPEN reviewCursor FOR
            SELECT R.review_id, C.first_name || ' ' || C.last_name AS "fullname", C.email, P.name, grade, description, flagged 
            FROM Reviews R
            INNER JOIN Customers C ON R.customer_id = C.customer_id
            INNER JOIN Products P ON R.product_id = P.product_id;
            RETURN reviewCursor;
    END;
    
    FUNCTION getAllCustomers RETURN SYS_REFCURSOR AS
        customerCursor SYS_REFCURSOR;
    BEGIN
        OPEN customerCursor FOR
            SELECT C.customer_id, C.first_name, C.last_name, C.email, A.street || ', ' || NVL(A.district || ', ', '') || NVL(Cities.name || ', ', '')  || NVL(Provinces.name, '') AS "fulladdress"
            FROM Customers C
            LEFT JOIN Addresses A ON C.address_id = A.address_id
            LEFT JOIN Cities ON Cities.city_id = A.city_id
            LEFT JOIN Provinces ON Provinces.province_id = A.province_id;
            RETURN customerCursor;
    END;
    
    FUNCTION getAllOrders RETURN SYS_REFCURSOR AS
        orderCursor SYS_REFCURSOR;
    BEGIN
        OPEN orderCursor FOR
            SELECT O.order_id, C.first_name || ' ' || C.last_name AS "customername", P.name AS "productname", O.quantity , NVL(O.order_date, SYSDATE) AS "order_date"
            FROM Orders O
            INNER JOIN Customers C ON C.customer_id = O.customer_id
            INNER JOIN Products P ON P.product_id = O.product_id;
            RETURN orderCursor;
    END;
    
    FUNCTION getAllAuditLogs RETURN SYS_REFCURSOR AS
        auditLogCursor SYS_REFCURSOR;
    BEGIN
        OPEN auditLogCursor FOR
            SELECT auditLog_id, action_performed, action_time, table_modified, id FROM AuditLog;
            RETURN auditLogCursor;
    END;   
    
    FUNCTION getAllStores RETURN SYS_REFCURSOR AS
        storeCursor SYS_REFCURSOR;
    BEGIN 
        OPEN storeCursor FOR
            SELECT store_id, name FROM Stores;
            RETURN storeCursor;
    END;
    
    FUNCTION getAllOrdersByStoreName (storeName VARCHAR2) RETURN SYS_REFCURSOR AS
        filteredOrderCursor SYS_REFCURSOR;
    BEGIN
        OPEN filteredOrderCursor FOR
            SELECT o.order_id FROM Orders o
            INNER JOIN Products p 
            ON o.product_id = p.product_id
            INNER JOIN Stores_Products sp
            ON p.product_id = sp.product_id
            INNER JOIN Stores s
            ON sp.store_id = s.store_id
            WHERE LOWER(s.name) = storeName;
            RETURN filteredOrderCursor;
    END;
    
END retrieveData;

/

-- Updating data package.

CREATE OR REPLACE PACKAGE updateData AS
    PROCEDURE updateProduct(p_product_id NUMBER, p_name VARCHAR2, p_category VARCHAR2, p_price NUMBER);
    PROCEDURE updateInventory(p_old_product_name VARCHAR2, p_old_warehouse_name VARCHAR2, p_product_name VARCHAR2, p_warehouse_name VARCHAR2, p_quantity NUMBER);
    PROCEDURE updateCustomer (p_customer_id NUMBER, p_first_name VARCHAR2, p_last_name VARCHAR2, p_email VARCHAR2);
    PROCEDURE updateReview (p_review_id NUMBER, p_customer_email VARCHAR2, p_product_name VARCHAR2, p_grade NUMBER, p_description VARCHAR2, p_flagged NUMBER);
END updateData;
/
CREATE OR REPLACE PACKAGE BODY updateData AS
    PROCEDURE updateProduct(p_product_id NUMBER, p_name VARCHAR2, p_category VARCHAR2, p_price NUMBER) AS
    BEGIN
        UPDATE Products p
        SET p.name = p_name, p.category = p_category, p.price = p_price
        WHERE p.product_id = p_product_id;
    END;
    
     PROCEDURE updateInventory(p_old_product_name VARCHAR2, p_old_warehouse_name VARCHAR2, p_product_name VARCHAR2, p_warehouse_name VARCHAR2, p_quantity NUMBER) AS
    BEGIN
        UPDATE Inventory I
        SET I.product_id = findPrimKey.forProduct(p_product_name), I.warehouse_id = findPrimKey.forWarehouse(p_warehouse_name), I.quantity = p_quantity
        WHERE
            (I.product_id = findPrimKey.forProduct(p_old_product_name)) AND (I.warehouse_id = findPrimKey.forWarehouse(p_old_warehouse_name));
    END;
    
    PROCEDURE updateCustomer (p_customer_id NUMBER, p_first_name VARCHAR2, p_last_name VARCHAR2, p_email VARCHAR2) AS
    BEGIN
        UPDATE Customers C
        SET C.first_name = p_first_name, C.last_name = p_last_name, C.email = p_email
        WHERE C.customer_id = p_customer_id;
    END;
    
    PROCEDURE updateReview (p_review_id NUMBER, p_customer_email VARCHAR2, p_product_name VARCHAR2, p_grade NUMBER, p_description VARCHAR2, p_flagged NUMBER) AS
    BEGIN
        UPDATE Reviews R
        SET R.customer_id = findPrimKey.forCustomer(p_customer_email), R.product_id = findPrimKey.forProduct(p_product_name), R.grade = p_grade, R.description = p_description, R.flagged = p_flagged
        WHERE R.review_id = p_review_id;
    END;
END updateData;

/

-- Deleting data package.

CREATE OR REPLACE PACKAGE deleteData AS 
    PROCEDURE deleteProduct (p_product_id NUMBER); 
    PROCEDURE deleteWarehouse (p_warehouse_id NUMBER);
    PROCEDURE deleteReview (p_review_id NUMBER);
    PROCEDURE deleteOrder (p_order_id NUMBER);
END deleteData;
/

CREATE OR REPLACE PACKAGE BODY deleteData AS 
    PROCEDURE deleteProduct (p_product_id NUMBER) AS
    BEGIN
        UPDATE Inventory I SET I.quantity = 0 WHERE I.product_id = p_product_id;
    END;
    
    PROCEDURE deleteWarehouse (p_warehouse_id NUMBER) AS
    BEGIN 
        DELETE FROM Inventory I WHERE I.warehouse_id = p_warehouse_id;
        DELETE FROM Warehouses W WHERE W.warehouse_id = p_warehouse_id;
    END;
    
    PROCEDURE deleteReview (p_review_id NUMBER) AS
    BEGIN
        DELETE FROM Reviews R WHERE R.review_id = p_review_id;
    END;
    
    PROCEDURE deleteOrder (p_order_id NUMBER) AS
    BEGIN
        DELETE FROM Orders O WHERE O.order_id = p_order_id;
    END;
END deleteData;
/

-- Validating data package.

CREATE OR REPLACE PACKAGE validateData AS
    FUNCTION validateOrder (p_order_id NUMBER) RETURN NUMBER;
    FUNCTION validateCustomer (p_customer_id NUMBER) RETURN NUMBER;
    FUNCTION validateAverageRating (p_product_id NUMBER) RETURN NUMBER;
    FUNCTION getTotalAmount (p_product_id NUMBER) RETURN NUMBER;
END validateData;
/

CREATE OR REPLACE PACKAGE BODY validateData AS
    FUNCTION validateOrder (p_order_id NUMBER) RETURN NUMBER AS
        v_canOrder NUMBER;
        v_invent_quantity NUMBER(7);
        v_order_quantity NUMBER (7);
        v_product_id NUMBER(6);
    BEGIN
        SELECT product_id INTO v_product_id FROM Orders O WHERE O.order_id = p_order_id;
        SELECT SUM(I.quantity) INTO v_invent_quantity FROM Inventory I WHERE I.product_id = v_product_id;
        SELECT quantity INTO v_order_quantity FROM Orders O WHERE O.order_id = p_order_id;
        IF (v_order_quantity > v_invent_quantity) THEN v_canOrder := 0;
        ELSE v_canOrder := 1;
        END IF;
        RETURN v_canOrder;
    END;
    
    FUNCTION validateCustomer (p_customer_id NUMBER) RETURN NUMBER AS
        v_numOfFlaggs NUMBER(7);
    BEGIN
        SELECT SUM(R.flagged) INTO v_numOfFlaggs FROM Reviews R WHERE R.customer_id = p_customer_id;
        RETURN v_numOfFlaggs;
    END;
    
    FUNCTION validateAverageRating (p_product_id NUMBER) RETURN NUMBER AS
        v_average_rating NUMBER (7);
    BEGIN
        SELECT AVG (R.grade) INTO v_average_rating FROM Reviews R WHERE R.product_id = p_product_id;
        RETURN v_average_rating;
    END;
    
    FUNCTION getTotalAmount (p_product_id NUMBER) RETURN NUMBER AS
        v_total_amount NUMBER(7);
        BEGIN
            SELECT SUM(I.quantity) INTO v_total_amount FROM Inventory I WHERE I.product_id = p_product_id;
            RETURN v_total_amount;
        END;
END validateData;

/

--Inserting the raw data.

INSERT INTO Provinces (name) VALUES ('Quebec');
INSERT INTO Provinces (name) VALUES ('Alberta');

-- Cities (Name)
INSERT INTO Cities (name) VALUES ('Montreal');
INSERT INTO Cities (name) VALUES ('Quebec City');
INSERT INTO Cities (name) VALUES ('Toronto');
INSERT INTO Cities (name) VALUES ('Calgary');
INSERT INTO Cities (name) VALUES ('Brossard');
INSERT INTO Cities (name) VALUES ('Laval');
INSERT INTO Cities (name) VALUES ('Ottawa');

-- Addresses (Street, District, City, Province)
EXECUTE InsertFields.intoAddresses ('dawson college', null, 'Montreal', 'Quebec');
EXECUTE InsertFields.intoAddresses ('090 boul Saint Laurent', null, 'Montreal', 'Quebec');
EXECUTE InsertFields.intoAddresses (null, 'Brossard', 'Montreal', 'Quebec');
EXECUTE InsertFields.intoAddresses ('100 atwater street', null, 'Toronto', null);
EXECUTE InsertFields.intoAddresses ('100 Young street', null, 'Toronto', null);
EXECUTE InsertFields.intoAddresses ('100 boul saint laurent', null, 'Montreal', 'Quebec');
EXECUTE InsertFields.intoAddresses (null, null, 'Calgary', 'Alberta');
EXECUTE InsertFields.intoAddresses ('104 gill street', null, 'Toronto', null);
EXECUTE InsertFields.intoAddresses ('105 Young street', null, 'Toronto', null);
EXECUTE InsertFields.intoAddresses ('87 boul saint laurent', null, 'Montreal', 'Quebec');
EXECUTE InsertFields.intoAddresses ('76 boul decalthon', null, 'Laval', 'Quebec');
EXECUTE InsertFields.intoAddresses ('22222 happy street', null, 'Laval', 'Quebec');

EXECUTE InsertFields.intoAddresses ('100 rue William', 'saint laurent', null, 'Quebec');
EXECUTE InsertFields.intoAddresses ('304 Rue Fran?ois-Perrault', 'Villera Saint-Michel', 'Montreal', 'Quebec');
EXECUTE InsertFields.intoAddresses ('86700 Weston Rd', null, 'Toronto', null);
EXECUTE InsertFields.intoAddresses ('170  Sideroad', null, 'Quebec City', null);
EXECUTE InsertFields.intoAddresses ('1231 Trudea road', null, 'Ottawa', null);
EXECUTE InsertFields.intoAddresses ('16  Whitlock Rd', null, null, 'Alberta');

-- Products (Name, Genre, Price)
INSERT INTO Products (name, category, price) VALUES ('Laptop ASUS 1045', 'Electronics', 970.00);
INSERT INTO Products (name, category, price) VALUES ('Apple', 'Grocery', 5.00);
INSERT INTO Products (name, category, price) VALUES ('SIMS CD', 'Video Games', 16.66);
INSERT INTO Products (name, category, price) VALUES ('Orange', 'Grocery', 2.00);
INSERT INTO Products (name, category, price) VALUES ('Barbie Movie', 'DVD', 30.00);
INSERT INTO Products (name, category, price) VALUES ('L''Oreal Normal Hair', 'Health', 10.00);
INSERT INTO Products (name, category, price) VALUES ('BMW IX Lego', 'Toys', 40.00);
INSERT INTO Products (name, category, price) VALUES ('BMW i6', 'Cars', 50000);
INSERT INTO Products (name, category, price) VALUES ('Truck 500c', 'Vehicle', 856600.00);
INSERT INTO Products (name, category, price) VALUES ('Paper Towel', 'Beauty', 16.66);
INSERT INTO Products (name, category, price) VALUES ('Plum', 'Grocery', 1.43);
INSERT INTO Products (name, category, price) VALUES ('Lamborghni Lego', 'Toys', 40.00);
INSERT INTO Products (name, category, price) VALUES ('Chicken', 'Grocery', 9.50);
INSERT INTO Products (name, category, price) VALUES ('PS5', 'Electronics', 200.00);
INSERT INTO Products (name, category, price) VALUES ('Pasta', 'Grocery', 5.00);
INSERT INTO Products (name, category, price) VALUES ('Tomato', 'Grocery', -1);
INSERT INTO Products (name, category, price) VALUES ('Train X745', null, -1);

-- Stores (Name)
INSERT INTO Stores  (name) VALUES ('Dealer One');
INSERT INTO Stores  (name) VALUES ('Super Rue Champlain');
INSERT INTO Stores  (name) VALUES ('Movie Start');
INSERT INTO Stores  (name) VALUES ('Store Magic');
INSERT INTO Stores  (name) VALUES ('Toy R Us');
INSERT INTO Stores  (name) VALUES ('Star Store');
INSERT INTO Stores  (name) VALUES ('Marche Atwater');
INSERT INTO Stores  (name) VALUES ('Marcher Adonis');
INSERT INTO Stores  (name) VALUES ('Movie Store');
INSERT INTO Stores  (name) VALUES ('Dealer Montreal');
INSERT INTO Stores  (name) VALUES ('Dawson Store');

-- StoresProducts (store_name, product_name)
EXECUTE InsertFields.intoStoresProducts ('Marcher Adonis', 'Laptop ASUS 1045');
EXECUTE InsertFields.intoStoresProducts ('Marche Atwater', 'Apple');
EXECUTE InsertFields.intoStoresProducts ('Dawson Store', 'SIMS CD');
EXECUTE InsertFields.intoStoresProducts ('Store Magic', 'Orange');
EXECUTE InsertFields.intoStoresProducts ('Movie Store', 'Barbie Movie');
EXECUTE InsertFields.intoStoresProducts ('Super Rue Champlain', 'L''Oreal Normal Hair');
EXECUTE InsertFields.intoStoresProducts ('Toy R Us', 'BMW IX Lego');
EXECUTE InsertFields.intoStoresProducts ('Dealer One', 'BMW i6');
EXECUTE InsertFields.intoStoresProducts ('Dealer Montreal', 'Truck 500c');
EXECUTE InsertFields.intoStoresProducts ('Movie Store', 'Paper Towel');
EXECUTE InsertFields.intoStoresProducts ('Marche Atwater', 'Plum');
EXECUTE InsertFields.intoStoresProducts ('Super Rue Champlain', 'L''Oreal Normal Hair');
EXECUTE InsertFields.intoStoresProducts ('Toy R Us', 'Lamborghni Lego');
EXECUTE InsertFields.intoStoresProducts ('Marche Atwater', 'Plum');
EXECUTE InsertFields.intoStoresProducts ('Toy R Us', 'Lamborghni Lego');
EXECUTE InsertFields.intoStoresProducts ('Dealer One', 'BMW i6');
EXECUTE InsertFields.intoStoresProducts ('Movie Store','SIMS CD');
EXECUTE InsertFields.intoStoresProducts ('Toy R Us', 'Barbie Movie');
EXECUTE InsertFields.intoStoresProducts ('Marcher Adonis', 'Chicken');
EXECUTE InsertFields.intoStoresProducts ('Marche Atwater', 'Pasta');
EXECUTE InsertFields.intoStoresProducts ('Star Store', 'PS5');
EXECUTE InsertFields.intoStoresProducts ('Toy R Us', 'BMW IX Lego');
EXECUTE InsertFields.intoStoresProducts ('Store Magic', 'Pasta');

-- Warehouses (Name, Address)
EXECUTE InsertFields.intoWarehouses ('Warehouse A', '100 rue William');
EXECUTE InsertFields.intoWarehouses ('Warehouse B', '304 Rue Fran?ois-Perrault');
EXECUTE InsertFields.intoWarehouses ('Warehouse C', '86700 Weston Rd');
EXECUTE InsertFields.intoWarehouses ('Warehouse D', '170  Sideroad');
EXECUTE InsertFields.intoWarehouses ('Warehouse E', '1231 Trudea road');
EXECUTE InsertFields.intoWarehouses ('Warehouse F', '16  Whitlock Rd');

-- Inventory (product_name, warehouse_name, Quantity)
EXECUTE InsertFields.intoInventory ('Laptop ASUS 1045', 'Warehouse A', 1000);
EXECUTE InsertFields.intoInventory ('Apple', 'Warehouse B', 24980);
EXECUTE InsertFields.intoInventory ('SIMS CD', 'Warehouse C', 103);
EXECUTE InsertFields.intoInventory ('Orange', 'Warehouse D', 35405);
EXECUTE InsertFields.intoInventory ('Barbie Movie', 'Warehouse E', 40);
EXECUTE InsertFields.intoInventory ('L''Oreal Normal Hair', 'Warehouse F', 450);
EXECUTE InsertFields.intoInventory ('BMW IX Lego', 'Warehouse A', 10);
EXECUTE InsertFields.intoInventory ('BMW i6', 'Warehouse A', 6);
EXECUTE InsertFields.intoInventory ('Truck 500c', 'Warehouse E', 1000);
EXECUTE InsertFields.intoInventory ('Paper Towel', 'Warehouse F', 3532);
EXECUTE InsertFields.intoInventory ('Plum', 'Warehouse C', 43242);
EXECUTE InsertFields.intoInventory ('Paper Towel', 'Warehouse B', 39484);
EXECUTE InsertFields.intoInventory ('Plum', 'Warehouse D', 6579);
EXECUTE InsertFields.intoInventory ('Lamborghni Lego', 'Warehouse E', 98765);
EXECUTE InsertFields.intoInventory ('Chicken', 'Warehouse F', 43523);
EXECUTE InsertFields.intoInventory ('Pasta', 'Warehouse A', 2132);
EXECUTE InsertFields.intoInventory ('PS5', 'Warehouse D', 123);
EXECUTE InsertFields.intoInventory ('Tomato', 'Warehouse A', 352222);
EXECUTE InsertFields.intoInventory ('Train X745', 'Warehouse E', 4543);

-- Customers (first_name, last_name, Email, Address)
EXECUTE InsertFields.intoCustomers ('Mahsa', 'Sadeghi', 'msadeghi@dawsoncollege.qc.ca', 'dawson college');
EXECUTE InsertFields.intoCustomers ('Alex', 'Brown', 'alex@gmail.com', '090 boul Saint Laurent');
EXECUTE InsertFields.intoCustomers ('Martin', 'Alexandre', 'marting@yahoo.com', 'Brossard');
EXECUTE InsertFields.intoCustomers ('Daneil', 'Hanne', 'daneil@yahoo.com', '100 atwater street');
EXECUTE InsertFields.intoCustomers ('John', 'Boura', 'bdoura@gmail.com', '100 Young street');
EXECUTE InsertFields.intoCustomers ('Ari', 'Brown', 'b.a@gmail.com', null);
EXECUTE InsertFields.intoCustomers ('Amanda', 'Harry', 'am.harry@yahioo.com', '100 boul saint laurent');
EXECUTE InsertFields.intoCustomers ('Jack', 'Johnson', 'johnson.a@gmail.com', 'Calgary');
EXECUTE InsertFields.intoCustomers ('Mahsa', 'Sadeghi', 'ms@gmail.com', '104 gill street');
EXECUTE InsertFields.intoCustomers ('John', 'Belle', 'abcd@yahoo.com', '105 Young street');
EXECUTE InsertFields.intoCustomers ('Martin', 'Li', 'm.li@gmail.com', '87 boul saint laurent');
EXECUTE InsertFields.intoCustomers ('Olivia', 'Smith', 'smith@hotmail.com', '76 boul decalthon');
EXECUTE InsertFields.intoCustomers ('Noah', 'Garcia', 'g.noah@yahoo.com', '22222 happy street');

-- Reviews (customer_email, product_name, Grade, Description, Flagged)               
EXECUTE InsertFields.intoReviews('msadeghi@dawsoncollege.qc.ca', 'Laptop ASUS 1045', 4, 'it was affordable.', 0);
EXECUTE InsertFields.intoReviews('alex@gmail.com', 'Apple', 3, 'quality was not good', 0);
EXECUTE InsertFields.intoReviews('marting@yahoo.com', 'SIMS CD', 2, null, 1);
EXECUTE InsertFields.intoReviews('daneil@yahoo.com', 'Orange', 5, 'highly recommend', 0);
EXECUTE InsertFields.intoReviews('alex@gmail.com', 'Barbie Movie', 1, null, 0);
EXECUTE InsertFields.intoReviews('marting@yahoo.com', 'L''Oreal Normal Hair', 1, 'did not worth the price', 0);
EXECUTE InsertFields.intoReviews('msadeghi@dawsoncollege.qc.ca', 'BMW IX Lego', 1, 'missing some parts', 0);
EXECUTE InsertFields.intoReviews('bdoura@gmail.com', 'BMW i6', 5, 'trash', 1);
EXECUTE InsertFields.intoReviews('b.a@gmail.com','Truck 500c', 2, null, 0);
EXECUTE InsertFields.intoReviews('am.harry@yahioo.com', 'Paper Towel', 5, null, 0);
EXECUTE InsertFields.intoReviews('johnson.a@gmail.com', 'Plum', 4, null, 0);
EXECUTE InsertFields.intoReviews('marting@yahoo.com', 'L''Oreal Normal Hair', 3, null, 0);
EXECUTE InsertFields.intoReviews('msadeghi@dawsoncollege.qc.ca', 'Lamborghni Lego', 1, 'missing some parts', 0);
EXECUTE InsertFields.intoReviews('msadeghi@dawsoncollege.qc.ca', 'Plum', 4, null, 0);
EXECUTE InsertFields.intoReviews('ms@gmail.com', 'LamborgHni Lego', 1, 'great product', 0);
EXECUTE InsertFields.intoReviews('abcd@yahoo.com', 'BMW i6', 5, 'bad quality', 1);
EXECUTE InsertFields.intoReviews('alex@gmail.com', 'SIMS CD', 1, null, 0);
EXECUTE InsertFields.intoReviews('alex@gmail.com', 'Barbie Movie', 4, null, 0);
EXECUTE InsertFields.intoReviews('m.li@gmail.com', 'Chicken', 4, null, 0);
EXECUTE InsertFields.intoReviews('smith@hotmail.com', 'Pasta', 5, null, 0);
EXECUTE InsertFields.intoReviews('msadeghi@dawsoncollege.qc.ca', 'BMW IX Lego', 1, 'worse car i have droven!', 2);
EXECUTE InsertFields.intoReviews('smith@hotmail.com', 'Pasta', 4, null, 0);

-- Orders (ustomer_email, Product_name, Quantity, order_date) 
EXECUTE InsertFields.intoOrders ('msadeghi@dawsoncollege.qc.ca', 'Laptop ASUS 1045', 1, '4/21/2023');
EXECUTE InsertFields.intoOrders ('alex@gmail.com', 'Apple', 2, '10/23/2023');
EXECUTE InsertFields.intoOrders ('marting@yahoo.com', 'SIMS CD', 3, '10/1/2023');
EXECUTE InsertFields.intoOrders ('daneil@yahoo.com', 'Orange', 1, '10/23/2023');
EXECUTE InsertFields.intoOrders ('alex@gmail.com', 'Barbie Movie', 1, '10/23/2023');
EXECUTE InsertFields.intoOrders ('marting@yahoo.com','L''Oreal Normal Hair', 1, '10/10/2023');
EXECUTE InsertFields.intoOrders ('msadeghi@dawsoncollege.qc.ca', 'BMW IX Lego', 1, '10/11/2023');
EXECUTE InsertFields.intoOrders ('bdoura@gmail.com', 'BMW i6', 1, '10/10/2023');
EXECUTE InsertFields.intoOrders ('b.a@gmail.com', 'Truck 500c', 1, null);
EXECUTE InsertFields.intoOrders ('am.harry@yahioo.com', 'Paper Towel', 3, null);
EXECUTE InsertFields.intoOrders ('johnson.a@gmail.com', 'Plum', 6, '5/6/2020');
EXECUTE InsertFields.intoOrders ('marting@yahoo.com', 'L''Oreal Normal Hair', 3, '9/12/2019');
EXECUTE InsertFields.intoOrders ('msadeghi@dawsoncollege.qc.ca', 'Lamborghni Lego', 1, '10/11/2010');
EXECUTE InsertFields.intoOrders ('msadeghi@dawsoncollege.qc.ca', 'Plum', 7, '5/6/2022');
EXECUTE InsertFields.intoOrders ('ms@gmail.com', 'Lamborghni Lego', 2, '10/7/2023');
EXECUTE InsertFields.intoOrders ('abcd@yahoo.com','BMW i6', 1, '8/10/2023');
EXECUTE InsertFields.intoOrders ('alex@gmail.com', 'SIMS CD', 1, '10/23/2023');
EXECUTE InsertFields.intoOrders ('alex@gmail.com', 'Barbie Movie', 1, '10/2/2023');
EXECUTE InsertFields.intoOrders ('m.li@gmail.com', 'Chicken', 1, '4/3/2019');
EXECUTE InsertFields.intoOrders ('smith@hotmail.com', 'Pasta', 3, '12/29/2021');
EXECUTE InsertFields.intoOrders ('g.noah@yahoo.com', 'PS5', 1, '1/20/2020');
EXECUTE InsertFields.intoOrders ('msadeghi@dawsoncollege.qc.ca', 'BMW IX Lego', 1, '10/11/2022');
EXECUTE InsertFields.intoOrders ('smith@hotmail.com', 'Pasta', 3, '12/29/2021');
/

--CREATING TRIGGERS

CREATE OR REPLACE TRIGGER track_product_changes
    AFTER INSERT OR UPDATE OR DELETE ON Products
    FOR EACH ROW
    DECLARE
        action VARCHAR2(20);
    BEGIN
        IF INSERTING THEN
            action := 'Insert';
            INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Products', :NEW.product_id);
        ELSIF UPDATING THEN
            action := 'Update';
            INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Products', :OLD.product_id);
        ELSIF DELETING THEN
            action := 'Delete';
            INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Products', :OLD.product_id);
        END IF;
    END;
/
CREATE OR REPLACE TRIGGER track_review_changes
    AFTER INSERT OR UPDATE OR DELETE ON Reviews
    FOR EACH ROW
    DECLARE
        action VARCHAR2(20);
    BEGIN
        IF INSERTING THEN
            action := 'Insert';
            INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Reviews', :NEW.review_id);
        ELSIF UPDATING THEN
            action := 'Update';
            INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Reviews', :OLD.review_id);
        ELSIF DELETING THEN
            action := 'Delete';
            INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Reviews', :OLD.review_id);
        END IF;
    END;
/
CREATE OR REPLACE TRIGGER track_order_changes
    AFTER INSERT OR DELETE ON Orders
    FOR EACH ROW
    DECLARE
        action VARCHAR2(20);
    BEGIN
        IF INSERTING THEN
            action := 'Insert';
            INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Orders', :NEW.order_id);
        ELSIF DELETING THEN
            action := 'Delete';
            INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Orders', :OLD.order_id);
        END IF;
    END;
/
CREATE OR REPLACE TRIGGER track_warehouse_changes
    AFTER DELETE ON Warehouses
    FOR EACH ROW
    DECLARE
        action VARCHAR2(20);
    BEGIN
        IF DELETING THEN
            action := 'Delete';
        END IF;
        
        INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Warehouses', :OLD.warehouse_id);
    END;
/
CREATE OR REPLACE TRIGGER track_inventory_changes
    AFTER UPDATE ON Inventory
    FOR EACH ROW
    DECLARE
        action VARCHAR2(20);
    BEGIN
        IF UPDATING THEN
            action := 'Updating';
        END IF;
        
        INSERT INTO AuditLog (action_performed, action_time, table_modified, id)
            VALUES (action, SYSDATE, 'Inventory', :OLD.product_id || ' - ' || :OLD.warehouse_id);
    END;
/
COMMIT;  