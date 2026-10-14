const labels = {
  APPLIED: 'Applied', UNDER_REVIEW: 'Under review', SHORTLISTED: 'Shortlisted', SELECTED: 'Selected', REJECTED: 'Rejected', OPEN: 'Open', CLOSED: 'Closed', DRAFT: 'Draft',
};

export function StatusBadge({ status }) {
  return <span className={`status-badge status-badge--${status?.toLowerCase().replace('_', '-') || 'neutral'}`}>{labels[status] || status}</span>;
}
