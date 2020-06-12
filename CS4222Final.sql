#Building Database
CREATE TABLE BORROWER
(ID INT NOT NULL, 
Fname VARCHAR(20) NOT NULL,
Lname VARCHAR(20) NOT NULL,
Gender CHAR,
Borrower_Type VARCHAR(30),
Dept VARCHAR(30),
Email VARCHAR(35),
Birthday DATE,
PRIMARY KEY (ID)
);


CREATE TABLE BOOK
(Catalog_Number INT NOT NULL, 
Book_Catalog VARCHAR(30),
Title VARCHAR(30),
ISBN VARCHAR(17),
Publish_Press VARCHAR(15),
Publish_Date DATE,
Author_Fname VARCHAR(30),
Author_Lname VARCHAR(30),
Price DOUBLE(10,2),
Num_Words VARCHAR(30),
Intro VARCHAR(90),
PRIMARY KEY (Catalog_Number)
);



Create TABLE Authors
(Catalog_Number INT NOT NULL,
Author_Fname VARCHAR(30),
Author_Lname VARCHAR(30),
PRIMARY KEY (Catalog_Number),
Foreign KEY (Catalog_Number) REFERENCES BOOK(Catalog_Number)
);


CREATE TABLE LOAN_TYPE
(Loan_Catalog VARCHAR(30) NOT NULL,
LType VARCHAR(30),
Loan_Period INT,
Loaned_Number INT,
Extension_Limit INT,
Late_Fee DOUBLE (10,2),
PRIMARY KEY (Loan_Catalog, Ltype)
);


ALTER TABLE BOOK
ADD FOREIGN KEY (Book_Catalog) REFERENCES LOAN_TYPE(Loan_Catalog);


CREATE TABLE LOANS
(LoanID INT,
Borrower_ID INT,
Copy_ID INT,
Branch_ID INT,
Extension INT NOT NULL,
Checked_Out DATE,
Due_Date DATE,
Date_Returned DATE,
Fee DOUBLE (10,2),
PRIMARY KEY(LoanID),
FOREIGN KEY(Borrower_ID) REFERENCES BORROWER(ID)
);




CREATE TABLE BOOK_COPIES
(Copy_ID INT NOT NULL,
Catalog_Number INT NOT NULL,
Branch_ID INT NOT NULL,
NumOfCopies INT NOT NULL,
PRIMARY KEY(Copy_ID),
FOREIGN KEY(Catalog_Number) REFERENCES BOOK(Catalog_Number)
);


ALTER TABLE LOANS
ADD FOREIGN KEY(Copy_ID) REFERENCES BOOK_COPIES(Copy_ID);


CREATE TABLE LIBRARY_BRANCH
(Branch_ID INT NOT NULL,
Branch_Name VARCHAR(30),
PRIMARY KEY(Branch_ID)
);
ALTER TABLE BOOK_COPIES
ADD FOREIGN KEY (Branch_ID) REFERENCES LIBRARY_BRANCH(Branch_ID);


ALTER TABLE LOANS
ADD FOREIGN KEY (Branch_ID ) REFERENCES LIBRARY_BRANCH(Branch_ID) ;


CREATE TABLE ONLINE_SYSTEM
(Username DATE,
Password VARCHAR(30) NOT NULL,
PRIMARY KEY(Username)
);


#Inputing Data

INSERT INTO BORROWER (ID, Fname, Lname, Gender, Borrower_Type, Dept, Email, Birthday)
VALUES ('1','Scott','Johnson','M','Graduate','Computer Science','scott1@gmail.com','1991-11-01'),
	('2','John','Doe','M','UnderGraduate','Electrical Engineering','john1@gmail.com','1999-05-21'),
    ('3','Bill','Rodriguez','M','UnderGraduate','Computer Science','bill1@gmail.com','1997-03-19'),
    ('4','Mary','Smith','F','Faculty','Economics','mary1@gamil.com','1984-10-31'),
    ('5','Carlos','Ramirez','M','Vocational','English','carlos1@yahoo.com','1992-08-05'),
    ('6','Bob','Hernandez','M','Graduate','Biology','bob1@gamil.com','1993-02-21'),
    ('7','Jennifer','Escobar','F','Faculty','Computer Science','jennifer1@gmail.com','1992-09-20'),
    ('8','Rick','Smalls','M','Graduate','Computer Science','rick1@gmail.com','1987-08-11'),
	('9','Patrick','Lee','M','Graduate','Computer Science','pat1@gmail.com','1989-03-23'),
    ('10','Ashley','Espinoza','F','Faculty','Electrical Engineering','ash1@gmail.com','1977-11-20'),
    ('11','Randy','Hill','M','Faculty','Electrical Engineering','rick1@gmail.com','1987-06-10'),
    ('12','Robert','Kang','M','Faculty','Computer Science','rob1@gmail.com','1981-05-01'),
	('13','Ryan','Fernandez','M','Faculty','Computer Science','pat1@gmail.com','1979-08-13'),
	('14','Megan','Morland','F','UnderGraduate','Finance','rob1@gmail.com','1981-05-01'),
	('15','Esmeralda','Lang','M','UnderGraduate','Finance','pat1@gmail.com','1979-08-13');
    
   SELECT*FROM BOOK_COPIES;

