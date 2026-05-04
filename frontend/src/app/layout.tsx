import type { Metadata } from 'next';
import { Toaster } from 'react-hot-toast';
import { ReduxProvider } from '@/providers/redux-provider';
import { QueryProvider } from '@/providers/query-provider';
import { ThemeProvider } from '@/components/providers/ThemeProvider';
import './globals.css';

export const metadata: Metadata = {
  title: 'Ecomm Admin Panel',
  description: 'Manage your e-commerce microservices with ease.',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className="min-h-screen antialiased bg-background text-foreground transition-colors duration-300">
        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
          disableTransitionOnChange
        >
          <ReduxProvider>
            <QueryProvider>
              {children}
              <Toaster
                position="top-right"
                toastOptions={{
                  duration: 4000,
                  className: 'bg-card text-foreground border border-card-border backdrop-blur-xl',
                }}
              />
            </QueryProvider>
          </ReduxProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
