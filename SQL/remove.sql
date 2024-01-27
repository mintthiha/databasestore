--DROP TRIGGERS 

DROP TRIGGER track_inventory_changes;
DROP TRIGGER track_order_changes;
DROP TRIGGER track_product_changes;
DROP TRIGGER track_review_changes;
DROP TRIGGER track_warehouse_changes;

--DROP TABLE

DROP TABLE Inventory;
DROP TABLE Orders;
DROP TABLE Stores_Products;
DROP TABLE Stores;
DROP TABLE Reviews;
DROP TABLE Products;
DROP TABLE Customers;
DROP TABLE Warehouses;
DROP TABLE Addresses;
DROP TABLE Cities;
DROP TABLE Provinces;
DROP TABLE AuditLog;

--DROP OBJECT TYPES 

DROP TYPE cities_typ;
DROP TYPE provinces_typ;
DROP TYPE addresses_typ;
DROP TYPE products_typ;
DROP TYPE stores_typ;
DROP TYPE stores_products_typ;
DROP TYPE warehouses_typ;
DROP TYPE inventory_typ;
DROP TYPE customers_typ;
DROP TYPE reviews_typ;
DROP TYPE orders_typ;

--DROP PACKAGES

DROP PACKAGE findPrimKey;
DROP PACKAGE insertFields;
DROP PACKAGE insertObject;
DROP PACKAGE retrieveData;
DROP PACKAGE updateData;
DROP PACKAGE deleteData;
DROP PACKAGE validateData;

COMMIT;