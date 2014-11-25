-- This holds the basic SQL for the sample application. It should be generic 
-- and easy to port from one database to another.


-- List of roles in a context
CREATE TABLE SECURITY_ROLE (
CONTEXT_ID varchar(255) NOT NULL,
ROLE varchar(64) NOT NULL,
PRIMARY KEY (CONTEXT_ID,ROLE)
);


-- List of permissions for roles in a context
CREATE TABLE SECURITY_ROLE_PERMISSION (
CONTEXT_ID varchar(255) NOT NULL,
ROLE varchar(64) NOT NULL,
TARGET varchar(255) NOT NULL,
PERMISSION long,
PRIMARY KEY (CONTEXT_ID,ROLE,TARGET)
);

-- List of logins in a context (linked to external party identifier)
CREATE TABLE SECURITY_LOGIN (
CONTEXT_ID varchar(255) NOT NULL,
LOGIN_ID long;
PARTY_ID long,
PRIMARY KEY (CONTEXT_ID,ROLE,TARGET)
);

-- List of credentials for a login in a context
CREATE TABLE SECURITY_CREDENTIAL (
CONTEXT_ID varchar(255) NOT NULL,
LOGIN_ID long;
CREDENTIAL varchar(64) NOT NULL,
DATA binary,
PRIMARY KEY (CONTEXT_ID,LOGIN_ID,CREDENTIAL)
);

-- List of Roles of a login in a context 
CREATE TABLE SECURITY_RBAC (
CONTEXT_ID varchar(255) NOT NULL,
LOGIN_ID long;
ROLE varchar(64) NOT NULL,
PRIMARY KEY (CONTEXT_ID,LOGIN_ID)
);

-- list of permission assigned to a login directly (addition to role)
CREATE TABLE SECURITY_LOGIN_PERMISSION (
CONTEXT_ID varchar(255) NOT NULL,
LOGIN_ID long;
TARGET varchar(255) NOT NULL,
PERMISSION long,
PRIMARY KEY (CONTEXT_ID,LOGIN_ID,TARGET)
);

-- list of permissions revoked from login directly (applied last)
CREATE TABLE SECURITY_LOGIN_REVOCATION (
CONTEXT_ID varchar(255) NOT NULL,
LOGIN_ID long;
TARGET varchar(255) NOT NULL,
PERMISSION long,
PRIMARY KEY (CONTEXT_ID,LOGIN_ID,TARGET)
);
