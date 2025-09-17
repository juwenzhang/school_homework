// 主题配置文件

// 颜色配置
export const colors = {
  primary: '#1890ff',
  primaryHover: '#40a9ff',
  primaryActive: '#096dd9',
  success: '#52c41a',
  warning: '#faad14',
  error: '#f5222d',
  info: '#1890ff',
  textPrimary: '#262626',
  textSecondary: '#595959',
  textTertiary: '#8c8c8c',
  textQuaternary: '#bfbfbf',
  border: '#d9d9d9',
  borderLight: '#f0f0f0',
  background: '#f5f5f5',
  backgroundWhite: '#ffffff',
  backgroundGray: '#fafafa',
  highlight: '#fff76a',
};

// 字体配置
export const fonts = {
  family: `-apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial, sans-serif, "Apple Color Emoji",
    "Segoe UI Emoji", "Segoe UI Symbol"`,

  sizes: {
    xs: '12px',
    sm: '14px',
    md: '16px',
    lg: '18px',
    xl: '24px',
    xxl: '32px',
    xxxl: '48px',
  },
  weights: {
    normal: '400',
    medium: '500',
    semiBold: '600',
    bold: '700',
  },
  lineHeights: {
    small: '1.2',
    normal: '1.5',
    large: '1.8',
  },
};

// 间距配置
export const spacing = {
  xs: '4px',
  sm: '8px',
  md: '16px',
  lg: '24px',
  xl: '32px',
  xxl: '48px',
  xxxl: '64px',
};

// 圆角配置
export const radius = {
  sm: '2px',
  md: '4px',
  lg: '8px',
  xl: '16px',
  full: '50%',
};

// 阴影配置
export const shadows = {
  none: 'none',
  sm: '0 2px 4px rgba(0, 0, 0, 0.05)',
  md: '0 2px 8px rgba(0, 0, 0, 0.06)',
  lg: '0 4px 12px rgba(0, 0, 0, 0.1)',
  xl: '0 8px 24px rgba(0, 0, 0, 0.12)',
};

// 动画配置
export const animations = {
  duration: {
    fast: '0.15s',
    normal: '0.3s',
    slow: '0.5s',
  },
  timingFunction: {
    easeInOut: 'cubic-bezier(0.4, 0, 0.2, 1)',
    easeOut: 'cubic-bezier(0, 0, 0.2, 1)',
    easeIn: 'cubic-bezier(0.4, 0, 1, 1)',
  },
};

// 响应式断点
export const breakpoints = {
  xs: '360px',
  sm: '480px',
  md: '768px',
  lg: '1024px',
  xl: '1200px',
  xxl: '1600px',
};

// 容器宽度
export const containerWidths = {
  sm: '480px',
  md: '720px',
  lg: '960px',
  xl: '1140px',
  xxl: '1520px',
};

// 主主题对象
export const theme = {
  colors,
  fonts,
  spacing,
  radius,
  shadows,
  animations,
  breakpoints,
  containerWidths,
};

export default theme;