create schema mobileapp;

use mobileapp;

create table tblUser (
	id int auto_increment primary key,
	username varchar(255),
    password varchar(255),
    name varchar(255),
    dob date,
    phonenumber varchar(10)
);

create table tblEvent(
	id int auto_increment primary key,
    userId int,
    foreign key (userId) references tblUser(id),
    name varchar(255),
	starttime varchar(255),
	endtime varchar(255),
    location varchar(255),
	address varchar(255),
	city varchar(255),
	des varchar(255),
	eventvideo varchar(255),
	registrationtype varchar(255),
	websitelink varchar(255),
	imgurl varchar(255),
	startdate date,
	enddate date
);

create table tblCategory(
	id int auto_increment primary key,
    name varchar(255),
    des varchar(255)
);

create table tblEventCategory(
	id int auto_increment primary key,
    eventId int,
    categoryId int,
    foreign key (eventId) references tblEvent(id),
    foreign key (categoryId) references tblCategory(id)
);

create table tblTask(
	id int auto_increment primary key,
    eventId int,
    foreign key (eventId) references tblEvent(id)
);

create table tblSchedule(
	id int auto_increment primary key,
    eventId int,
    foreign key (eventId) references tblEvent(id)
);

insert into tblcategory (name) values("kids");
insert into tblcategory (name) values("virtual");
insert into tblcategory (name) values("tour");

insert into tbluser (username, password, name, dob, phonenumber) values ("ruypa", "123456", "Duy", "2002-12-10", "0123456789");

insert into tblevent ( userId, name, starttime, endtime, location, 
						address, city, des, eventvideo, registrationtype, 
						websitelink, imgurl, startdate, enddate) 
			values (1, "Ptit tour", "06:00 am", "08:00 pm", "PTIT", "Nguyen Trai km12", 
					"Ha Noi", "very fun", "link", "venue", "link", 
                    "https://res.cloudinary.com/dkf74ju3o/image/upload/v1711613331/lu9evkymo1e0butoq7zb.png", 
                    "2024-03-31", "2024-03-31");

insert into tbleventcategory(eventId, categoryId) values( 1, 4);
insert into tbleventcategory(eventId, categoryId) values( 1, 5);

use mobileapp;
alter table tblschedule add name varchar(255);
alter table tblschedule add startTime varchar(255);
alter table tblschedule add endTime varchar(255);
alter table tblschedule add endDate date;
alter table tblschedule add startDate date;
alter table tblschedule add des varchar(255);
