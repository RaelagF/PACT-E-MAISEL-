import socket
import time

s = socket.socket()
server_ip = socket.gethostname()
#server_ip = '35.189.106.175'
port = 50000
s.connect((server_ip, port))

s.send('hardware\n'.encode())
rec = s.recv(1024)
s.send('update washing machine states\n'.encode())
rec = s.recv(1024)
s.send(('0 0 1 0 1 1 1 0\n').encode())
rec = s.recv(1024).decode()
print (rec)

s.close()
