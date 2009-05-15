DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Fund;
DROP TABLE IF EXISTS Closing;

CREATE TABLE Category (
    id          VARCHAR(20) PRIMARY KEY,
    name        VARCHAR(30) NOT NULL
);

CREATE TABLE Fund (
    id          VARCHAR(20) PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    categoryId  VARCHAR(20) NOT NULL REFERENCES Category(id)
);

CREATE TABLE Closing (
    id          INT(10) PRIMARY KEY AUTO_INCREMENT,
    fundId      VARCHAR(20) NOT NULL REFERENCES Fund(id),
    date        DATE NOT NULL,
    price       DOUBLE
);

/*
INSERT INTO Category VALUES ('aex', 'AEX hoofdfondsen');
INSERT INTO Category VALUES ('midcap', 'MidCap fondsen');
INSERT INTO Category VALUES ('lokaal', 'Lokale fondsen');
INSERT INTO Category VALUES ('funds', 'Beleggingsfondsen');

INSERT INTO Fund VALUES ('ahold', 'Ahold', 'aex');
INSERT INTO Fund VALUES ('ict', 'ICT Automatisering', 'lokaal');

INSERT INTO Closing VALUES (NULL, 'ahold', '2009-01-30', 12.25);
INSERT INTO Closing VALUES (NULL, 'ahold', '2009-01-31', 13.50);
INSERT INTO Closing VALUES (NULL, 'ahold', '2009-02-01', 13.75);
*/
