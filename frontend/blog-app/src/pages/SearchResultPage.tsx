import React, { useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { List, Typography, Empty, Input } from 'antd';
import styled from '@emotion/styled';
import { useArticleStore } from '../store/articleStore';
import ArticleCard from '../components/article/ArticleCard';
import TrackService from '../utils/track';

const { Title } = Typography;
const { Search } = Input;

const SearchResultPageContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;

  .search-header {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;
  }

  .search-query {
    color: #1890ff;
  }

  .search-result-stats {
    margin-bottom: 24px;
    color: #666;
  }

  .search-result-list {
    max-width: 800px;
  }

  .empty-result {
    padding: 48px 0;
    text-align: center;
  }

  .try-another-search {
    margin-top: 24px;
  }
`;

const SearchResultPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('q') || '';
  const {
    articles,
    totalArticles,
    loading,
    error,
    currentPage,
    pageSize,
    fetchArticles,
    resetFilters
  } = useArticleStore();

  // 当搜索参数变化时，执行搜索
  useEffect(() => {
    if (query) {
      resetFilters();
      fetchArticles(1, 10, query);
      TrackService.track('search', {
        query: query
      });
    }
  }, [query, fetchArticles, resetFilters]);

  // 处理页面变化
  const handlePageChange = (page: number) => {
    fetchArticles(page);
    TrackService.trackPageChange(page, pageSize);
  };



  // 处理再次搜索
  const handleSearch = (value: string) => {
    if (value.trim()) {
      resetFilters();
      fetchArticles(1, 10, value);
      TrackService.track('search', {
        query: value
      });
    }
  };



  return (
    <SearchResultPageContainer>
      <div className="search-header">
        <Title level={2}>
          搜索结果: <span className="search-query">{query}</span>
        </Title>
      </div>

      {query ? (
        <>
          <div className="search-result-stats">
            {loading ? '搜索中...' : `找到 ${totalArticles} 条结果`}
          </div>

          <div className="search-result-list">
            {!loading && articles.length === 0 && (
              <div className="empty-result">
                <Empty description="未找到相关文章" />
                <div className="try-another-search">
                  <Search
                    placeholder="尝试其他关键词"
                    allowClear
                    enterButton="搜索"
                    size="large"
                    onSearch={handleSearch}
                    defaultValue={query}
                  />
                </div>
              </div>
            )}

            {articles.length > 0 && (
              <List
                grid={{ gutter: 16, column: 1 }}
                dataSource={articles}
                renderItem={(article) => (
                  <List.Item>
                    <ArticleCard 
                      article={article} 
                      highlightKeyword={query}
                    />
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
            )}
          </div>
        </>
      ) : (
        <div className="empty-search">
          <Empty description="请输入搜索关键词" />
          <div style={{ marginTop: 24 }}>
            <Search
              placeholder="搜索文章"
              allowClear
              enterButton="搜索"
              size="large"
              onSearch={handleSearch}
            />
          </div>
        </div>
      )}
    </SearchResultPageContainer>
  );
};

export default SearchResultPage;