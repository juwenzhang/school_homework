import React, { useEffect } from 'react';
import { List, Typography } from 'antd';
import { TwoColumnLayout, MainContent, SidebarContent } from '../styles/layout';
import { useArticleStore } from '../store/articleStore';
import ArticleCard from '../components/article/ArticleCard';
import Sidebar from '../components/sidebar/Sidebar';
import TrackService from '../utils/track';

const { Title } = Typography;

const BlogHome: React.FC = () => {
  const {
    articles,
    totalArticles,
    loading,
    error,
    currentPage,
    pageSize,
    fetchArticles
  } = useArticleStore();

  // 初始化时获取文章列表
  useEffect(() => {
    fetchArticles();
    TrackService.track('home_page_view');
  }, [fetchArticles]);

  // 处理页面变化
  const handlePageChange = (page: number) => {
    fetchArticles(page);
    TrackService.trackPageChange(page, pageSize);
  };

  return (
    <>
      <div style={{ marginBottom: 24 }}>
        <Title level={2}>最新文章</Title>
      </div>

      <TwoColumnLayout>
        <MainContent>
          <List
            grid={{ gutter: 16, column: 1 }}
            dataSource={articles}
            renderItem={(article) => (
              <List.Item>
                <ArticleCard article={article} />
              </List.Item>
            )}
            pagination={{
              current: currentPage,
              pageSize: pageSize,
              total: totalArticles,
              onChange: handlePageChange,
              showSizeChanger: false,
              showQuickJumper: true,
              showTotal: (total) => `共 ${total} 篇文章`,
            }}
            loading={loading}
            locale={{
              emptyText: error || '暂无文章',
            }}
          />
        </MainContent>

        <SidebarContent>
          <Sidebar />
        </SidebarContent>
      </TwoColumnLayout>
    </>
  );
};

export default BlogHome;