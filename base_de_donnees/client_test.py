import socket
import time

s = socket.socket()
server_ip = socket.gethostname()
#server_ip = '35.189.106.175'
port = 50000
s.connect((server_ip, port))

s.send('client\n'.encode())
rec = s.recv(1024)
s.send('reserve washing machine\n'.encode())
rec = s.recv(1024)
s.send(('Guyu 5\n').encode())
rec = s.recv(1024).decode()
print (rec)

s.close()
