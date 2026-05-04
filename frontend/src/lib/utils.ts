import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';

/** Merge Tailwind classes with conflict resolution */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/** Format a date string to locale display */
export function formatDate(date: string | null | undefined): string {
  if (!date) return '-';
  return new Date(date).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  });
}

/** Format a date string to relative time */
export function formatRelativeTime(date: string | null | undefined): string {
  if (!date) return '-';
  const now = new Date();
  const d = new Date(date);
  const diffMs = now.getTime() - d.getTime();
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMs / 3600000);
  const diffDays = Math.floor(diffMs / 86400000);

  if (diffMins < 1) return 'Just now';
  if (diffMins < 60) return `${diffMins}m ago`;
  if (diffHours < 24) return `${diffHours}h ago`;
  if (diffDays < 7) return `${diffDays}d ago`;
  return formatDate(date);
}

/** Status color mapping */
export function getStatusColor(status: string): string {
  const map: Record<string, string> = {
    PENDING: 'text-gray-400 bg-white/[0.05] border-white/[0.05]',
    CRAWLING: 'text-white bg-white/[0.08] border-white/[0.1]',
    PROCESSING: 'text-white bg-white/[0.08] border-white/[0.1]',
    EMBEDDING: 'text-white bg-white/[0.08] border-white/[0.1]',
    GENERATING: 'text-white bg-white/[0.08] border-white/[0.1]',
    READY: 'text-black bg-white border-white',
    COMPLETED: 'text-black bg-white border-white',
    PAUSED: 'text-gray-500 bg-white/[0.02] border-white/[0.05]',
    CANCELLED: 'text-gray-600 bg-transparent border-white/[0.05]',
    FAILED: 'text-white bg-red-950/20 border-red-900/30',
  };
  return map[status] || 'text-gray-400 bg-white/[0.05]';
}

/** Status label mapping */
export function getStatusLabel(status: string): string {
  const map: Record<string, string> = {
    PENDING: 'Pending',
    CRAWLING: 'Crawling...',
    PROCESSING: 'Processing',
    EMBEDDING: 'Embedding',
    GENERATING: 'Generating',
    READY: 'Ready',
    COMPLETED: 'Finished',
    PAUSED: 'Paused',
    CANCELLED: 'Stopped',
    FAILED: 'Failed',
  };
  return map[status] || status;
}

/** Check if status is a processing state */
export function isProcessing(status: string): boolean {
  return ['CRAWLING', 'PROCESSING', 'EMBEDDING', 'GENERATING'].includes(status);
}

/** Generate a random ID for chat messages */
export function generateId(): string {
  return `${Date.now()}-${Math.random().toString(36).slice(2, 9)}`;
}
