import React from 'react';
import { AlertCircle, CheckCircle } from 'lucide-react';

interface AlertProps {
  type: 'error' | 'success';
  message: string | null;
}


export const Alert: React.FC<AlertProps> = ({ type, message }) => {
  if (!message) return null;

  return (
    <div className={`alert alert-${type}`}>
      { type === 'error' &&  <AlertCircle size={18} /> }
       {type === 'success' &&  <CheckCircle size={18} />}
      <span>{message}</span>
    </div>
  );
};
