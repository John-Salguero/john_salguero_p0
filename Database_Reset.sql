DROP TABLE IF EXISTS "User_Data";
CREATE TABLE IF NOT EXISTS "User_Data" (
	"Username" VARCHAR(25) NOT NULL PRIMARY KEY,
	"First_Name" VARCHAR(20) NOT NULL,
	"Last_Name" VARCHAR(25) NOT NULL,
	"Phone_Number" VARCHAR(20),
    "Email" VARCHAR(254),
    "Address_Line_1" VARCHAR(100) NOT NULL,
    "Address_Line_2" VARCHAR(100),
    "State" CHAR(2) NOT NULL,
    "Zip_Code" VArCHAR(10) NOT NULL,
	"SSN_Hash" CHAR(64) NOT NULL,
    "Pass_Hash" CHAR(64) NOT NULL
);

DROP TABLE IF EXISTS "Account_Data";
CREATE TABLE IF NOT EXISTS "Account_Data" (
  "Account_ID" CHAR(64) NOT NULL PRIMARY KEY,
  "Balance" NUMERIC(20, 2) NOT NULL,
  "Type" VARCHAR(9) NOT NULL,
  "Active" BOOL NOT NULL
);

DROP TABLE IF EXISTS "User_Accounts";
CREATE TABLE IF NOT EXISTS "User_Accounts" (
  "Username" VARCHAR(25) NOT NULL REFERENCES "User_Data"("Username") ON DELETE CASCADE ON UPDATE CASCADE,
  "Account_ID" CHAR(64) NOT NULL REFERENCES "Account_Data"("Account_ID") ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY ("Username", "Account_ID")
);

DROP TABLE IF EXISTS "Transactions";
CREATE TABLE IF NOT EXISTS "Transactions" (
  "Transacation_ID" CHAR(64) NOT NULL PRIMARY KEY,
  "Date_Made" Date,
  "Amount" NUMERIC(20, 2) NOT NULL,
  "From_Account" CHAR(64) REFERENCES "Account_Data"("Account_ID") ON DELETE RESTRICT ON UPDATE CASCADE,
  "To_Account" CHAR(64) REFERENCES "Account_Data"("Account_ID") ON DELETE RESTRICT ON UPDATE CASCADE
);