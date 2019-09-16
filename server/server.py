# chat_server.py
 
import sys
import socket
import select
import json
import random

from conn import Conn

HOST = '' 
CONNECTIONS_LIST = []
RECV_BUFFER = 4096
PORT = 9009

# Tutorial at: https://www.bogotobogo.com/python/python_network_programming_tcp_server_client_chat_server_chat_client_select.php

def chat_server():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((HOST, PORT))
    server_socket.listen(15)
 
    main_conn = Conn(server_socket)

    # add server socket object to the list of readable connections
    CONNECTIONS_LIST.append(main_conn)
 
    print("Chat server started on port", str(PORT))
 
    while 1:
        sockets = (o.socket for o in CONNECTIONS_LIST)
        
        # get the list sockets which are ready to be read through select
        # 4th arg, time_out  = 0 : poll and never block
        ready_to_read,ready_to_write,in_error = select.select(sockets,[],[],0)

        for sock in ready_to_read:
            
            for conn in CONNECTIONS_LIST:
                if conn.socket == sock:
                    currentConn = conn

            # a new connection request recieved
            if currentConn == main_conn:
                sockfd, addr = server_socket.accept()
                CONNECTIONS_LIST.append(Conn(sockfd, addr))
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

                            broadcast(server_socket, sockfd, '{"servermsg": "' + conn.username + ' entrou no chat."}')
                        elif msg.get("action"):
                            if msg["action"] == "showusers":
                                usersStr = '{"users": ["'
                                for c in CONNECTIONS_LIST:
                                    if c != currentConn and c != main_conn:
                                        usersStr += c.username + '", "'

                                send_message(currentConn, usersStr[:-3] + ']}')
                        else:
                            # there is something in the socket
                            broadcast(server_socket, sock, '{"username": "' + currentConn.username + '", "color" : ' + currentConn.color + ', "content": "' + msg["content"] + '"}')
                    else:
                        # remove the socket that's broken    
                        if currentConn in CONNECTIONS_LIST:
                            CONNECTIONS_LIST.remove(currentConn)

                        # at this stage, no data means probably the connection has been broken
                        broadcast(server_socket, sock, '{"servermsg": Usuario "' + currentConn.username + ' saiu"}')
                # exception 
                except:
                    broadcast(server_socket, sock, '{"servermsg": "Usuario ' + currentConn.username + ' saiu"}')
                    if currentConn in CONNECTIONS_LIST:
                        CONNECTIONS_LIST.remove(currentConn)

                    continue

    server_socket.close()
 
# send message to a specific user
def send_message(conn, message):
    try :
        conn.socket.send(message)
    except :
        # broken socket connection
        conn.socket.close()
        # broken socket, remove it
        if conn in CONNECTIONS_LIST:
            CONNECTIONS_LIST.remove(conn)
   
# broadcast chat messages to all connected clients
def broadcast(server_socket, sock, message):
    for conn in CONNECTIONS_LIST:
        # send the message only to peer
        if conn.socket != server_socket and conn.socket != sock :
            send_message(conn, message)
 
if __name__ == "__main__":
    sys.exit(chat_server())      

