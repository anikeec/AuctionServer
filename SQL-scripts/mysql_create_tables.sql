use auctiondb;

/* DROP TABLE USTATUS; */

CREATE TABLE USTATUS(
 status_id  INT NOT NULL,
 name VARCHAR(20),
 PRIMARY KEY (status_id)
);

/* DROP TABLE USER; */

CREATE TABLE USER(
 user_id  INT NOT NULL,
 login VARCHAR(20),
 passw_hash VARCHAR(20),
 status_id INT,
 used BOOLEAN DEFAULT TRUE,
 PRIMARY KEY (user_id),
 FOREIGN KEY (status_id) REFERENCES USTATUS (status_id)
);

/* DROP TABLE LOTSTATUS; */

CREATE TABLE AUCTIONLOTSTATUS(
 status_id  INT NOT NULL,
 name VARCHAR(20),
 PRIMARY KEY (status_id)
);

/* DROP TABLE LOT; */

CREATE TABLE AUCTIONLOT(
 lot_id  INT NOT NULL,
 lot_name VARCHAR(30),
 start_price INT,
 start_date DATE,
 finish_date DATE,
 last_rate INT,
 last_rate_user_id INT,
 status_id INT,
 PRIMARY KEY (lot_id),
 FOREIGN KEY (status_id) REFERENCES AUCTIONLOTSTATUS (status_id),
 FOREIGN KEY (last_rate_user_id) REFERENCES USER (user_id)
);

/* DROP TABLE OBSERVES; */

CREATE TABLE OBSERVES(
 id  INT NOT NULL,
 user_id INT,
 lot_id INT,
 PRIMARY KEY (id),
 FOREIGN KEY (user_id) REFERENCES USER (user_id),
 FOREIGN KEY (lot_id) REFERENCES AUCTIONLOT (lot_id)
);













