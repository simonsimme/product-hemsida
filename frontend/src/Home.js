
import { useTheme } from './ThemeContext';

export default function Home() {
  const { theme } = useTheme();
  return (
    <div style={{ padding: '2rem', maxWidth: '700px', margin: '0 auto', color: theme === 'dark' ? '#fff' : '#181818' }}>
      <h1>Welcome to the NFT shop of PrimeAgen</h1>
      <p>Here you can buy the coolest PrimeAgen NFTs, each one unique and guaranteed to boost your Vim skills and hairline.</p>
      <p>
        Browse our exclusive collection of digital art, featuring legendary moments and memes from the world of PrimeAgen. Every NFT is a one-of-a-kind collectible, perfect for fans, developers, and meme enthusiasts alike.
      </p>
      <p>
        <strong>How it works:</strong><br />
        1. Explore the Products page to see all available NFTs.<br />
        2. Click on any NFT to view details and add it to your cart.<br />
        3. Checkout securely and become the proud owner of a PrimeAgen NFT!
      </p>
      <p>
        <em>Note: All NFTs are for entertainment purposes only. No actual Vim speed or hairline improvements guaranteed!</em>
      </p>
    </div>
  );
}
