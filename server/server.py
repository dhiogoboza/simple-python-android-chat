# chat_server.py
 
import sys
import socket
import select
import json
import random

from conn import Conn

HOST = '' 
RECV_BUFFER = 4096
PORT = 9009

currentConnections = []

def chat_server():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((HOST, PORT))
    server_socket.listen(15)
 
    main_conn = Conn(server_socket)

    # add server socket object to the list of readable connections
    currentConnections.append(main_conn)
 
    print("Chat server started on port", str(PORT))
 
    while 1:
        sockets = (o.socket for o in currentConnections)

        # get the list sockets which are ready to be read through select
        # 4th arg, time_out  = 0 : poll and never block
        ready_to_read,ready_to_write,in_error = select.select(sockets,[],[],0)

        for sock in ready_to_read:
            for conn in currentConnections:
                if conn.socket == sock:
                    currentConn = conn

            # a new connection request recieved
            if currentConn == main_conn:
                sockfd, addr = server_socket.accept()
                currentConnections.append(Conn(sockfd, addr))
            # a message from a client, not a new connection
            else:
                # process data recieved from client, 
                try:
                    # receiving data from the socket.
                    data = sock.recv(RECV_BUFFER)
                    if data:
                        print("data received:", data)

                        msg = json.loads(data)

                        if msg.get("setname"):
                            currentConn.username = msg["setname"]
                            currentConn.color = "#%06x" % random.randint(0, 0xFFFFFF)
                            currentConn.userId = random.randint(0, 0xFFFFFF) # TODO: check if userId is unique

                            welcomeMsg = {'msgtype': 2, 'username': currentConn.username, 'color': str(currentConn.color), 'userid': currentConn.userId}
                            send_message(currentConn, json.dumps(welcomeMsg))

                            broadcast(server_socket, sockfd, '{"servermsg": "' + conn.username + ' entrou no chat."}')

                            # FIXME: add this in a function
                            usersJson = {"users": []}
                            for c in currentConnections:
                                if hasattr(c, 'username') and hasattr(c, 'userId') and hasattr(c, 'color'):
                                    user = {'username': c.username, 'color': str(c.color), 'userid': c.userId}
                                    usersJson['users'].append(user)

                            broadcast(server_socket, sockfd, json.dumps(usersJson))
                        elif msg.get("action"):
                            if msg["action"] == "showusers":
                                usersJson = {"users": []}
                                for c in currentConnections:
                                    if c != currentConn and c != main_conn and hasattr(c, 'username') and hasattr(c, 'userId') and hasattr(c, 'color'):
                                        user = {'username': c.username, 'color': str(c.color), 'userid': c.userId}
                                        usersJson['users'].append(user)

                                send_message(currentConn, json.dumps(usersJson))
                        else:
                            # there is something in the socket
                            message = {'content': msg["content"], 'username': currentConn.username, 'color': str(currentConn.color), 'userid': currentConn.userId}
                            broadcast(server_socket, sock, json.dumps(message))
                    else:
                        # remove the socket that's broken    
                        if currentConn in currentConnections:
                            currentConnections.remove(currentConn)

                        # at this stage, no data means probably the connection has been broken
                        broadcast(server_socket, sock, '{"servermsg": Usuario "' + currentConn.username + ' saiu"}')
                # exception 
                except:
                    broadcast(server_socket, sock, '{"servermsg": "Usuario ' + currentConn.username + ' saiu"}')
                    if currentConn in currentConnections:
                        currentConnections.remove(currentConn)

                    continue

    server_socket.close()
 
# send message to a specific user
def send_message(conn, message):
    print("data sent:", message)
    try :
        conn.socket.send(message + '\r')
    except :
        # broken socket connection
        conn.socket.close()
        # broken socket, remove it
        if conn in currentConnections:
            currentConnections.remove(conn)
   
# broadcast chat messages to all connected clients
def broadcast(server_socket, sock, message):
    for conn in currentConnections:
        # send the message only to peer
        if conn.socket != server_socket and conn.socket != sock :
            send_message(conn, message + '\r')
 
if __name__ == "__main__":
    sys.exit(chat_server())      

