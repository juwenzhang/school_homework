import styled from '@emotion/styled';
import { theme } from './theme';

// 主布局容器
export const MainLayout = styled.div`
  min-height: 100vh;
  display: flex;
  flex-direction: column;
`;

// 头部容器
export const HeaderContainer = styled.header`
  background-color: ${theme.colors.backgroundWhite};
  box-shadow: ${theme.shadows.sm};
  position: sticky;
  top: 0;
  z-index: 1000;
`;

// 内容容器
export const ContentContainer = styled.main`
  flex: 1;
  max-width: ${theme.containerWidths.xl};
  width: 100%;
  margin: 0 auto;
  padding: ${theme.spacing.lg} ${theme.spacing.md};
`;

// 页脚容器
export const FooterContainer = styled.footer`
  background-color: ${theme.colors.backgroundWhite};
  border-top: 1px solid ${theme.colors.borderLight};
  padding: ${theme.spacing.lg} ${theme.spacing.md};
  text-align: center;
  color: ${theme.colors.textTertiary};
`;

// 两列布局
export const TwoColumnLayout = styled.div`
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: ${theme.spacing.xl};
  align-items: start;

  @media (max-width: ${theme.breakpoints.md}) {
    grid-template-columns: 1fr;
    gap: ${theme.spacing.lg};
  }
`;

// 主内容区域
export const MainContent = styled.div`
  width: 100%;
`;

// 侧边栏区域
export const SidebarContent = styled.div`
  width: 100%;
  position: sticky;
  top: ${theme.spacing.xxxl};

  @media (max-width: ${theme.breakpoints.md}) {
    position: static;
  }
`;

// 卡片容器
export const CardContainer = styled.div`
  background-color: ${theme.colors.backgroundWhite};
  border-radius: ${theme.radius.lg};
  padding: ${theme.spacing.lg};
  box-shadow: ${theme.shadows.md};
  margin-bottom: ${theme.spacing.lg};
  transition: all ${theme.animations.duration.normal} ${theme.animations.timingFunction.easeInOut};

  &:hover {
    box-shadow: ${theme.shadows.lg};
  }
`;

// 页面标题容器
export const PageTitleContainer = styled.div`
  margin-bottom: ${theme.spacing.lg};
  padding-bottom: ${theme.spacing.md};
  border-bottom: 1px solid ${theme.colors.borderLight};
`;

// 加载状态容器
export const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
`;

// 错误状态容器
export const ErrorContainer = styled.div`
  text-align: center;
  padding: ${theme.spacing.xxl};
  color: ${theme.colors.textSecondary};
`;

// 空状态容器
export const EmptyContainer = styled.div`
  text-align: center;
  padding: ${theme.spacing.xxl};
  color: ${theme.colors.textSecondary};
`;

// 返回顶部按钮
export const BackToTopButton = styled.button`
  position: fixed;
  bottom: ${theme.spacing.xl};
  right: ${theme.spacing.xl};
  width: 40px;
  height: 40px;
  border-radius: ${theme.radius.full};
  background-color: ${theme.colors.primary};
  color: ${theme.colors.backgroundWhite};
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: ${theme.shadows.md};
  transition: all ${theme.animations.duration.normal} ${theme.animations.timingFunction.easeInOut};
  z-index: 999;

  &:hover {
    background-color: ${theme.colors.primaryHover};
    transform: translateY(-2px);
    box-shadow: ${theme.shadows.lg};
  }

  &:active {
    background-color: ${theme.colors.primaryActive};
  }

  @media (max-width: ${theme.breakpoints.md}) {
    bottom: ${theme.spacing.lg};
    right: ${theme.spacing.lg};
  }
`;