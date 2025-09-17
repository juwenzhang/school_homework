import React, { useState, useEffect } from 'react';
import { Routes, Route, Link, useNavigate } from 'react-router-dom';
import { 
  ConfigProvider, 
  Layout, 
  Menu, 
  Avatar, 
  Button, 
  Space, 
  Badge 
} from 'antd';
import { 
  MenuFoldOutlined, 
  MenuUnfoldOutlined, 
  HomeOutlined, 
  BarChartOutlined, 
  UserOutlined, 
  LoginOutlined, 
  LogoutOutlined, 
  MessageOutlined, 
  SettingOutlined, 
  SearchOutlined, 
  BellOutlined, 
  FileTextOutlined, 
  CodeOutlined 
} from '@ant-design/icons';
import type { MenuProps } from 'antd';
import type { Garfish } from 'garfish';
import { styled } from '@emotion/styled';
import './App.css';

const { Header, Sider, Content } = Layout;

interface AppProps {
  garfish: Garfish;
}

// 定义用户类型
interface User {
  id: string;
  username: string;
  avatar: string;
  roles: string[];
}

// 定义菜单项类型
const menuItems: MenuProps['items'] = [
  {
    key: '1',
    icon: <HomeOutlined />,
    label: <Link to="/">首页</Link>,
  },
  {
    key: '2',
    icon: <MessageOutlined />,
    label: <Link to="/blog">博客平台</Link>,
  },
  {
    key: '3',
    icon: <BarChartOutlined />,
    label: <Link to="/monitor">监控系统</Link>,
  },
  {
    key: '4',
    icon: <FileTextOutlined />,
    label: <Link to="/admin">管理平台</Link>,
  },
];

const UserMenu: MenuProps['items'] = [
  {
    key: 'profile',
    icon: <UserOutlined />,
    label: '个人中心',
  },
  {
    key: 'settings',
    icon: <SettingOutlined />,
    label: '设置',
  },
  {
    key: 'logout',
    icon: <LogoutOutlined />,
    label: '退出登录',
  },
];

// 埋点服务
const trackEvent = (eventName: string, eventParams?: Record<string, any>): void => {
  console.log('Track event:', eventName, eventParams);
  // 这里会在实际环境中发送到埋点服务器
  try {
    if (window && (window as any).analytics) {
      (window as any).analytics.track(eventName, eventParams);
    }
  } catch (error) {
    console.error('Error tracking event:', error);
  }
};

// 主应用布局样式
const MainLayout = styled.div`
  min-height: 100vh;
  display: flex;
  flex-direction: column;
`;

const App: React.FC<AppProps> = ({ garfish }) => {
  const [collapsed, setCollapsed] = useState(false);
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const navigate = useNavigate();

  // 模拟用户登录状态
  useEffect(() => {
    // 实际环境中会从localStorage或API获取用户信息
    const mockUser: User = {
      id: '1',
      username: 'admin',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
      roles: ['admin', 'user'],
    };
    setCurrentUser(mockUser);

    // 埋点：页面访问
    trackEvent('page_view', {
      page: window.location.pathname,
      timestamp: new Date().toISOString(),
    });
  }, []);

  const toggle = () => {
    setCollapsed(!collapsed);
    trackEvent('menu_toggle', { collapsed: !collapsed });
  };

  const handleMenuClick = (e: { key: string }) => {
    trackEvent('menu_click', { menu_key: e.key });
  };

  const handleLogout = () => {
    setCurrentUser(null);
    navigate('/login');
    trackEvent('user_logout', { user_id: currentUser?.id });
  };

  return (
    <ConfigProvider>
      <MainLayout>
        <Layout>
          <Header className="header">
            <div className="header-left">
              <Button
                type="text"
                icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
                onClick={toggle}
                className="trigger"
                size="large"
              />
              <div className="logo">
                <CodeOutlined className="logo-icon" />
                <span className="logo-text">Blog Platform</span>
              </div>
            </div>
            <div className="header-right">
              <Space size="middle">
                <Button
                  type="text"
                  icon={<SearchOutlined />}
                  size="large"
                  className="header-btn"
                />
                <Badge count={5} showZero>
                  <Button
                    type="text"
                    icon={<BellOutlined />}
                    size="large"
                    className="header-btn"
                  />
                </Badge>
                {currentUser ? (
                  <div className="user-menu">
                    <Space wrap>
                      <Avatar src={currentUser.avatar} />
                      <span>{currentUser.username}</span>
                    </Space>
                  </div>
                ) : (
                  <Button
                    type="primary"
                    icon={<LoginOutlined />}
                    onClick={() => navigate('/login')}
                  >
                    登录
                  </Button>
                )}
              </Space>
            </div>
          </Header>
          <Layout>
            <Sider
              width={200}
              theme="light"
              trigger={null}
              collapsible
              collapsed={collapsed}
              className="sidebar"
            >
              <Menu
                mode="inline"
                items={menuItems}
                onClick={handleMenuClick}
                className="menu"
              />
            </Sider>
            <Layout>
              <Content className="content">
                <Routes>
                  <Route
                    path="/"
                    element={
                      <div className="home-page">
                        <h1>欢迎使用博客系统</h1>
                        <p>这是一个基于微前端架构的博客系统，支持多用户、埋点系统和监控系统。</p>
                        <div className="features">
                          <div className="feature-card">
                            <h3>博客平台</h3>
                            <p>支持多用户发布、编辑、管理博客文章</p>
                          </div>
                          <div className="feature-card">
                            <h3>监控系统</h3>
                            <p>实时监控系统性能、访问量和用户行为</p>
                          </div>
                          <div className="feature-card">
                            <h3>管理平台</h3>
                            <p>统一管理用户、文章、评论和系统设置</p>
                          </div>
                        </div>
                      </div>
                    }
                  />
                  <Route
                    path="/login"
                    element={
                      <div className="login-page">
                        <h1>登录页面</h1>
                        <p>登录后即可访问系统功能</p>
                      </div>
                    }
                  />
                </Routes>
                {/* 子应用容器 */}
                <div id="subapp-container" className="subapp-container"></div>
              </Content>
            </Layout>
          </Layout>
        </Layout>
      </MainLayout>
    </ConfigProvider>
  );
};

export default App;
