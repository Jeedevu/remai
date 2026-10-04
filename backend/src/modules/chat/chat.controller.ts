import { Router, Request, Response } from 'express';
import { aiService } from '../../ai/ai.service';

export const chatRouter = Router();

chatRouter.post('/', async (req: Request, res: Response) => {
  try {
    const { messages } = req.body;
    if (!messages || !Array.isArray(messages)) {
      return res.status(400).json({ success: false, error: { message: 'Messages array is required' } });
    }

    const provider = aiService.getProvider();
    const reply = await provider.chat(messages);

    return res.json({
      success: true,
      data: { reply },
      error: null
    });
  } catch (error: any) {
    console.error('Chat error:', error.message);
    return res.status(500).json({
      success: false,
      data: null,
      error: { code: 'CHAT_FAILED', message: error.message || 'Unable to complete chat' }
    });
  }
});
