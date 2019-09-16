class Conn:
   main = False
   username = ""
   color = ""

   def __init__(self, socket, addr = "", main = True):
      self.socket = socket
      self.addr = addr
      self.main = main
