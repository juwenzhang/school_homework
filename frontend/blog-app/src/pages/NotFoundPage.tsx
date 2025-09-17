import React, { useEffect } from 'react';
import { Button, Typography, Result } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import styled from '@emotion/styled';
import TrackService from '../utils/track';

const { Paragraph } = Typography;

const NotFoundContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  padding: 40px 20px;
  text-align: center;

  .not-found-content {
    max-width: 600px;
  }

  .not-found-image {
    max-width: 100%;
    height: auto;
    margin-bottom: 32px;
  }

  .not-found-title {
    font-size: 72px;
    font-weight: 700;
    color: #f5222d;
    margin-bottom: 24px;
  }

  .not-found-description {
    font-size: 18px;
    color: #666;
    margin-bottom: 32px;
  }

  .back-button {
    font-size: 16px;
    padding: 8px 16px;
  }
`;

const NotFoundPage: React.FC = () => {
  const navigate = useNavigate();

  // 埋点：记录404页面访问
  useEffect(() => {
    TrackService.track('page_not_found', {
      url: window.location.pathname
    });
  }, []);

  const handleBackToHome = () => {
    navigate('/');
    TrackService.track('back_to_home_from_404');
  };

  return (
    <NotFoundContainer>
      <div className="not-found-content">
        <Result
          status="404"
          title="404"
          subTitle="抱歉，您访问的页面不存在"
          extra={
            <Button 
              type="primary" 
              icon={<ArrowLeftOutlined />}
              onClick={handleBackToHome}
              className="back-button"
            >
              返回首页
            </Button>
          }
        />
        
        <Paragraph className="not-found-description">
          可能的原因：<br />
          • 该页面已被删除<br />
          • URL地址输入错误<br />
          • 文章已被移动到其他位置
        </Paragraph>
      </div>
    </NotFoundContainer>
  );
};

export default NotFoundPage;