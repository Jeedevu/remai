import { Router, Request, Response } from 'express';
import multer from 'multer';
import { aiService } from '../../ai/ai.service';
import { config } from '../../config/env';

const upload = multer({
  limits: {
    fileSize: config.limits.maxPdfSizeMb * 1024 * 1024
  },
  storage: multer.memoryStorage()
});

export const analyzeRouter = Router();

// Text analysis
analyzeRouter.post('/text', async (req: Request, res: Response) => {
  try {
    const { text } = req.body;
    if (!text || typeof text !== 'string') {
      return res.status(400).json({ success: false, error: { message: 'Text field is required' } });
    }

    const provider = aiService.getProvider();
    const result = await provider.extractFromText(text);

    return res.json({
      success: true,
      data: result,
      error: null
    });
  } catch (error: any) {
    console.error('Analyze text error:', error.message);
    return res.status(500).json({
      success: false,
      data: null,
      error: { code: 'AI_PROCESSING_FAILED', message: error.message || 'Unable to process text' }
    });
  }
});

// Image / Screenshot multimodal analysis
analyzeRouter.post('/image', upload.single('image'), async (req: Request, res: Response) => {
  try {
    if (!req.file) {
      return res.status(400).json({ success: false, error: { message: 'Image file is required' } });
    }

    const voiceContext = req.body.voiceContext as string | undefined;
    const provider = aiService.getProvider();
    const result = await provider.extractFromImage(req.file.buffer, req.file.mimetype, voiceContext);

    return res.json({
      success: true,
      data: result,
      error: null
    });
  } catch (error: any) {
    console.error('Analyze image error:', error.message);
    return res.status(500).json({
      success: false,
      data: null,
      error: { code: 'AI_PROCESSING_FAILED', message: error.message || 'Unable to process image' }
    });
  }
});

// Document / PDF analysis
analyzeRouter.post('/document', upload.single('document'), async (req: Request, res: Response) => {
  try {
    if (!req.file) {
      return res.status(400).json({ success: false, error: { message: 'Document file is required' } });
    }

    const provider = aiService.getProvider();
    const result = await provider.extractFromDocument(req.file.buffer, req.file.originalname);

    return res.json({
      success: true,
      data: result,
      error: null
    });
  } catch (error: any) {
    console.error('Analyze document error:', error.message);
    return res.status(500).json({
      success: false,
      data: null,
      error: { code: 'AI_PROCESSING_FAILED', message: error.message || 'Unable to process document' }
    });
  }
});
