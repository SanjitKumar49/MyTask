# pc_client.py
import websocket
import threading

def on_message(ws, message):
    print(f"Received from server: {message}")

def on_error(ws, error):
    print(error)

def on_close(ws):
    print("### closed ###")

def on_open(ws):
    def run(*args):
        while True:
            message = input("Enter message: ")
            ws.send(message)
    threading.Thread(target=run).start()

websocket.enableTrace(True)
ws = websocket.WebSocketApp("ws://localhost:8080/",
                          on_message=on_message,
                          on_error=on_error,
                          on_close=on_close)
ws.on_open = on_open
ws.run_forever()
