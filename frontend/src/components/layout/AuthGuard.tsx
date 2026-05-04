'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { hydrate } from '@/store/slices/authSlice';
import { Loader2 } from 'lucide-react';

export function AuthGuard({ children }: { children: React.ReactNode }) {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const { isAuthenticated, isHydrated } = useAppSelector((s) => s.auth);

  useEffect(() => {
    dispatch(hydrate());
  }, [dispatch]);

  useEffect(() => {
    if (isHydrated && !isAuthenticated) {
      router.push('/login');
    }
  }, [isHydrated, isAuthenticated, router]);

  if (!isHydrated) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <Loader2 className="w-8 h-8 text-white animate-spin opacity-50" />
      </div>
    );
  }

  if (!isAuthenticated) return null;

  return <>{children}</>;
}
