import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.css';
import App from './component/App'
import reportWebVitals from './reportWebVitals';
import { StyledEngineProvider } from '@mui/material/styles';

ReactDOM.createRoot(document.querySelector("#root")!).render(
    <React.StrictMode>
        <StyledEngineProvider injectFirst>
            <App />
        </StyledEngineProvider>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
