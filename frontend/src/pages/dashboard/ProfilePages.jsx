import { useEffect, useState } from 'react';
import { Button } from '../../components/common/Button';
import { ErrorState, LoadingState } from '../../components/common/Feedback';
import { apiClient } from '../../services/apiClient';
import { useAuth } from '../../context/AuthContext';

export function ProfilePage({ role }) {
  const path = role === 'student' ? '/students/me' : '/companies/me'; const { setUser } = useAuth();
  const [profile, setProfile] = useState(null); const [values, setValues] = useState({}); const [error, setError] = useState(null); const [notice, setNotice] = useState(''); const [pending, setPending] = useState(false);
  useEffect(() => { let active = true; apiClient.get(path).then(({ data }) => { if (!active) return; setProfile(data); setValues(role === 'student' ? { name: data.user.name, phone: data.phone || '', institution: data.institution || '', degreeOrBranch: data.degreeOrBranch || '', graduationYear: data.graduationYear || '', cgpa: data.cgpa || '', skills: (data.skills || []).join(', ') } : { companyName: data.companyName, description: data.description || '', website: data.website || '', industry: data.industry || '', contactInformation: data.contactInformation || '' }); }).catch((requestError) => active && setError(requestError.message)); return () => { active = false; }; }, [path, role]);
  if (error) return <ErrorState message={error} />;
  if (!profile) return <LoadingState label="Loading profile..." />;
  const update = (event) => { setValues((current) => ({ ...current, [event.target.name]: event.target.value })); setNotice(''); setError(null); };
  const submit = async (event) => { event.preventDefault(); if (role === 'student' && !values.name.trim()) { setError('Name is required.'); return; } if (role === 'company' && !values.companyName.trim()) { setError('Company name is required.'); return; } setPending(true); setError(null); setNotice(''); try { const body = role === 'student' ? { ...values, graduationYear: values.graduationYear ? Number(values.graduationYear) : null, cgpa: values.cgpa ? Number(values.cgpa) : null, skills: values.skills.split(',').map((skill) => skill.trim()).filter(Boolean) } : values; const response = await apiClient.put(path, body); setProfile(response.data); if (response.data.user) setUser(response.data.user); setNotice('Profile updated successfully.'); } catch (requestError) { setError(requestError.message); } finally { setPending(false); } };
  return <form className="auth-card" onSubmit={submit} noValidate><span className="eyebrow">{role} workspace</span><h1>Profile</h1>{Object.entries(values).map(([key, value]) => <label key={key}>{key.replaceAll(/([A-Z])/g, ' $1')}<input name={key} type={key === 'graduationYear' ? 'number' : key === 'cgpa' ? 'number' : 'text'} step={key === 'cgpa' ? '0.01' : undefined} value={value} onChange={update} /></label>)}{error && <p className="form-notice form-notice--error" role="alert">{error}</p>}{notice && <p className="form-notice form-notice--success" role="status">{notice}</p>}<Button type="submit" loading={pending}>Save profile</Button></form>;
}
