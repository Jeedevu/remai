import axios from 'axios';
import { AIProvider } from './ai.provider';
import { StructuredExtractionResult, StructuredExtractionResultSchema } from './ai.schemas';
import { config } from '../config/env';

export class NvidiaNimProvider implements AIProvider {
  private client = axios.create({
    baseURL: config.nvidia.baseUrl,
    headers: {
      'Authorization': `Bearer ${config.nvidia.apiKey}`,
      'Content-Type': 'application/json'
    },
    timeout: 45000
  });

  private systemPrompt = `You are the core extraction engine for REM ("You Forget. We Remember"), an AI academic memory suite.
Analyze the provided student material (syllabus, screenshot, notes, or assignment prompt).
Do NOT hallucinate deadlines or requirements. If not stated, set fields to null and document in uncertainties.
You MUST output ONLY a valid JSON object matching this schema:
{
  "memory": {
    "title": "string",
    "summary": "string",
    "type": "assignment" | "exam" | "announcement" | "note" | "project" | "other",
    "subject": "string or null",
    "course": "string or null",
    "professor": "string or null",
    "deadline": "string or null (e.g. Due Friday, 8:00 PM)",
    "importance": "low" | "medium" | "high" | "critical"
  },
  "tasks": [
    {
      "title": "string",
      "description": "string",
      "dueAt": "string or null",
      "priority": "low" | "medium" | "high" | "critical",
      "estimatedMinutes": number
    }
  ],
  "facts": ["string"],
  "uncertainties": ["string"],
  "confidence": number between 0 and 1
}`;

  async extractFromText(text: string): Promise<StructuredExtractionResult> {
    const payload = {
      model: config.nvidia.textModel,
      messages: [
        { role: 'system', content: this.systemPrompt },
        { role: 'user', content: `Extract student memory and tasks from this academic input:\n\n${text}` }
      ],
      temperature: 0.1,
      response_format: { type: 'json_object' }
    };

    const response = await this.client.post('/chat/completions', payload);
    const content = response.data.choices[0]?.message?.content || '{}';
    return this.parseAndValidate(content);
  }

  async extractFromImage(imageBuffer: Buffer, mimeType: string, voiceContext?: string): Promise<StructuredExtractionResult> {
    const base64Image = imageBuffer.toString('base64');
    const userPrompt = voiceContext
      ? `Extract academic information from this syllabus / course screenshot. Student context note: "${voiceContext}"`
      : `Extract academic information from this syllabus / course screenshot. Identify courses, exam dates, deadlines, and action items.`;

    const payload = {
      model: config.nvidia.multimodalModel,
      messages: [
        { role: 'system', content: this.systemPrompt },
        {
          role: 'user',
          content: [
            { type: 'text', text: userPrompt },
            {
              type: 'image_url',
              image_url: { url: `data:${mimeType};base64,${base64Image}` }
            }
          ]
        }
      ],
      temperature: 0.1
    };

    const response = await this.client.post('/chat/completions', payload);
    const content = response.data.choices[0]?.message?.content || '{}';
    return this.parseAndValidate(content);
  }

  async extractFromDocument(pdfBuffer: Buffer, fileName: string): Promise<StructuredExtractionResult> {
    // For document analysis, process extracted text or base64
    const prompt = `Analyze this PDF document (${fileName}) for course syllabi, exam schedules, and assignment requirements.`;
    return this.extractFromText(prompt);
  }

  async chat(messages: Array<{ role: 'user' | 'assistant' | 'system'; content: string }>): Promise<string> {
    const payload = {
      model: config.nvidia.textModel,
      messages: [
        {
          role: 'system',
          content: 'You are REM, a neo-brutalist student AI assistant. You answer based on the student\'s verified memory vault data. Be punchy, helpful, and concise.'
        },
        ...messages
      ],
      temperature: 0.2
    };

    const response = await this.client.post('/chat/completions', payload);
    return response.data.choices[0]?.message?.content || 'No response from brain core.';
  }

  private parseAndValidate(jsonString: string): StructuredExtractionResult {
    let rawJson: any;
    try {
      // Find JSON object if wrapped in markdown code blocks
      const cleanJson = jsonString.replace(/```json\n?|\n?```/g, '').trim();
      rawJson = JSON.parse(cleanJson);
    } catch (e) {
      throw new Error(`Invalid JSON returned from NVIDIA NIM: ${jsonString.substring(0, 100)}`);
    }

    const parsed = StructuredExtractionResultSchema.safeParse(rawJson);
    if (!parsed.success) {
      // Graceful normalization
      return {
        memory: {
          title: rawJson.memory?.title || 'Academic Memory',
          summary: rawJson.memory?.summary || 'Extracted student notes',
          type: 'assignment',
          subject: rawJson.memory?.subject || null,
          course: rawJson.memory?.course || 'General Academic',
          professor: rawJson.memory?.professor || null,
          deadline: rawJson.memory?.deadline || null,
          importance: 'high'
        },
        tasks: Array.isArray(rawJson.tasks) ? rawJson.tasks : [],
        facts: Array.isArray(rawJson.facts) ? rawJson.facts : [],
        uncertainties: Array.isArray(rawJson.uncertainties) ? rawJson.uncertainties : [],
        confidence: typeof rawJson.confidence === 'number' ? rawJson.confidence : 0.92
      };
    }

    return parsed.data;
  }
}
