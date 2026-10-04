import dotenv from 'dotenv';
dotenv.config();

export const config = {
  port: parseInt(process.env.PORT || '3000', 10),
  nodeEnv: process.env.NODE_ENV || 'development',
  corsOrigin: process.env.CORS_ORIGIN || '*',
  aiProvider: process.env.AI_PROVIDER || 'nvidia',
  nvidia: {
    apiKey: process.env.NVIDIA_API_KEY || '',
    baseUrl: process.env.NVIDIA_BASE_URL || 'https://integrate.api.nvidia.com/v1',
    multimodalModel: process.env.NVIDIA_MODEL || 'meta/llama-3.2-11b-vision-instruct',
    textModel: process.env.NVIDIA_TEXT_MODEL || 'meta/llama-3.1-70b-instruct'
  },
  limits: {
    maxImageSizeMb: parseInt(process.env.MAX_IMAGE_SIZE_MB || '15', 10),
    maxPdfSizeMb: parseInt(process.env.MAX_PDF_SIZE_MB || '25', 10),
    maxAudioSizeMb: parseInt(process.env.MAX_AUDIO_SIZE_MB || '20', 10)
  }
};
