import { jsx, css } from '@emotion/react';

const globalStyles = {
  '&': css`
  /* 重置浏览器默认样式 */
  * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
  }

  body {
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB',
      'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif, 'Apple Color Emoji',
      'Segoe UI Emoji', 'Segoe UI Symbol';
    font-size: 14px;
    line-height: 1.6;
    color: #333;
    background-color: #f5f5f5;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  /* 重置列表样式 */
  ul, ol {
    list-style: none;
  }

  /* 重置链接样式 */
  a {
    color: #1890ff;
    text-decoration: none;
    transition: color 0.3s;
  }

  a:hover {
    color: #40a9ff;
    text-decoration: underline;
  }

  /* 重置图片样式 */
  img {
    max-width: 100%;
    height: auto;
    vertical-align: middle;
  }

  /* 重置按钮样式 */
  button {
    cursor: pointer;
    border: none;
    background: transparent;
    outline: none;
    font-family: inherit;
  }

  /* 重置输入框样式 */
  input, textarea, select {
    font-family: inherit;
    font-size: inherit;
    outline: none;
  }

  /* 清除浮动 */
  .clearfix::after {
    content: '';
    display: table;
    clear: both;
  }

  /* 文本省略 */
  .text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  /* 多行文本省略 */
  .text-ellipsis-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .text-ellipsis-3 {
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  /* 滚动条样式 */
  ::-webkit-scrollbar {
    width: 8px;
    height: 8px;
  }

  ::-webkit-scrollbar-track {
    background: #f1f1f1;
  }

  ::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 4px;
  }

  ::-webkit-scrollbar-thumb:hover {
    background: #555;
  }

  /* 搜索高亮 */
  mark.search-highlight {
    background-color: #fff76a;
    padding: 0 2px;
    border-radius: 2px;
    color: inherit;
  }

  /* 动画效果 */
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  @keyframes slideUp {
    from {
      transform: translateY(20px);
      opacity: 0;
    }
    to {
      transform: translateY(0);
      opacity: 1;
    }
  }

  .fade-in {
    animation: fadeIn 0.5s ease-in-out;
  }

  .slide-up {
    animation: slideUp 0.5s ease-out;
  }

  /* 响应式断点 */
  @media (max-width: 768px) {
    body {
      font-size: 13px;
    }
  }
`
};

const GlobalStyle = () => jsx('style', { jsx: true, css: globalStyles });

export default GlobalStyle;