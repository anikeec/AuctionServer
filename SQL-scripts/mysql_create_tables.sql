use auctiondb;

/* DROP TABLE USER; */

CREATE TABLE USER(
 user_id  INT NOT NULL,
 login VARCHAR(20),
 passw_hash VARCHAR(20),
 status VARCHAR(20),
 used BOOLEAN DEFAULT TRUE,
 observed INT, 
 PRIMARY KEY (user_id) 
);

/* DROP TABLE LOT; */

CREATE TABLE AUCTIONLOT(
 lot_id  INT NOT NULL,
 lot_name VARCHAR(30),
 start_price INT,
 start_date DATE,
 finish_date DATE,
 last_rate INT,
 last_rate_user INT,
 status VARCHAR(20), 
 observer INT,
 PRIMARY KEY (lot_id),
 FOREIGN KEY (last_rate_user) REFERENCES USER (user_id),
 FOREIGN KEY (observer) REFERENCES USER (user_id)
);

ALTER TABLE USER ADD FOREIGN KEY (observed) REFERENCES AUCTIONLOT (lot_id);














