create schema if not exists AlphaCalc;

drop table if exists Skills_users;
drop table if exists Skills_Tasks;
drop table if exists Skills;
drop table if exists Tasks_Users;
drop table if exists Tasks;
drop table if exists Subprojects;
drop table if exists Projects;
drop table if exists Users;


create table Users (
                       Username varchar (50) unique,
                       Firstname varchar (50),
                       Lastname varchar (50),
                       Password varchar (50),
                       Email varchar (250),
                       Streetname varchar (50),
                       Streetnumber varchar (50),
                       Postnumber int,
                       Phonenumber int,
                       City varchar (50),
                       Country varchar (50),
                       primary key (username));

create table Projects (
                          ID int auto_increment,
                          Name varchar(30) not null unique,
                          description varchar(250),
                          deadline varchar(20),
                          estimatedhours double,
                          usedhours double,
                          username varchar (50),
                          foreign key (username) references users(username),
                          primary key (ID)
);

create table Subprojects (
                             ID int auto_increment,
                             Name varchar(30) not null,
                             description varchar(250),
                             estimatedhours double,
                             usedhours double,
                             project_ID int,
                             foreign key (project_ID) references projects(ID),
                             primary key (ID)
);

create table Tasks (
                       ID int auto_increment,
                       Name varchar(30) not null,
                       description varchar(250),
                       estimatedhours double,
                       usedhours double,
                       subproject_ID int,
                       foreign key (subproject_ID) references subprojects(ID),
                       primary key (ID),
                       done varchar (1),
                       status varchar(15)
);


CREATE TABLE Tasks_Users (
                             Task_ID int,
                             Username varchar(50),
                             PRIMARY KEY (Task_ID, Username),
                             FOREIGN KEY (Task_ID) REFERENCES tasks(ID),
                             FOREIGN KEY (username) REFERENCES Users(username)
);

CREATE TABLE Skills(
                       id int auto_increment,
                       Name varchar(30),
                       primary key(ID)
);

CREATE TABLE SKILLS_USERS(
                             SKILL_ID int,
                             Username varchar(50),
                             PRIMARY KEY (SKILL_ID, Username),
                             FOREIGN KEY (SKILl_ID) REFERENCES skills(ID),
                             FOREIGN KEY (username) REFERENCES Users(username)
);

CREATE TABLE SKILLS_TASKS(
                             SKILL_ID int,
                             TASK_ID int,
                             PRIMARY KEY (SKILL_ID, TASK_ID),
                             FOREIGN KEY (SKILL_ID) REFERENCES skills(ID),
                             FOREIGN KEY (TASK_ID) REFERENCES TASKS(ID)
);

-- Personer indsat:

Insert into users(username, firstname, lastname, password, email, streetname, streetnumber, postnumber, phonenumber, city, country) values ('Bobby', 'Bobby', 'Bobsen', 'bobby555', 'bobbyersej@gmail.com', 'Bobbyvej', '66', 2200, 12345678, 'København', 'Danmark');
Insert into users(username, firstname, lastname, password, email, streetname, streetnumber, postnumber, phonenumber, city, country) values ('abc', 'Harry', 'Potter', '123', 'harrypotter@mail.co.uk', 'Londonroad', '29', 10000, 1234567899, 'London', 'England');
Insert into users(username, firstname, lastname, password, email, streetname, streetnumber, postnumber, phonenumber, city, country) values ('HanneRocks', 'Hanne', 'Jensen', '1234', 'hanneframarketing@gmail.com', 'Taffelæblevej', '13', 4000, 87654321, 'Roskilde', 'Sverige');

-- Projekter:

Insert into projects(name, description, deadline, estimatedhours, usedhours, username) values ('Eksamen', 'dette er en eksamen', '2024-05-29', 13, 0, 'Bobby');
Insert into projects(name, description, deadline, estimatedhours, usedhours, username) values ('Skoleprojekt', 'dette er et projekt til skolen i foråret 2024', '2024-05-31', 0, 0,  'Bobby');

-- subprojects:

Insert into subprojects(name, description, estimatedhours, usedhours, project_ID) values ('PO meeting', 'Mødes med Luise', 11, 0, 1);
Insert into subprojects(name, description, estimatedhours, usedhours, project_ID) values ('Code review', 'Mødes med Tine', 2, 0, 1);
Insert into subprojects(name, description, estimatedhours, usedhours, project_ID) values ('Business', 'Stuff', 0, 0, 2);

-- tasks:
Insert into tasks(name, description, estimatedhours, usedhours, subproject_ID, done, status) values ('Userstory 1', 'vis userstory til Luise', 5, 0, 1, 'f', 'high');
Insert into tasks(name, description, estimatedhours, usedhours, subproject_ID, done, status) values ('Userstory 2', 'vis userstory til Luise', 6, 0, 1, 'f', 'uncategorized');
Insert into tasks(name, description, estimatedhours, usedhours, subproject_ID, done, status) values ('Userstory 3', 'vis userstory til Luise igen', 2, 0, 2, 'f', 'uncategorized');


-- Assign users to tasks:

Insert into tasks_users(task_ID, username) values (1, 'abc');
Insert into tasks_users(task_ID, username) values (2, 'abc');
Insert into tasks_users(task_ID, username) values (1, 'HanneRocks');

-- Skills:

Insert into skills(name) values ('Java');
Insert into skills(name) values ('HTML');
Insert into skills(name) values ('CSS');
Insert into skills(name) values ('SQL');
Insert into skills(name) values ('Python');
Insert into skills(name) values ('C++');
Insert into skills(name) values ('C#');
Insert into skills(name) values ('JavaScript');
Insert into skills(name) values ('Frontend');
Insert into skills(name) values ('Salesforce Apex udvikling');
Insert into skills(name) values ('Salesforce LWC udvikling');
Insert into skills(name) values ('Salesforce CC udvikling');
Insert into skills(name) values ('Azure integration udvikling');
Insert into skills(name) values ('Enterprise arkitektur');
Insert into skills(name) values ('Projektledelse');
Insert into skills(name) values ('Programledelse');
Insert into skills(name) values ('QA Manager');
Insert into skills(name) values ('Manuel testing');
Insert into skills(name) values ('Test automation');
Insert into skills(name) values ('CRM business consultant');
Insert into skills(name) values ('e-commerce consultant');

-- Assign skills to users:
Insert into skills_users(Skill_ID, username) values (1, 'abc');
Insert into skills_users(Skill_ID, username) values (2, 'abc');
Insert into skills_users(Skill_ID, username) values (5, 'abc');
Insert into skills_users(Skill_ID, username) values (3, 'Bobby');
Insert into skills_users(Skill_ID, username) values (4, 'Bobby');
Insert into skills_users(Skill_ID, username) values (9, 'HanneRocks');
Insert into skills_users(Skill_ID, username) values (10, 'HanneRocks');

-- Assign skills to tasks;
Insert into skills_tasks(Skill_ID, task_id) values (1, 1);
Insert into skills_tasks(Skill_ID, task_id) values (2, 1);
Insert into skills_tasks(Skill_ID, task_id) values (3, 2);
Insert into skills_tasks(Skill_ID, task_id) values (4, 2);
Insert into skills_tasks(Skill_ID, task_id) values (5, 3);
Insert into skills_tasks(Skill_ID, task_id) values (6, 3);