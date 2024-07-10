// src/WebSocketService.js
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
    constructor() {
        this.client = new Client({
            webSocketFactory: () => new SockJS('http://ec2-13-60-35-198.eu-north-1.compute.amazonaws.com:8080/ws'),
            debug: (str) => { console.log(new Date(), str); },
        });
    }

    connect(onMessageReceived) {
        this.client.onConnect = () => {
            console.log('WebSocket connection established');
            this.client.subscribe('/topic/voltage', (message) => {
                const data = JSON.parse(message.body);
                onMessageReceived(data);
            });
        };

        this.client.onDisconnect = () => {
            console.log('WebSocket connection closed');
        };

        this.client.activate();
    }
}

const webSocketService = new WebSocketService();
export default webSocketService;
