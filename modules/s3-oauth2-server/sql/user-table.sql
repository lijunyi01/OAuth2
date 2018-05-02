CREATE TABLE account
(
  id INT NOT NULL AUTO_INCREMENT,
  user_name VARCHAR(50),
  email VARCHAR(255),
  password VARCHAR(512),
  role_string VARCHAR(50),
  PRIMARY KEY (id)
);

INSERT INTO account(user_name, email, password, role_string) VALUES ('user', 'user@sample.com', '123', 'ROLE_USER');