import React from 'react';
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import GlobalStyle from './styles/global';
import BlogLayout from './components/layout/BlogLayout';
import BlogHome from './pages/BlogHome';
import ArticleDetail from './pages/ArticleDetail';
import CategoryPage from './pages/CategoryPage';
import TagPage from './pages/TagPage';
import AuthorPage from './pages/AuthorPage';
import SearchResultPage from './pages/SearchResultPage';
import NotFoundPage from './pages/NotFoundPage';
import TrackService from './utils/track';

// 应用初始化
function initApp() {
  // 1. 加载全局配置
  console.log('Loading app configuration...');
  
  // 2. 初始化埋点服务
  console.log('Initializing tracking service...');
  TrackService.track('app_load');
  
  // 3. 检查用户登录状态
  console.log('Checking user login status...');
  
  // 4. 预加载必要数据
  console.log('Preloading essential data...');
}

// 创建路由
const router = createBrowserRouter([
  {
    path: '/',
    element: <BlogLayout />,
    children: [
      {
        path: '/',
        element: <BlogHome />
      },
      {
        path: '/blog/article/:articleId',
        element: <ArticleDetail />
      },
      {
        path: '/blog/category/:categoryId',
        element: <CategoryPage />
      },
      {
        path: '/blog/tag/:tagId',
        element: <TagPage />
      },
      {
        path: '/blog/author/:authorId',
        element: <AuthorPage />
      },
      {
        path: '/blog/search',
        element: <SearchResultPage />
      }
    ],
    errorElement: <NotFoundPage />
  }
]);

// 渲染应用
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <GlobalStyle />
    <RouterProvider router={router} />
  </React.StrictMode>,
);

// 初始化应用
initApp();
