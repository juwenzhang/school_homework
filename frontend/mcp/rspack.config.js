import { defineConfig } from '@rspack/core';
import * as path from 'path';

const isProduction = process.env.NODE_ENV === 'production';

export default defineConfig({
  entry: {
    main: path.resolve(__dirname, 'src/main.tsx'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: isProduction ? '[name].[contenthash].js' : '[name].js',
    chunkFilename: isProduction ? '[name].[contenthash].chunk.js' : '[name].chunk.js',
    publicPath: '/',
  },
  resolve: {
    extensions: ['.ts', '.tsx', '.js', '.jsx', '.json'],
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        use: [
          {
            loader: 'swc-loader',
            options: {
              jsc: {
                parser: {
                  syntax: 'typescript',
                  tsx: true,
                },
                transform: {
                  react: {
                    runtime: 'automatic',
                  },
                },
              },
            },
          },
        ],
        exclude: /node_modules/,
      },
      {
        test: /\.(js|jsx)$/,
        use: [
          {
            loader: 'swc-loader',
            options: {
              jsc: {
                parser: {
                  syntax: 'ecmascript',
                  jsx: true,
                },
                transform: {
                  react: {
                    runtime: 'automatic',
                  },
                },
              },
            },
          },
        ],
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
      {
        test: /\.less$/,
        use: ['style-loader', 'css-loader', 'less-loader'],
      },
      {
        test: /\.(png|jpe?g|gif|svg)$/i,
        type: 'asset',
        generator: {
          filename: 'assets/[name].[hash][ext]',
        },
      },
    ],
  },
  devServer: {
    port: 5173,
    historyApiFallback: true,
    hot: true,
    open: true,
  },
  plugins: [],
  mode: isProduction ? 'production' : 'development',
});