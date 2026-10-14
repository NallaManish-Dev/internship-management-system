import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from '../../components/common/Button';
import { useAuth } from '../../context/AuthContext';
import { apiClient } from '../../services/apiClient';

const passwordHint = 'Use at least 8 characters, including a letter and a number.';

export function LoginPage() {
  return <AuthForm mode="login" title="Welcome back" subtitle="Sign in to access your workspace." fields={['Email address', 'Password']} footer="New to InternHub?" linkTo="/register/student" linkText="Create a student account" />;
}

export function StudentRegistrationPage() {
  return <AuthForm mode="student-registration" title="Start your journey" subtitle="Create your student account." fields={['Full name', 'Email address', 'Password', 'Confirm password']} footer="Already have an account?" linkTo="/login" linkText="Sign in" />;
}

export function CompanyRegistrationPage() {
  return <AuthForm mode="company-registration" title="Find your next great intern" subtitle="Create your company account." fields={['Company name', 'Email address', 'Password', 'Confirm password']} footer="Already have an account?" linkTo="/login" linkText="Sign in" />;
}

function AuthForm({ mode, title, subtitle, fields, footer, linkTo, linkText }) {
  const navigate = useNavigate();
  const location = useLocation();
  const { setSession } = useAuth();
  const [values, setValues] = useState({});
  const [errors, setErrors] = useState({});
  const [notice, setNotice] = useState(location.state?.message || '');
  const [pending, setPending] = useState(false);
  const isLogin = mode === 'login';
  const isCompany = mode === 'company-registration';

  function updateValue(field, value) {
    const key = fieldKey(field);
    setValues((current) => ({ ...current, [key]: value }));
    setErrors((current) => ({ ...current, [key]: '' }));
    setNotice('');
  }

  function validate() {
    const nextErrors = {};
    if (!isLogin && !values[isCompany ? 'companyName' : 'name']?.trim()) nextErrors[isCompany ? 'companyName' : 'name'] = `${isCompany ? 'Company name' : 'Full name'} is required.`;
    if (!values.email?.trim()) nextErrors.email = 'Email address is required.';
    else if (!/^\S+@\S+\.\S+$/.test(values.email)) nextErrors.email = 'Enter a valid email address.';
    if (!values.password) nextErrors.password = 'Password is required.';
    else if (!isLogin && (!/^.{8,72}$/.test(values.password) || !/[A-Za-z]/.test(values.password) || !/\d/.test(values.password))) nextErrors.password = passwordHint;
    if (!isLogin && values.password !== values.confirmPassword) nextErrors.confirmPassword = 'Passwords must match.';
    return nextErrors;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length) { setErrors(validationErrors); setNotice('Please correct the highlighted fields.'); return; }
    setPending(true); setErrors({}); setNotice('');
    try {
      if (isLogin) {
        const response = await apiClient.post('/auth/login', { email: values.email.trim(), password: values.password });
        setSession(response.data);
        const dashboard = { STUDENT: '/student/dashboard', COMPANY: '/company/dashboard', ADMIN: '/admin/dashboard' }[response.data.user.role];
        navigate(dashboard || '/unauthorized', { replace: true });
      } else {
        const path = isCompany ? '/auth/register/company' : '/auth/register/student';
        const payload = isCompany ? { companyName: values.companyName.trim(), email: values.email.trim(), password: values.password } : { name: values.name.trim(), email: values.email.trim(), password: values.password };
        await apiClient.post(path, payload);
        navigate('/login', { replace: true, state: { message: 'Account created successfully. Sign in to continue.' } });
      }
    } catch (error) {
      setErrors(error.fieldErrors || {});
      setNotice(error.message || 'We could not complete that request. Please try again.');
    } finally { setPending(false); }
  }

  return <section className="auth-page"><form className="auth-card" onSubmit={handleSubmit} noValidate><span className="eyebrow">InternHub</span><h1>{title}</h1><p>{subtitle}</p>{fields.map((field) => <AuthField key={field} field={field} value={values[fieldKey(field)] || ''} error={errors[fieldKey(field)]} onChange={updateValue} isLogin={isLogin} />)}{!isLogin && <small className="form-help">{passwordHint}</small>}{notice && <p className={`form-notice ${Object.keys(errors).length || notice.startsWith('We could') || notice.startsWith('Invalid') ? 'form-notice--error' : 'form-notice--success'}`} role="alert">{notice}</p>}<Button type="submit" loading={pending}>{isLogin ? 'Sign in' : 'Create account'}</Button><p className="auth-footer">{footer} <Link to={linkTo}>{linkText}</Link></p></form></section>;
}

function AuthField({ field, value, error, onChange, isLogin }) {
  const key = fieldKey(field);
  const password = field.toLowerCase().includes('password');
  return <label>{field}<input required value={value} type={password ? 'password' : key === 'email' ? 'email' : 'text'} autoComplete={password ? (isLogin ? 'current-password' : 'new-password') : key === 'email' ? 'email' : 'name'} aria-invalid={Boolean(error)} onChange={(event) => onChange(field, event.target.value)} />{error && <span className="field-error" role="alert">{error}</span>}</label>;
}

function fieldKey(field) {
  return { 'Full name': 'name', 'Company name': 'companyName', 'Email address': 'email', Password: 'password', 'Confirm password': 'confirmPassword' }[field];
}
