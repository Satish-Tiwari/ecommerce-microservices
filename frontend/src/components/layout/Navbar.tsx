'use client';

import Link from 'next/link';
import { useAppSelector } from '@/store/hooks';
import { useLogout, useProfile } from '@/hooks/useAuth';
import { useEffect, useState } from 'react';
import { hydrate } from '@/store/slices/authSlice';
import { useAppDispatch } from '@/store/hooks';
import { ThemeToggle } from '@/components/common/ThemeToggle';
import { LogOut, LayoutDashboard, ShoppingBag, User } from 'lucide-react';

export function Navbar() {
  const dispatch = useAppDispatch();
  const { isAuthenticated, user, isHydrated } = useAppSelector((s) => s.auth);
  const handleLogout = useLogout();
  useProfile();
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    dispatch(hydrate());
    setMounted(true);
  }, [dispatch]);

  if (!mounted) return null;

  return (
    <nav className="sticky top-0 z-50 border-b border-card-border bg-background/80 backdrop-blur-2xl">
      <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
        {/* Brand */}
        <Link href="/" className="flex items-center gap-3 group">
          <div className="w-10 h-10 rounded-xl bg-foreground flex items-center justify-center group-hover:rotate-6 transition-transform duration-300">
            <ShoppingBag className="w-5 h-5 text-background" />
          </div>
          <span className="font-bold text-2xl tracking-tighter uppercase font-display text-foreground">
            Ecomm<span className="font-light text-muted-foreground">Admin</span>
          </span>
        </Link>

        {/* Actions */}
        <div className="flex items-center gap-6">
          {isHydrated && isAuthenticated ? (
            <>
              <Link
                href="/dashboard"
                className="text-sm font-bold text-muted-foreground hover:text-foreground transition-colors flex items-center gap-2"
              >
                <LayoutDashboard className="w-4 h-4" />
                Dashboard
              </Link>

              <div className="h-4 w-[1px] bg-card-border" />

              <div className="flex items-center gap-3">
                <div className="flex flex-col items-end hidden sm:flex">
                  <span className="text-xs font-bold text-foreground leading-none">
                    {user?.fullname || user?.username}
                  </span>
                  <span className="text-[10px] text-muted-foreground uppercase tracking-widest mt-1">
                    Store Manager
                  </span>
                </div>
                <div className="w-9 h-9 rounded-full bg-secondary flex items-center
                                justify-center text-foreground font-black border border-card-border">
                  <User className="w-4 h-4" />
                </div>
              </div>

              <div className="flex items-center gap-2">
                <ThemeToggle />
                <button
                  onClick={handleLogout}
                  className="w-9 h-9 rounded-lg border border-card-border flex items-center justify-center
                             text-muted-foreground hover:text-foreground hover:bg-secondary transition-all"
                  title="Logout"
                >
                  <LogOut className="w-4 h-4" />
                </button>
              </div>
            </>
          ) : (
            <div className="flex items-center gap-3">
              <ThemeToggle />
              <Link href="/login" className="text-sm font-bold text-muted-foreground hover:text-foreground px-4 py-2">
                Sign In
              </Link>
              <Link href="/register" className="btn-primary !py-2 !px-5 !text-sm">
                Get Started
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
