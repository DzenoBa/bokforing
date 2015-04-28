insert into UserInfo (userInfoId, userName) VALUES(1, 'Jakob');
insert into UserAccount (id, email, userInfoId) VALUES(1, 'apa@test.com', 1);
insert into Customers (customerNumber, name, phoneNumber, userAccount_id) VALUES (1, '123', '123', 1);

insert into Verifications (id, verificationNumber, userAccount_id) VALUES (1, 125, 1);
insert into Verifications (id, verificationNumber, userAccount_id) VALUES (2, 133, 1);
insert into Verifications (id, verificationNumber, userAccount_id) VALUES (3, 156, 1);
insert into Verifications (id, verificationNumber, userAccount_id) VALUES (4, 7371, 1);
insert into Verifications (id, verificationNumber, customer_id, userAccount_id) VALUES (5, 9, 1, 1);