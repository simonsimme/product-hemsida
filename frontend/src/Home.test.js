import { render, screen } from '@testing-library/react';
import { ThemeProvider } from './ThemeContext';
import Home from './Home';

test('renders home component', () => {
  render(
    <ThemeProvider>
      <Home />
    </ThemeProvider>
  );
  const welcomeText = screen.getByText(/Welcome to the NFT shop/i);
  expect(welcomeText).toBeInTheDocument();
});
