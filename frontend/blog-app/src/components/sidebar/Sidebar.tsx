import React, { useEffect } from 'react';
import { Input, List, Tag, Avatar } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useCategoryTagStore } from '../../store/categoryTagStore';
import TrackService from '../../utils/track';
import { CardContainer } from '../../styles/layout';

const { Search } = Input;

const Sidebar: React.FC = () => {
  const navigate = useNavigate();
  const { categories, tags, fetchCategories, fetchTags } = useCategoryTagStore();

  // 初始化时获取分类和标签数据
  useEffect(() => {
    fetchCategories();
    fetchTags();
  }, [fetchCategories, fetchTags]);

  // 处理搜索
  const handleSearch = (value: string) => {
    if (value.trim()) {
      navigate(`/blog/search?q=${encodeURIComponent(value)}`);
      TrackService.track('search', {
        query: value
      });
    }
  };

  // 处理分类点击
  const handleCategoryClick = (categoryId: string, categoryName: string) => {
    navigate(`/blog/category/${categoryId}`);
    TrackService.track('category_select', {
      category_id: categoryId,
      category_name: categoryName
    });
  };

  // 处理标签点击
  const handleTagClick = (tagId: string, tagName: string) => {
    navigate(`/blog/tag/${tagId}`);
    TrackService.track('tag_select', {
      tag_id: tagId,
      tag_name: tagName
    });
  };

  // 模拟用户信息
  const userInfo = {
    id: '1',
    name: '张三',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=zhangsan',
    bio: '热爱分享技术的前端开发者',
    articles: 128,
    followers: 5678
  };

  // 处理作者信息点击
  const handleAuthorClick = (authorId: string) => {
    navigate(`/blog/author/${authorId}`);
    TrackService.track('author_click', {
      author_id: authorId
    });
  };

  return (
    <>
      {/* 搜索框 */}
      <CardContainer style={{ marginBottom: '16px' }}>
        <Search
          placeholder="搜索文章"
          allowClear
          enterButton={<SearchOutlined />}
          size="large"
          onSearch={handleSearch}
        />
      </CardContainer>

      {/* 用户信息卡片 */}
      <CardContainer style={{ marginBottom: '16px', cursor: 'pointer' }} onClick={() => handleAuthorClick(userInfo.id)}>
        <div style={{ textAlign: 'center', padding: '24px 16px' }}>
          <Avatar 
            size={80} 
            src={userInfo.avatar} 
            style={{ margin: '0 auto 16px' }} 
          />
          <div style={{ fontSize: '18px', fontWeight: '600', marginBottom: '8px' }}>{userInfo.name}</div>
          <div style={{ color: '#8c8c8c', marginBottom: '16px' }}>{userInfo.bio}</div>
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0 24px' }}>
            <div>
              <div style={{ fontSize: '18px', fontWeight: '600' }}>{userInfo.articles}</div>
              <div style={{ fontSize: '12px', color: '#8c8c8c' }}>文章</div>
            </div>
            <div>
              <div style={{ fontSize: '18px', fontWeight: '600' }}>{userInfo.followers}</div>
              <div style={{ fontSize: '12px', color: '#8c8c8c' }}>粉丝</div>
            </div>
          </div>
        </div>
      </CardContainer>

      {/* 分类列表 */}
      <CardContainer style={{ marginBottom: '16px' }}>
        <div style={{ 
          fontSize: '16px', 
          fontWeight: '600', 
          marginBottom: '16px', 
          paddingBottom: '8px', 
          borderBottom: '1px solid #f0f0f0' 
        }}>分类</div>
        <List
          dataSource={categories}
          renderItem={(category) => (
            <List.Item 
              style={{ 
                cursor: 'pointer', 
                padding: '8px 0', 
                transition: 'all 0.3s'
              }}
              onClick={() => handleCategoryClick(category.id, category.name)}
            >
              <div>{category.name}</div>
              <div style={{ float: 'right', color: '#8c8c8c' }}>{category.count}</div>
            </List.Item>
          )}
          locale={{
            emptyText: '暂无分类',
          }}
        />
      </CardContainer>

      {/* 标签云 */}
      <CardContainer>
        <div style={{ 
          fontSize: '16px', 
          fontWeight: '600', 
          marginBottom: '16px', 
          paddingBottom: '8px', 
          borderBottom: '1px solid #f0f0f0' 
        }}>标签</div>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px' }}>
          {tags.map((tag) => (
            <Tag 
              key={tag.id} 
              style={{ 
                cursor: 'pointer', 
                transition: 'all 0.3s'
              }}
              onClick={() => handleTagClick(tag.id, tag.name)}
            >
              {tag.name} ({tag.count})
            </Tag>
          ))}
        </div>
      </CardContainer>
    </>
  );
};

export default Sidebar;