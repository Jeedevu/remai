import { StructuredExtractionResult } from './ai.schemas';

export interface AIProvider {
  extractFromText(text: string): Promise<StructuredExtractionResult>;
  extractFromImage(imageBuffer: Buffer, mimeType: string, voiceContext?: string): Promise<StructuredExtractionResult>;
  extractFromDocument(pdfBuffer: Buffer, fileName: string): Promise<StructuredExtractionResult>;
  chat(messages: Array<{ role: 'user' | 'assistant' | 'system'; content: string }>): Promise<string>;
}
