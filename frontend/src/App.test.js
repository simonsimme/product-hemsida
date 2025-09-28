import { render } from '@testing-library/react';
import App from './App';

// Use manual mocks
jest.mock('react-router-dom');
jest.mock('./AuthContext');
jest.mock('./ThemeContext');

test('renders app without crashing', () => {
  render(<App />);
  // Test passes if component renders without throwing
  expect(true).toBe(true);
});
