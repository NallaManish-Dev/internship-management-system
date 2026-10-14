import { AlertCircle, Inbox, LoaderCircle } from 'lucide-react';
import { Button } from './Button';

export function LoadingState({ label = 'Loading…' }) {
  return <div className="feedback-state" role="status"><LoaderCircle className="spin" size={26} />{label}</div>;
}

export function EmptyState({ title, message, action }) {
  return <section className="feedback-state feedback-state--panel"><Inbox size={32} /><h2>{title}</h2><p>{message}</p>{action}</section>;
}

export function ErrorState({ message = 'This information is not available yet.', onRetry }) {
  return <section className="feedback-state feedback-state--panel" role="alert"><AlertCircle size={32} /><h2>Unable to load this page</h2><p>{message}</p>{onRetry && <Button variant="secondary" onClick={onRetry}>Try again</Button>}</section>;
}
