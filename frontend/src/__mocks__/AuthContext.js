import React from 'react';

export const useAuth = () => ({
  loggedIn: false,
  email: 'test@example.com',
  login: jest.fn(),
  logout: jest.fn(),
});

export const AuthProvider = ({ children }) => <div data-testid="auth-provider">{children}</div>;
