import { AIProvider } from './ai.provider';
import { StructuredExtractionResult } from './ai.schemas';

export class MockAIProvider implements AIProvider {
  async extractFromText(text: string): Promise<StructuredExtractionResult> {
    const lower = text.toLowerCase();
    const course = lower.containsAny(['physics']) ? 'Physics 101' :
      lower.containsAny(['cs', 'sql', 'database', 'miller']) ? 'CS 101' :
      lower.containsAny(['chem', 'acid']) ? 'Chemistry 1A' : 'General Academic';

    const deadline = lower.containsAny(['friday']) ? 'Due Friday, 9:00 AM' :
      lower.containsAny(['tomorrow']) ? 'Due Tomorrow, 8:00 PM' : 'Due Thursday, 3:00 PM';

    return {
      memory: {
        title: text.length > 30 ? text.substring(0, 27) + '...' : text,
        summary: text,
        type: 'assignment',
        subject: course,
        course: course,
        professor: lower.includes('miller') ? 'Prof. Miller' : 'Dr. Alvarez',
        deadline: deadline,
        importance: 'high'
      },
      tasks: [
        {
          title: `Complete ${course} assignment`,
          description: `Work on items for ${deadline}`,
          dueAt: deadline,
          priority: 'high',
          estimatedMinutes: 45
        }
      ],
      facts: [`Course: ${course}`, `Target deadline: ${deadline}`],
      uncertainties: [],
      confidence: 0.95
    };
  }

  async extractFromImage(imageBuffer: Buffer, mimeType: string, voiceContext?: string): Promise<StructuredExtractionResult> {
    return this.extractFromText(voiceContext || 'Physics Module 4 Assignment problem set due tomorrow 8:00 PM.');
  }

  async extractFromDocument(pdfBuffer: Buffer, fileName: string): Promise<StructuredExtractionResult> {
    return this.extractFromText(`Syllabus Extract from ${fileName}: CS 101 Relational Database Spec due Nov 15.`);
  }

  async chat(messages: Array<{ role: 'user' | 'assistant' | 'system'; content: string }>): Promise<string> {
    const last = messages[messages.length - 1]?.content.toLowerCase() || '';
    if (last.includes('physics')) {
      return 'Physics Module 4 is due tomorrow at 8:00 PM. Neural Radar has auto-blocked 2:00 PM–4:00 PM for your sprint.';
    }
    return 'I checked your memory vault. You have 42 active academic entries synced across Physics, CS 101, and Chemistry.';
  }
}

declare global {
  interface String {
    containsAny(keywords: string[]): boolean;
  }
}

String.prototype.containsAny = function(keywords: string[]): boolean {
  return keywords.some(k => this.includes(k));
};
