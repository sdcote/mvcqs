-- This holds the basic SQL for the sample application. It should be generic 
-- and easy to port from one database to another.

-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- List of roles in a context
DROP TABLE SECURITY_ROLE CASCADE;
CREATE CACHED TABLE SECURITY_ROLE
(
   CONTEXT      VARCHAR(64)    NOT NULL,
   ROLE         VARCHAR(64)    NOT NULL,
   DESCRIPTION  VARCHAR(255)   NOT NULL
);

ALTER TABLE SECURITY_ROLE
   ADD CONSTRAINT PK_SECURITY_ROLE
   PRIMARY KEY (CONTEXT, ROLE);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- List of permissions for roles in a context
DROP TABLE SECURITY_ROLE_PERMISSION CASCADE;
CREATE CACHED TABLE SECURITY_ROLE_PERMISSION
(
   CONTEXT     VARCHAR(64)    NOT NULL,
   ROLE        VARCHAR(64)    NOT NULL,
   TARGET      VARCHAR(255)   NOT NULL,
   PERMISSION  BIGINT
);

ALTER TABLE SECURITY_ROLE_PERMISSION
   ADD CONSTRAINT PK_SECURITY_ROLE_PERMISSION
   PRIMARY KEY (CONTEXT, ROLE, TARGET);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- List of logins in a context (linked to external party identifier)
DROP TABLE SECURITY_LOGIN CASCADE;
CREATE CACHED TABLE SECURITY_LOGIN
(
   LOGIN                    BIGINT        NOT NULL AUTO_INCREMENT,
   CONTEXT                  VARCHAR(64)   NOT NULL,
   NAME                     VARCHAR(64)   NOT NULL,
   PASSWORD                 VARCHAR(64)   NOT NULL,
   PASSWORD_HINT            VARCHAR(64),
   IS_SYSTEM                BOOLEAN,
   IS_ENABLED               BOOLEAN,
   IS_LOGGEDOUT             BOOLEAN,
   REQUIRE_PASSWORD_CHANGE  BOOLEAN,
   CURRENCY_UOM             VARCHAR(64),
   LOCALE                   VARCHAR(64),
   TIMEZONE                 VARCHAR(64),
   DISABLED_DATETIME        TIMESTAMP,
   PARTY                    BIGINT
);

ALTER TABLE SECURITY_LOGIN
   ADD CONSTRAINT PK_SECURITY_LOGIN
   PRIMARY KEY (CONTEXT, LOGIN);

CREATE UNIQUE INDEX IDX_SECURITY_LOGIN
   ON SECURITY_LOGIN (CONTEXT ASC, NAME ASC);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- List of login role memberships
DROP TABLE SECURITY_LOGIN_ROLE CASCADE;
CREATE CACHED TABLE SECURITY_LOGIN_ROLE
(
   CONTEXT    VARCHAR(64)   NOT NULL,
   LOGIN      BIGINT        NOT NULL,
   ROLE       VARCHAR(64)   NOT NULL,
   FROM_DATE  TIMESTAMP,
   THRU_DATE  TIMESTAMP
);

ALTER TABLE SECURITY_LOGIN_ROLE
   ADD CONSTRAINT PK_SECURITY_LOGIN_ROLE
   PRIMARY KEY (CONTEXT, LOGIN, ROLE);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- list of permission assigned to a login directly (addition to role)
DROP TABLE SECURITY_LOGIN_PERMISSION CASCADE;
CREATE CACHED TABLE SECURITY_LOGIN_PERMISSION
(
   CONTEXT     VARCHAR(64)    NOT NULL,
   LOGIN       BIGINT         NOT NULL,
   TARGET      VARCHAR(255)   NOT NULL,
   PERMISSION  BIGINT
);

ALTER TABLE SECURITY_LOGIN_PERMISSION
   ADD CONSTRAINT PK_SECURITY_LOGIN_PERMISSION
   PRIMARY KEY (CONTEXT, LOGIN, TARGET);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- list of permissions revoked from login directly (applied last)
DROP TABLE SECURITY_LOGIN_REVOCATION CASCADE;
CREATE CACHED TABLE SECURITY_LOGIN_REVOCATION
(
   CONTEXT     VARCHAR(64)    NOT NULL,
   LOGIN       BIGINT         NOT NULL,
   TARGET      VARCHAR(255)   NOT NULL,
   PERMISSION  BIGINT
);

ALTER TABLE SECURITY_LOGIN_REVOCATION
   ADD CONSTRAINT PK_SECURITY_LOGIN_REVOCATION
   PRIMARY KEY (CONTEXT, LOGIN, TARGET);


-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- The following are still under development
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Party - People, groups, external systems; just any system actor
DROP TABLE PARTY CASCADE;
CREATE TABLE PARTY
(
   PARTY    BIGINT         NOT NULL AUTO_INCREMENT,
   PARTY_TYPE  BIGINT,
   NAME        VARCHAR(64),
   DESCRIPTION VARCHAR(255)
);

ALTER TABLE PARTY
   ADD CONSTRAINT pk_party
   PRIMARY KEY (PARTY);   

   
   
   
   
--Some OAuth tables
ROP TABLE OAUTH_CONSUMER CASCADE;
CREATE TABLE OAUTH_CONSUMER
(
   CONSUMER_KEY  VARCHAR(64)   NOT NULL,
   LOGIN         VARCHAR(64)   NOT NULL,
   NAME          VARCHAR(45)    NOT NULL,
   SECRET        VARCHAR(32)    NOT NULL,
);

ALTER TABLE OAUTH_CONSUMER
   ADD CONSTRAINT pk_oauth_consumer
   PRIMARY KEY (CONSUMER_KEY);



DROP TABLE OAUTH_NONCE CASCADE;
CREATE TABLE OAUTH_NONCE
(
   CONSUMER_KEY  VARCHAR(64)   NOT NULL,
   NONCE         VARCHAR(32)    NOT NULL,
   ISSUED        TIMESTAMP      NOT NULL
);

ALTER TABLE OAUTH_NONCE
   ADD CONSTRAINT pk_oauth_nonce
   PRIMARY KEY (CONSUMER_KEY, NONCE);

ALTER TABLE OAUTH_NONCE
  ADD CONSTRAINT OAUTH_NONCE_CONSUMER_FK1 FOREIGN KEY (CONSUMER_KEY)
  REFERENCES OAUTH_CONSUMER (CONSUMER_KEY)
  ON UPDATE RESTRICT
  ON DELETE RESTRICT;

CREATE INDEX OAUTH_NONCE_CONSUMER_FK1_INDEX_D
   ON OAUTH_NONCE (CONSUMER_KEY ASC);


