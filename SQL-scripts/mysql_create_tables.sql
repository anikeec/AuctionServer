use auctiondb;

/* DROP TABLE USER; */

CREATE TABLE USER(
 user_id  INT NOT NULL,
 login VARCHAR(20) DEFAULT '',
 passw_hash VARCHAR(20) DEFAULT '',
 used BOOLEAN DEFAULT TRUE,
 PRIMARY KEY (user_id) 
);

/* DROP TABLE LOT; */

CREATE TABLE AUCTIONLOT(
 lot_id  INT NOT NULL,
 lot_name VARCHAR(30) DEFAULT '',
 start_price INT DEFAULT 0,
 start_date DATE,
 finish_date DATE,
 last_rate INT DEFAULT 0,
 last_rate_user INT,
 status VARCHAR(20) DEFAULT '', 
 PRIMARY KEY (lot_id),
 FOREIGN KEY (last_rate_user) REFERENCES USER (user_id)
);

/* DROP TABLE OBSERVE; */

CREATE TABLE OBSERVE(
 id INT NOT NULL AUTO_INCREMENT,  
 user INT NOT NULL,
 lot INT NOT NULL,
 PRIMARY KEY (id),
 FOREIGN KEY (user) REFERENCES USER (user_id),
 FOREIGN KEY (lot) REFERENCES AUCTIONLOT (lot_id)
);














