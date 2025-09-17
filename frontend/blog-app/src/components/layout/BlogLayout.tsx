import React from 'react';
import { Outlet } from 'react-router-dom';
import { Typography, BackTop } from 'antd';
import styled from '@emotion/styled';
import TrackService from '../../utils/track';
import { MainLayout, HeaderContainer, ContentContainer, FooterContainer } from '../../styles/layout';

const { Title } = Typography;

const BlogTitle = styled(Title)`
  margin: 0;
  line-height: 64px;
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
`;

const BlogLayout: React.FC = () => {
  const handleScrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    // 埋点：记录返回顶部操作
    TrackService.track('back_to_top');
  };

  const getCurrentYear = () => {
    return new Date().getFullYear();
  };

  return (
    <MainLayout>
      <HeaderContainer>
        <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '0 24px' }}>
          <BlogTitle level={3}>
            个人博客
          </BlogTitle>
        </div>
      </HeaderContainer>
      
      <ContentContainer>
        <Outlet />
      </ContentContainer>
      
      <FooterContainer>
        © {getCurrentYear()}-year JUWENZHANG @bytedance @pdd @ baidu
      </FooterContainer>
      
      <BackTop visibilityHeight={300} onClick={handleScrollToTop} />
    </MainLayout>
  );
};

export default BlogLayout;