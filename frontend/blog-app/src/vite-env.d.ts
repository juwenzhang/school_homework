/// <reference types="vite/client" />

// 声明CSS模块类型
declare module '*.css' {
  const classes: { [key: string]: string };
  export default classes;
}