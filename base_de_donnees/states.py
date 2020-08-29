#global variables for lingerie
Nw = 16 #number of washing machines
usedWs = [0] * Nw #a list of 0/1 which represents whether the machine is used or not, initialized to 0
reserved = [0] * Nw #a list of 0/1 which represents whether the machine is reserved or not, initialized to 0
lastReservation = [None] * Nw # a list of time/None to note the moment when the last reservation begins, None if it's not reserved

#global variables for kitchen
Nk = 8 #number of kitchens
N_Stoves_available = [0] * Nk #number of stoves available in every kitchen
affluence = [0] * Nk #number of people in every kitchen