import React, { useEffect, useState } from 'react';
import webSocketService from './WebSocketService';
import './App.css';

function App() {
    const [data, setData] = useState([
        { name: "Voltage", data: null },
        { name: "Current", data: null },
        { name: "Hz", data: null }
    ]);

    useEffect(() => {
        webSocketService.connect((receivedData) => {
            console.log('Received data from server:', receivedData);
            setData((prevData) => {
                const index = prevData.findIndex(item => item.name === receivedData.name);
                if (index !== -1) {
                    const updatedData = [...prevData];
                    updatedData[index].data = parseInt(receivedData.data.replace(/[^\d]/g, ''), 10);
                    return updatedData;
                } else {
                    return [...prevData, receivedData];
                }
            });
        });
    }, []);

    return (
        <div className="App">
            <h1>Teltonika Data Receiver</h1>
            <h2>Received Data:</h2>
            <div className="gauge-container">
                {data.map((item, index) => (
                    <div className="gauge" key={index}>
                        <div className="gauge__body">
                            <div className="gauge__fill" style={{ transform: `rotate(${(item.data / 1000) /2}turn)` }}></div>
                            <div className="gauge__cover">
                                <div>{item.data}</div>  {item.name}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default App;
