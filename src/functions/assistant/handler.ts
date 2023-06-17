import type { ValidatedEventAPIGatewayProxyEvent } from '@libs/api-gateway';
import { formatJSONResponse } from '@libs/api-gateway';
import { middyfy } from '@libs/lambda';
import { Configuration, OpenAIApi } from 'openai';

const configuration = new Configuration({
  organization: process.env.OPENAI_ORG,
  apiKey: process.env.OPENAI_API_KEY,
});
const openai = new OpenAIApi(configuration);

import schema from './schema';

const assistant: ValidatedEventAPIGatewayProxyEvent<typeof schema> = async (
  event
) => {
  const res = await openai.createChatCompletion({
    model: 'gpt-3.5-turbo',
    temperature: 0.5,
    messages: [
      {
        role: 'system',
        content: `「ポジティブな行動促進クラス」の実装:

        クラス名: PositiveActionPromoter
        
        プロパティ:
        negative_emotion: ネガティブな感情を表す変数
        
        メソッド:
        process_input(): 入力を受け取り、ネガティブな感情を設定するメソッド
        promote_positive_action(): ネガティブな感情をポジティブに転換し、前向きな行動を促すメソッド
        
        process_input()メソッドの手順:
        入力を受け取る
        ネガティブな感情の変数に入力を設定する
        
        promote_positive_action()メソッドの手順:
        ネガティブな感情の変数を分析する
        ポジティブな視点を見つけるために、ネガティブな感情を反転する
        反転した感情に基づいて、具体的な前向きな行動を推奨するメッセージを140文字以内で生成する。その際に適切なハッシュタグをつける。
        生成したメッセージを出力する
        
        このように、PositiveActionPromoterクラスは、入力としてネガティブな感情を受け取り、それをポジティブな視点に転換して前向きな行動を促す役割を持つ抽象クラスです。具体的な実装や具体的な自然言語の処理方法は省略されていますが、この抽象クラスを実際のプログラミング言語で具体化することで、具体的な動作や出力を得ることができます。
        
        さて、あなたはPositiveActionPromoterクラスです。negative_emotionプロパティを渡すので、余計な言葉は省いて結果だけ出力してください。
        
        まずユーザーがメッセージを送るので、それをnegative_emotionに渡して、PositiveActionPromoterの結果を返してください。`,
      },
      {
        role: 'user',
        content: `negative_emotion=${event.body.message}`,
      },
    ],
  });
  const result = res.data.choices[0].message.content;
  return formatJSONResponse({
    message: result,
  });
};

export const main = middyfy(assistant);
