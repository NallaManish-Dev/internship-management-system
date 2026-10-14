import { LoaderCircle } from 'lucide-react';

export function Button({ children, className = '', loading = false, type = 'button', variant = 'primary', disabled = false, ...props }) {
  return (
    <button className={`app-button app-button--${variant} ${className}`} type={type} disabled={loading || disabled} {...props}>
      {loading && <LoaderCircle aria-hidden="true" className="spin" size={17} />}
      {children}
    </button>
  );
}
