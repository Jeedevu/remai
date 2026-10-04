import { AIProvider } from './ai.provider';
import { NvidiaNimProvider } from './nvidia.provider';
import { MockAIProvider } from './mock.provider';
import { config } from '../config/env';

export class AIService {
  private provider: AIProvider;

  constructor() {
    if (config.aiProvider === 'nvidia' && config.nvidia.apiKey) {
      console.log('Using NvidiaNimProvider with model:', config.nvidia.multimodalModel);
      this.provider = new NvidiaNimProvider();
    } else {
      console.log('Using MockAIProvider (No NVIDIA_API_KEY found or AI_PROVIDER=mock)');
      this.provider = new MockAIProvider();
    }
  }

  getProvider(): AIProvider {
    return this.provider;
  }
}

export const aiService = new AIService();
