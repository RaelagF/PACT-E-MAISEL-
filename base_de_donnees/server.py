import socket
import service

def main():

	s = socket.socket()
	#release the port immediately after the process is finished
	s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1)
	#host = socket.gethostname()
	host = '10.154.0.2'
	port = 50000
	s.bind((host, port))
	s.listen(10)
	print ("Server has started")

	updateA = service.UpdateAffluence()
	updateA.start()

	while True:
		conn, addr = s.accept()
		ser = service.Service(conn, addr)
		ser.start()


if __name__ == '__main__':
	main()