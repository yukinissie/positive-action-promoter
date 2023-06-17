import type { AWS } from '@serverless/typescript';

import assistant from '@functions/assistant';

import { config } from 'dotenv';

config();

const serverlessConfiguration: AWS = {
  service: 'positive-action-promoter',
  frameworkVersion: '3',
  plugins: ['serverless-esbuild'],
  provider: {
    name: 'aws',
    runtime: 'nodejs18.x',
    apiGateway: {
      minimumCompressionSize: 1024,
      shouldStartNameWithService: true,
    },
    environment: {
      AWS_NODEJS_CONNECTION_REUSE_ENABLED: '1',
      NODE_OPTIONS: '--enable-source-maps --stack-trace-limit=1000',
      OPENAI_ORG: process.env.OPENAI_ORG,
      OPENAI_API_KEY: process.env.OPENAI_API_KEY,
    },
    stage: 'dev',
    region: 'ap-northeast-1',
  },
  // import the function via paths
  functions: { assistant },
  package: { individually: true },
  custom: {
    esbuild: {
      bundle: true,
      minify: false,
      sourcemap: true,
      exclude: ['aws-sdk'],
      target: 'node18',
      define: { 'require.resolve': undefined },
      platform: 'node',
      concurrency: 10,
    },
  },
};

module.exports = serverlessConfiguration;