INSERT INTO BOOK(Catalog_Number, Book_Catalog, Title, ISBN, Publish_Press, Publish_Date, Author_Fname, Author_Lname, Price, Num_Words, Intro)
VALUES('1','English','Puppies','9872343','Pearson','1961-12-10','Billy','Hill','10.00','1500','Dogs like to play when they are small'),
	  ('2','Foreign','How to Fly','5328994','Penguin Books','1984-02-01','John','Wick','25.00','2000','Flying is not an easy thing to learn'),
      ('3','English','Green Eggs and Ham','3259853','McGraw Hill','1992-05-07','Dr','Suss','14.99','250','I like to eat green eggs and ham'),
	  ('4','New','Learning Python','5319023','Pearson','2010-10-14','Jonny','Ramon','29.99','5000','Python is language of the future'),
      ('5','English','How to Write Essays','9234037','McGraw Hill','2007-03-27','Phil','Perks','19.00','2000','The introduction is the most important'),
	  ('6','English','Coding','3214983','McGraw Hill','1974-07-19','Sally','Smith','7.00','2000','C is one of the oldest languges'),
	  ('7','Foreign','The Human Body','4928235','Penguin Books','1999-10-08','Rammy','Hernandez','5.00','5000','The brain is a complex part'),
      ('8','English','History of English','7298874','Pearson','1986-11-17','Taylor','Stephan','4.99','2500','English originated at'),
	  ('9','New','Sponge Bob','6792894','Pearson','2000-06-20','Gary','Ramos','9.99','1000','Are you ready kids'),
      ('10','Foreign','Spanish to English','2098479','Penguin Books','1999-03-22','Hector','Perez','9.00','2000','Hola is Hello');

INSERT INTO BORROWER (ID, Fname, Lname, Gender, Borrower_Type, Dept, Email, Birthday)
VALUES ('11','Sammy','Maze','M','Faculty','Computer Science','scott1@gmail.com','1991-11-01'),
	('12','Frank','Hill','M','Faculty','Electrical Engineering','john1@gmail.com','1999-05-21'),
    ('13','Steve','Rodriguez','M','Faculty','Computer Science','bill1@gmail.com','1997-03-19');


INSERT INTO AUTHORS(Catalog_Number, Author_Fname, Author_Lname)
VALUES('1','Billy','Hill'),
	('2','John','Wick'),
    ('3','Dr','Suss'),
    ('4','Jonny','Ramon'),
    ('5', 'Phil','Perks'),
    ('6','Sally','Smith'),
    ('7','Rammy','Hernandez'),
    ('8','Taylor','Stephan'),
    ('9','Gary','Ramos'),
    ('10','Hector','Perez');


INSERT INTO LIBRARY_BRANCH(Branch_ID, Branch_Name)
VALUES('1','Branch A'),
	('2','Branch B'),
    ('3','Branch C');
    
    INSERT INTO LOANS(LoanID, Borrower_ID, Copy_ID, Branch_ID, Extension, Checked_Out, Due_Date, Date_Returned, Fee)
VALUES('1', '11','5','3','0','2019-11-15','2020-01-14','9999-01-01','0.00'),
	('2','12','5','3','1','2019-06-10','2019-08-07','9999-01-01','0.00'),
    ('3','13','1','3','0','2019-12-08','2020-02-06','2020-02-06','0.00'),
    ('4','14','7','2','0','2019-11-20','2020-01-19','9999-01-01','0.00'),
	('5','15','10','1','1','2019-10-06','2020-02-03','9999-01-01','0.00'),
    ('6','10','6','2','0','2019-10-09','2019-12-08','9999-01-01','0.40'),
    ('7','7','5','3','1','2019-10-11','2019-12-10','9999-01-01','0.00');




 INSERT INTO LOANS(LoanID, Borrower_ID, Copy_ID, Branch_ID, Extension, Checked_Out, Due_Date, Date_Returned, Fee)
VALUES('8', '3','7','2','0','2019-10-09','2019-12-08','9999-01-01','0.40');


