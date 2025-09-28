import React from 'react';

export const useTheme = () => ({
  theme: 'light',
  toggleTheme: jest.fn(),
});

export const ThemeProvider = ({ children }) => <div data-testid="theme-provider">{children}</div>;
