import threading
import functions_sqlite
import time
import states
 
#lock to protect global variables
mutex1 = threading.Lock() #for laverie
mutex2 = threading.Lock() #for stoves
mutex3 = threading.Lock() #for affluence

class Service(threading.Thread):
	#conn: connection, addr: ip address
	def __init__(self, connection, address): 
		threading.Thread.__init__(self)
		self.conn = connection
		self.addr = address
	
	def recv(self):
		ans = self.conn.recv(1024).decode()
		return ans

	def send(self, msg):
		self.conn.send(msg.encode())

	def login(self):
		[username, password] = self.recv().split()
		#three possible answers: "username doesn't exist" or "true" or "false"
		if not(functions_sqlite.username_already_exists(username)):
			self.send("username doesn't exist\n")
		else:
			success = functions_sqlite.login(username, password)
			self.send(str(success))

	def sign_up(self):
		#if the client didn't enter a room number, the client send '000' to the server
		#three possible answers: "username already exists" or "true" for success or "false" for failure
		#if it returns false, the client should show a sentence like"sorry, please try again", same for the other request
		[username, password, room] = self.recv().split()
		if room == '000':
			room = None

		if functions_sqlite.username_already_exists(username):
			self.send("username already exists\n")
		else:
			success = functions_sqlite.insert_user(username, password, room)
			self.send(str(success))

	def change_password(self):
		[username, password, newPassword] = self.recv().split()

		if not(functions_sqlite.username_already_exists(username)):
			self.send("username doesn't exist\n")
		elif not(functions_sqlite.login(username, password)):
			self.send("password is not correct\n")
		else:
			success = functions_sqlite.change_password(username, newPassword)
			self.send(str(success))

	#get washing machine states, return two lists: used and reserved 
	def get_WM_states(self):
		ack = self.recv() #wait for acknowledgemnt('ok\n') from client

		#protect the global variables shared by threads
		mutex1.acquire()
		
		self.send(" ".join(map(str,states.usedWs)) + '\n') #return the list in the form of a string with characters seperated by space "0 0 0 0 0"
		self.send(" ".join(map(str,states.reserved)) + '\n')
		#self.send(" ".join(map(str,states.lastReservation)) + '\n') 

		mutex1.release()

	def reserve_WM(self):
		[username, id] = self.recv().split() #username and machine id expected
		id = int(id)

		mutex1.acquire()

		if (states.reserved[id] == 0):
			success = functions_sqlite.add_record_reservation_ws(username, id)
			if (success == True):
				states.reserved[id] = 1
				states.lastReservation[id] = time.time()
			
				timer = TimeoutWs(id)
				timer.start()
				print (states.reserved)
				print (states.lastReservation)

			self.send(str(success))

		else: #in this case the client must send a 'get washing machine states' request to update 
			self.send('already reserved\n') #after it receives the message 'already reserved'

		mutex1.release()

	def cancel_reservation(self):
		id = int(self.recv())

		mutex1.acquire()

		states.reserved[id] = 0
		states.lastReservation[id] = None
		print (states.reserved)
		print (states.lastReservation)
		self.send('ok\n')

		mutex1.release()

	def update_WM_states(self):
		usedWs = list(map(int, self.recv().split()))

		mutex1.acquire()

		states.usedWs = usedWs
		print (states.usedWs)
		self.send('ok\n')

		mutex1.release()

	def get_St_states(self):
		ack = self.recv() #wait for acknowledgemnt('ok\n') from client

		#protect the global variables shared by threads
		mutex2.acquire()
		
		self.send(" ".join(map(str,states.N_Stoves_available)))

		mutex2.release()

	def update_St_states(self):
		N_Stoves_available = list(map(int, self.recv().split()))

		mutex2.acquire()

		states.N_Stoves_available = N_Stoves_available
		print (states.N_Stoves_available)
		self.send('ok\n')

		mutex2.release()		

	def get_Ki_affluence(self):
		ack = self.recv() #wait for acknowledgemnt('ok\n') from client

		#protect the global variables shared by threads
		mutex3.acquire()
		
		self.send(" ".join(map(str,states.affluence)))

		mutex3.release()

	def update_affluence(self):
		affluence = list(map(int, self.recv().split()))

		mutex3.acquire()

		states.affluence = affluence
		print (states.affluence)
		self.send('ok\n')

		mutex3.release()

	def client(self):
		print ('Client connected, address: ', self.addr)
		self.send('ok\n')

		request = self.recv()
		print (request)
		self.send('ok\n')

		if request == 'login\n':
			self.login()

		if request == 'sign up\n':
			self.sign_up()

		if request == 'change password\n':
			self.change_password()

		if request == 'get washing machine states\n':
			self.get_WM_states()

		if request == 'reserve washing machine\n':
			self.reserve_WM()

		if request == 'cancel reservation\n':
			self.cancel_reservation()

		if request == 'get stove states\n':
			self.get_St_states()

		if request == 'get kitchen affluence\n':
			self.get_Ki_affluence()


	def hardware(self):
		print ('Arduino connected, address: ', self.addr)
		self.send('ok\n')

		request = self.recv()
		print (request)
		self.send('ok\n')

		if request == 'get washing machine states\n':
			self.get_WM_states()

		if request == 'update washing machine states\n':
			self.update_WM_states()

		if request == 'update stove states\n':
			self.update_St_states()

		if request == 'update affluence\n':
			self.update_affluence()

	def run(self):

		#identity can be 'client' or 'hardware'
		identity = self.recv()
		
		print (identity)
		if identity == 'client\n':
			self.client()

		if identity == 'hardware\n':
			self.hardware()


#thread to cancel the reservation of washing machine after 5 minutes
class TimeoutWs(threading.Thread):
	def __init__(self, id):
		threading.Thread.__init__(self)
		self.id = id

	def run(self):
		time.sleep(300) #wait for 5 minutes
		states.reserved[self.id] = 0
		states.lastReservation[self.id] = None


#thread to update affluence every hour
class UpdateAffluence(threading.Thread):
	def __init__(self):
		threading.Thread.__init__(self)

	def run(self):
		while True:
			time.sleep(3600) #every hour
			functions_sqlite.update_affluence(states.affluence, states.Nk)