INSERT INTO LOANS(LoanID, Borrower_ID, Copy_ID, Branch_ID,Extension, Checked_Out, Due_date, Date_Returned, Fee)
VALUES     ('9','1','6','1','1','2019-10-11','2019-12-10','9999-01-01','0.00'),
        ('10','8','4','2','1','2019-10-11','2019-12-10','9999-01-01','0.00');

INSERT INTO LOANS(LoanID, Borrower_ID, Copy_ID, Branch_ID,Extension, Checked_Out, Due_date, Date_Returned, Fee)
VALUES     ('11','12','6','1','0','2019-10-11','2019-12-10','9999-01-01','0.00'),
        ('12','13','4','2','1','2019-10-11','2019-12-10','9999-01-01','0.00');



	INSERT INTO Loan_Type(Loan_Catalog, LType, Loan_Period, Loaned_Number, Extension_Limit, Late_Fee)
VALUES('English','English Books','60','12','1','0.20'),
('Foreign','Foreign Books','60','3','1','0.50'),
('New','Short Loan','60','1','0','0.50');

	INSERT INTO Book_Copies(Copy_ID, Catalog_Number, Branch_ID, NumOfCopies)
VAlUES ('1','1','3','10'),   
	('2','2','1','4'), 
	('3','3','3','8'), 
	('4','4','2','3'), 
	('5','5','3','5'), 
	('6','6','2','12'), 
	('7','7','2','1'), 
	('8','8','3','3'), 
	('9','9','1','9'),
	('10','10','1','4');
	
INSERT INTO BOOK_COPIES (Copy_ID, Catalog_Number, Branch_ID, NumOfCopies)
VALUES ('11','9','3','2');



#Project Quaries


#1. Create a view to show all the books borrowed by the faculty in ‘Electrical Engineering’ department.
#DONE
CREATE VIEW view3 as SELECT BOOK.Catalog_Number,BOOK.Title FROM BOOK
inner join BOOK_COPIES on BOOK.Catalog_Number=BOOK_COPIES.Catalog_Number
inner join LOANS on BOOK_COPIES.Copy_ID=LOANS.Copy_ID
inner join BORROWER on LOANS.Borrower_ID=BORROWER.ID
where BORROWER.Borrower_Type='Faculty' and BORROWER.Dept='Electrical Engineering';
select * from view3;



#2. Show the average number of books borrowed by the graduate students in ‘Computer Science’ department.
#

SELECT AVG(boc) FROM
(SELECT * FROM BORROWER WHERE Borrower_Type = 'Graduate' AND Dept = 'Computer Science') AS bo
INNER JOIN (SELECT Borrower_ID, COUNT(Copy_ID) as boc FROM LOANS GROUP BY Borrower_ID) as bk on bo.ID=bk.Borrower_ID;

#3. Give the information of all the undergraduate students who have books overdue, and the fine they should pay for the books overdue.
#DONE
SELECT * FROM BORROWER WHERE ID IN(SELECT Borrower_ID FROM LOANS WHERE Due_Date < curdate() AND Borrower_Type = 'Undergraduate');



#4. Find the names of the library branches where all "faculty" members from ‘Computer Science’ have borrowed some book from.
#DONE

SELECT Branch_Name FROM LIBRARY_BRANCH WHERE Branch_ID IN (SELECT DISTINCT Branch_ID FROM LOANS WHERE Borrower_ID IN(SELECT ID FROM BORROWER WHERE Borrower_Type = 'Faculty' AND Dept = 'Computer Science' ));


 #5. Get the titles of books that can be only borrowed from Branch B.
#DONE
SELECT Title FROM BOOK WHERE Catalog_Number IN(SELECT Catalog_Number FROM Book_Copies WHERE Branch_ID != '1' AND Branch_ID != '3');


#6. Show the students in ‘Finance’ department who renewed the current borrowed book.
#DONE

SELECT Fname, Lname FROM BORROWER WHERE Dept = 'Finance' AND ID IN(SELECT Borrower_ID FROM LOANS WHERE Extension > '0');

#7. Show all the new books that are not loaned out from Branch C. (Note that if all copies of a book are loaned out, then this book is loaned out.)
#Done

SELECT distinct(Catalog_Number) FROM BOOK_COPIES WHERE Catalog_Number 
IN (SELECT Catalog_Number FROM BOOK WHERE Book_Catalog = 'New') AND BOOK_COPIES.Branch_ID = '3' and BOOK_COPIES.NumOfCopies > 0 ;

#8. Find out the presses who have the maximum total numbers of book copies in Branch A, Branch B, and Branch C respectively.

SELECT Publish_Press FROM BOOK WHERE Catalog_Number IN(SELECT count(Catalog_Number) FROM Book_Copies WHERE Branch_ID);

