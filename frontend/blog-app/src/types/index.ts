// 基础类型定义

export interface Article {
  id: string;
  title: string;
  summary: string;
  content: string;
  author: {
    id: string;
    name: string;
    avatar: string;
  };
  coverImage?: string;
  category: string;
  tags: string[];
  viewCount: number;
  likeCount: number;
  commentCount: number;
  publishTime: string;
  updateTime: string;
}

export interface Category {
  id: string;
  name: string;
  count: number;
}

export interface Tag {
  id: string;
  name: string;
  count: number;
}

export interface User {
  id: string;
  name: string;
  avatar: string;
  email?: string;
  bio?: string;
  role: 'admin' | 'author' | 'reader';
}

export interface Comment {
  id: string;
  articleId: string;
  content: string;
  author: {
    id: string;
    name: string;
    avatar: string;
  };
  publishTime: string;
  parentId?: string;
  likes: number;
}

export interface AppProps {
  base: string;
}

export interface EventParams {
  [key: string]: unknown;
}