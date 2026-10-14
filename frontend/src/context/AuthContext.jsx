import { createContext, useContext, useMemo, useState } from 'react';

const AuthContext = createContext({ user: null, setUser: () => {}, setSession: () => {}, clearSession: () => {} });
const storageKey = 'internhub_current_user';
export const tokenStorageKey = 'internhub_access_token';

function getStoredUser() {
  try {
    const user = JSON.parse(sessionStorage.getItem(storageKey));
    return user?.role ? user : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [user, setCurrentUser] = useState(getStoredUser);
  const setUser = (nextUser) => {
    setCurrentUser(nextUser);
    if (nextUser) sessionStorage.setItem(storageKey, JSON.stringify(nextUser));
    else sessionStorage.removeItem(storageKey);
  };
  const setSession = (session) => {
    setUser(session.user);
    sessionStorage.setItem(tokenStorageKey, session.accessToken);
  };
  const clearSession = () => {
    setCurrentUser(null);
    sessionStorage.removeItem(storageKey);
    sessionStorage.removeItem(tokenStorageKey);
  };
  const value = useMemo(() => ({ user, setUser, setSession, clearSession }), [user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() { return useContext(AuthContext); }
