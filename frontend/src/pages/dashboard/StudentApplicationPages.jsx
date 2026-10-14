import { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button } from '../../components/common/Button';
import { ErrorState, EmptyState, LoadingState } from '../../components/common/Feedback';
import { apiClient } from '../../services/apiClient';
import { StatusBadge } from '../../components/common/StatusBadge';

export function StudentApplicationListPage() {
  const [data, setData] = useState(null); const [error, setError] = useState(null); const [page, setPage] = useState(0); const [pending, setPending] = useState(false);
  const load = useCallback(async (nextPage = 0) => { setError(null); setPending(true); try { setData((await apiClient.get('/students/me/applications', { params: { page: nextPage, size: 10 } })).data); setPage(nextPage); } catch (requestError) { setError(requestError.message); } finally { setPending(false); } }, []);
  useEffect(() => { load(); }, [load]);
  if (error) return <ErrorState message={error} onRetry={() => load(page)} />;
  if (!data) return <LoadingState label="Loading applications..." />;
  return <><header className="page-header"><span className="eyebrow">Student workspace</span><h1>My applications</h1><p>Track the applications submitted from your account.</p></header>{data.content.length === 0 ? <EmptyState title="No applications yet" message="Browse open internships and apply to one that matches your goals." action={<Link to="/student/internships"><Button>Browse internships</Button></Link>} /> : <><div className="feature-grid">{data.content.map((application) => <article className="feature-card" key={application.id}><span className="eyebrow">{application.status}</span><h2>{application.internshipTitle}</h2><p>{application.companyName}</p><p>Applied {new Date(application.appliedAt).toLocaleDateString()}</p><Link to={`/student/applications/${application.id}`}><Button variant="secondary">View application</Button></Link></article>)}</div><div className="hero-actions"><Button type="button" variant="secondary" disabled={pending || page === 0} onClick={() => load(page - 1)}>Previous</Button><span aria-live="polite">Page {page + 1} of {data.totalPages || 1}</span><Button type="button" variant="secondary" disabled={pending || page + 1 >= data.totalPages} onClick={() => load(page + 1)}>Next</Button></div></>}</>;
}

export function StudentApplicationDetailPage() {
  const { id } = useParams(); const [application, setApplication] = useState(null); const [error, setError] = useState(null);
  const load = useCallback(async () => { setError(null); try { setApplication((await apiClient.get(`/students/me/applications/${id}`)).data); } catch (requestError) { setError(requestError.message); } }, [id]);
  useEffect(() => { load(); }, [load]);
  if (error) return <section className="page-container"><ErrorState message={error} onRetry={load} /><Link to="/student/applications"><Button variant="secondary">Back to applications</Button></Link></section>;
  if (!application) return <section className="page-container"><LoadingState label="Loading application..." /></section>;
  return <section className="page-container"><header className="page-header"><span className="eyebrow">Student workspace</span><h1>{application.internshipTitle}</h1><p>{application.companyName} · <StatusBadge status={application.status} /></p></header><article className="feature-card"><h2>Application timeline</h2><div className="application-timeline">{(application.history || []).map((event) => <div className="timeline-event" key={`${event.status}-${event.changedAt}`}><StatusBadge status={event.status} /><span>{new Date(event.changedAt).toLocaleString()}</span></div>)}</div><Link to="/student/applications"><Button variant="secondary">Back to applications</Button></Link></article></section>;
}
