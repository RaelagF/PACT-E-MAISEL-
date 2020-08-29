import sqlite3

conn = sqlite3.connect('e-maisel.db')
cursor = conn.cursor()
#turn on foreign key constraints
cursor.execute('PRAGMA foreign_keys = ON;')

#create table Users
cursor.execute('''
	CREATE TABLE Users
	(
		id 					INT 		NOT NULL PRIMARY KEY,
		username 			VARCHAR(20) NOT NULL,
		password			VARCHAR(20) NOT NULL,
		room_number 		INT 		
	)
	''')

#initialize table Users
username = ['Soline', 'Zoe', 'Alexandre', 'Florian', 'Guyu', 'Paul', 'Raphael']
password = ['000', '111', '222', '333', '444', '555', '666']

for i in range(len(username)):
	cursor.execute('''INSERT INTO Users
		VALUES(?, ?, ?, NULL)''', (i, username[i], password[i]))


#create table Statistics_ws (reservation of washing machines)
cursor.execute('''
	CREATE TABLE Statistics_ws
	(
		id			 		INT 		NOT NULL PRIMARY KEY,
		reservation_time	TEXT		NOT NULL,
		user_id				INT 		NOT NULL,
		ws_id 				INT 		NOT NULL,
		FOREIGN KEY(user_id) 			REFERENCES Users(id)		
	)
	''')

#create table Statistics_ki (number of people in each kitchen)
cursor.execute('''
	CREATE TABLE Statistics_ki
	(
		id 					INT 		NOT NULL PRIMARY KEY,
		time 				TEXT		NOT NULL,
		floor 				INT 		NOT NULL, 
		num 				INT 		NOT NULL
	)
	''')

conn.commit()
conn.close()