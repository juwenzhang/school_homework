import { StrictMode } from 'react'
import React, { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { Garfish } from 'garfish';
import './index.css';
import App from './App';

// 初始化微前端框架
const garfish = new Garfish({
  basename: '/',
  domGetter: '#subapp-container',
  apps: [
    {
      name: 'blog-app',
      entry: 'http://localhost:5174',
      activeWhen: '/blog',
      container: '#subapp-container',
      props: {
        base: '/blog',
      },
    },
    {
      name: 'admin-app',
      entry: 'http://localhost:5175',
      activeWhen: '/admin',
      container: '#subapp-container',
      props: {
        base: '/admin',
      },
    },
    {
      name: 'monitor-app',
      entry: 'http://localhost:5176',
      activeWhen: '/monitor',
      container: '#subapp-container',
      props: {
        base: '/monitor',
      },
    },
  ],
  protectVariable: ['React', 'ReactDOM'],
  beforeLoad(appInfo) {
    console.log('beforeLoad:', appInfo.name);
    return true;
  },
  afterLoad(appInfo) {
    console.log('afterLoad:', appInfo.name);
  },
  beforeMount(appInfo) {
    console.log('beforeMount:', appInfo.name);
    return true;
  },
  afterMount(appInfo) {
    console.log('afterMount:', appInfo.name);
  },
  beforeUnmount(appInfo) {
    console.log('beforeUnmount:', appInfo.name);
    return true;
  },
  afterUnmount(appInfo) {
    console.log('afterUnmount:', appInfo.name);
  },
});

// 全局注册微前端实例
(window as any).garfish = garfish;

// 启动React主应用
const root = createRoot(document.getElementById('root')!);
root.render(
  <StrictMode>
    <BrowserRouter>
      <App garfish={garfish} />
    </BrowserRouter>
  </StrictMode>
);

// 启动微前端框架
garfish.start();
