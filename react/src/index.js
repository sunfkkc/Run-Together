import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
//import { store } from './app/store';
import { Provider } from 'react-redux'; //리덕스 사용하려면 <App />을 Provider로 감싸야 함
import * as serviceWorker from './serviceWorker';

ReactDOM.render(
  <React.StrictMode>
      <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
