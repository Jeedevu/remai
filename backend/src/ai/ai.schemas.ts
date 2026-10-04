import { z } from 'zod';

export const TaskSchema = z.object({
  title: z.string(),
  description: z.string().default(''),
  dueAt: z.string().nullable().default(null),
  priority: z.enum(['low', 'medium', 'high', 'critical']).default('medium'),
  estimatedMinutes: z.number().int().positive().default(30)
});

export const MemorySchema = z.object({
  title: z.string(),
  summary: z.string(),
  type: z.enum(['assignment', 'exam', 'announcement', 'note', 'project', 'other']).default('assignment'),
  subject: z.string().nullable().default(null),
  course: z.string().nullable().default(null),
  professor: z.string().nullable().default(null),
  deadline: z.string().nullable().default(null),
  importance: z.enum(['low', 'medium', 'high', 'critical']).default('medium')
});

export const StructuredExtractionResultSchema = z.object({
  memory: MemorySchema,
  tasks: z.array(TaskSchema).default([]),
  facts: z.array(z.string()).default([]),
  uncertainties: z.array(z.string()).default([]),
  confidence: z.number().min(0).max(1).default(0.9)
});

export type TaskDto = z.infer<typeof TaskSchema>;
export type MemoryDto = z.infer<typeof MemorySchema>;
export type StructuredExtractionResult = z.infer<typeof StructuredExtractionResultSchema>;
