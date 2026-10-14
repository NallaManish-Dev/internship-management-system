import { createBrowserRouter } from 'react-router-dom';
import { PublicLayout } from '../components/layout/PublicLayout';
import { DashboardLayout } from '../components/layout/DashboardLayout';
import { LandingPage } from '../pages/public/LandingPage';
import { InternshipDetailPage, InternshipListPage } from '../pages/public/InternshipPages';
import { CompanyRegistrationPage, LoginPage, StudentRegistrationPage } from '../pages/public/AuthPages';
import { PendingDashboardPage, PendingWorkspacePage } from '../pages/dashboard/DashboardPages';
import { CompanyInternshipCreatePage, CompanyInternshipListPage } from '../pages/dashboard/CompanyInternshipPages';
import { StudentApplicationListPage, StudentApplicationDetailPage } from '../pages/dashboard/StudentApplicationPages';
import { CompanyApplicantPage } from '../pages/dashboard/CompanyApplicationPages';
import { ProfilePage } from '../pages/dashboard/ProfilePages';
import { AdminListPage } from '../pages/dashboard/AdminPages';
import { AdminAuditPage } from '../pages/dashboard/AdminAuditPage';
import { NotFoundPage, RouteErrorPage, UnauthorizedPage } from '../pages/errors/ErrorPages';
import { ProtectedRoute } from '../routes/ProtectedRoute';

const companyWorkspaceRoutes = { path: 'company', errorElement: <RouteErrorPage />, element: <ProtectedRoute role="company"><DashboardLayout role="company" /></ProtectedRoute>, children: [{ path: 'dashboard', element: <PendingDashboardPage role="company" /> }, { path: 'internships', element: <CompanyInternshipListPage /> }, { path: 'internships/new', element: <CompanyInternshipCreatePage /> }, { path: 'internships/:id/edit', element: <PendingWorkspacePage role="company" /> }, { path: 'internships/:id/applicants', element: <CompanyApplicantPage /> }, { path: 'applications/:id', element: <PendingWorkspacePage role="company" /> }, { path: 'profile', element: <ProfilePage role="company" /> }] };
const studentWorkspaceRoutes = { path: 'student', errorElement: <RouteErrorPage />, element: <ProtectedRoute role="student"><DashboardLayout role="student" /></ProtectedRoute>, children: [{ path: 'dashboard', element: <PendingDashboardPage role="student" /> }, { path: 'internships', element: <InternshipListPage /> }, { path: 'applications', element: <StudentApplicationListPage /> }, { path: 'applications/:id', element: <StudentApplicationDetailPage /> }, { path: 'profile', element: <ProfilePage role="student" /> }] };
export const router = createBrowserRouter([
  { errorElement: <RouteErrorPage />, element: <PublicLayout />, children: [{ index: true, element: <LandingPage /> }, { path: 'internships', element: <InternshipListPage /> }, { path: 'internships/:id', element: <InternshipDetailPage /> }, { path: 'login', element: <LoginPage /> }, { path: 'register/student', element: <StudentRegistrationPage /> }, { path: 'register/company', element: <CompanyRegistrationPage /> }, { path: 'unauthorized', element: <UnauthorizedPage /> }] },
  studentWorkspaceRoutes,
  companyWorkspaceRoutes,
  { path: 'admin', errorElement: <RouteErrorPage />, element: <ProtectedRoute role="admin"><DashboardLayout role="admin" /></ProtectedRoute>, children: [{ path: 'dashboard', element: <PendingDashboardPage role="admin" /> }, { path: 'students', element: <AdminListPage resource="students" title="Students" /> }, { path: 'companies', element: <AdminListPage resource="companies" title="Companies" /> }, { path: 'internships', element: <AdminListPage resource="internships" title="Internships" /> }, { path: 'applications', element: <AdminListPage resource="applications" title="Applications" /> }, { path: 'audit-logs', element: <AdminAuditPage /> }] },
  { path: '*', element: <NotFoundPage /> },
]);
