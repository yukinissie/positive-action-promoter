import schema from './schema';
import { handlerPath } from '@libs/handler-resolver';

export default {
  handler: `${handlerPath(__dirname)}/handler.main`,
  events: [
    {
      http: {
        cors: true,
        method: 'post',
        path: 'assistant',
        request: {
          schemas: {
            'application/json': schema,
          },
        },
      },
    },
  ],
  timeout: 30,
};
