// server.js
const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 8080 });

wss.on('connection', ws => {
    ws.on('message', message => {
        console.log('received: %s', message);
        // Broadcast the message to all clients except the sender
        wss.clients.forEach(client => {
            if (client !== ws && client.readyState === WebSocket.OPEN) {
                client.send(message);
            }
        });
    });

    // Send a welcome message to the newly connected client
    ws.send('Connected to WebSocket server');

    // You can also broadcast to all clients that a new client has connected
    wss.clients.forEach(client => {
        if (client !== ws && client.readyState === WebSocket.OPEN) {
            client.send('A new client has connected');
        }
    });
});

console.log('WebSocket server is running on ws://localhost:8080');
