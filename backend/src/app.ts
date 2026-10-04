import express from 'express';
import cors from 'cors';
import { analyzeRouter } from './modules/analyze/analyze.controller';
import { chatRouter } from './modules/chat/chat.controller';
import { config } from './config/env';

export const app = express();

app.use(cors({ origin: config.corsOrigin }));
app.use(express.json({ limit: '20mb' }));
app.use(express.urlencoded({ extended: true, limit: '20mb' }));

// Health check
app.get('/health', (req, res) => {
  res.json({
    status: 'ok',
    service: 'REM AI Gateway',
    provider: config.aiProvider,
    timestamp: new Date().toISOString()
  });
});

// AI Endpoints
app.use('/api/ai/analyze', analyzeRouter);
app.use('/api/ai/chat', chatRouter);
