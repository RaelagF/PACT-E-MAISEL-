import sqlite3

def open_connection():
	conn = sqlite3.connect("e-maisel.db")
	cursor = conn.cursor()
	cursor.execute('PRAGMA foreign_keys = ON')
	return conn, cursor

def close_connection(conn, cursor):
	cursor.close();
	conn.close();

#return true if the username is in the database
def username_already_exists(username):
	conn, cursor = open_connection()
	res = False

	cursor.execute('SELECT * FROM Users WHERE username = ?', (username,))

	if not(cursor.fetchone() == None):
		res = True

	close_connection(conn, cursor)
	return res

#return true if it's successful
def insert_user(username, password, room_number = None): #None will be converted to NULL
	conn, cursor = open_connection()
	successful = False

	try:
		cursor.execute('SELECT MAX(id) FROM Users')
		result = cursor.fetchone()

		if result == (None,):
			id = 0
		else:
			id = result[0] + 1

		cursor.execute('''INSERT INTO Users(id, username, password, room_number)
		VALUES(?, ?, ?, ?)''', (id, username, password, room_number))
	
	except:
		conn.rollback()

	else:
		conn.commit() 
		successful = True

	close_connection(conn, cursor)
	return successful

#return true if it's successful
def login(username, password):

	conn, cursor = open_connection()
	successful = False

	cursor.execute('SELECT password FROM Users WHERE username = ?', (username,))
	result = cursor.fetchone()

	if result[0] == password:
		successful = True

	close_connection(conn, cursor)
	return successful

#return true if it's successful
def change_password(username, newPassword):
	conn, cursor = open_connection()
	successful = False

	try:
		conn.execute('UPDATE Users SET password = ? WHERE username = ?', (newPassword, username))

	except:
		conn.rollback()

	else:
		conn.commit()
		successful = True

	close_connection(conn, cursor)
	return successful

def add_record_reservation_ws(username, ws_id):
	conn, cursor = open_connection()
	successful = False

	try:
		cursor.execute('SELECT MAX(id) FROM Statistics_ws')
		result = cursor.fetchone()

		if result == (None,):
			id = 0
		else:
			id = result[0] + 1

		cursor.execute("SELECT datetime('now', 'localtime')")
		reservation_time = cursor.fetchone()[0]

		cursor.execute('SELECT id FROM Users WHERE username = ?', (username,))
		user_id = cursor.fetchone()[0]

		cursor.execute('''INSERT INTO Statistics_ws(id, reservation_time, user_id, ws_id)
		VALUES(?, ?, ?, ?)''', (id, reservation_time, user_id, ws_id))

	except:
		conn.rollback()

	else:
		conn.commit()
		successful = True

	close_connection(conn, cursor)
	return successful

def update_affluence(affluence, N):
	conn, cursor = open_connection()
	successful = False

	try:
		for i in range(N):
			cursor.execute('SELECT MAX(id) FROM Statistics_ki')
			result = cursor.fetchone()

			if result == (None,):
				id = 0
			else:
				id = result[0] + 1

			cursor.execute("SELECT datetime('now', 'localtime')")
			time = cursor.fetchone()[0]

			floor = i + 2 #i = 0 corresponds to the 2nd floor

			cursor.execute('''INSERT INTO Statistics_ki(id, time, floor, num)
			VALUES(?, ?, ?, ?)''', (id, time, floor, affluence[i]))

	except:
		conn.rollback()

	else:
		conn.commit()
		successful = True

	close_connection(conn, cursor)
	return successful


def main():
	insert_user('abytc', 'bctd', None)

if __name__ == '__main__':
	main()