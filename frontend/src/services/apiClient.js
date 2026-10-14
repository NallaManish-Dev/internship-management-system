import axios from 'axios';
import { tokenStorageKey } from '../context/AuthContext';

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || (import.meta.env.DEV ? 'http://localhost:8080/api/v1' : '/api/v1');

export const apiClient = axios.create({
  baseURL: apiBaseUrl,
  headers: { Accept: 'application/json' },
  timeout: 10000,
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    // Only invalidate the local session when the authenticated session check fails.
    // A protected mutation (such as applying) should surface its API error without
    // unexpectedly redirecting the user or deleting the session while they work.
    if (error.response?.status === 401 && error.config?.url?.endsWith('/auth/me')) {
      sessionStorage.removeItem('internhub_current_user');
      sessionStorage.removeItem(tokenStorageKey);
    }
    return Promise.reject(normalizeApiError(error));
  },
);

apiClient.interceptors.request.use((config) => {
  const token = sessionStorage.getItem(tokenStorageKey);
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export function normalizeApiError(error) {
  const payload = error.response?.data;
  return {
    status: error.response?.status,
    code: payload?.code || 'REQUEST_FAILED',
    message: payload?.message || 'We could not complete that request. Please try again.',
    fieldErrors: payload?.fieldErrors || {},
  };
}
