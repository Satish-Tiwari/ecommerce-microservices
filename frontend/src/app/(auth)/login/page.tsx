'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useLogin } from '@/hooks/useAuth';
import { ShoppingBag, ArrowRight, Loader2, Key, User } from 'lucide-react';

export default function LoginPage() {
  const { mutate: login, isPending } = useLogin();
  const [formData, setFormData] = useState({ username: '', password: '' });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    login(formData);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-6 relative overflow-hidden">
      {/* Background Glow */}
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-foreground/[0.03] rounded-full blur-[100px] pointer-events-none" />

      <div className="w-full max-w-md relative z-10 animate-slide-up">
        <div className="text-center mb-12">
          <Link href="/" className="inline-flex items-center gap-3 mb-8 group">
            <div className="w-12 h-12 rounded-2xl bg-foreground flex items-center justify-center group-hover:scale-105 transition-transform">
              <ShoppingBag className="w-6 h-6 text-background" />
            </div>
            <span className="text-3xl font-bold tracking-tighter uppercase text-foreground font-display">Ecomm</span>
          </Link>
          <h1 className="text-3xl font-bold text-foreground uppercase tracking-tight font-display">Welcome Back</h1>
          <p className="text-muted-foreground mt-3 font-medium tracking-wide">Access your enterprise dashboard</p>
        </div>

        <div className="p-10 rounded-3xl bg-card border border-card-border backdrop-blur-3xl shadow-2xl">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <label className="text-[10px] font-bold uppercase tracking-[0.2em] text-muted-foreground ml-1 font-display">Username</label>
              <div className="relative">
                <User className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder="Enter your username"
                  className="input-field !pl-12"
                  value={formData.username}
                  onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                  required
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-[10px] font-bold uppercase tracking-[0.2em] text-muted-foreground ml-1 font-display">Password</label>
              <div className="relative">
                <Key className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <input
                  type="password"
                  placeholder="••••••••"
                  className="input-field !pl-12"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  required
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={isPending}
              className="btn-primary w-full !py-4 shadow-xl"
            >
              {isPending ? (
                <Loader2 className="w-5 h-5 animate-spin" />
              ) : (
                <>Sign In <ArrowRight className="w-5 h-5" /></>
              )}
            </button>
          </form>

          <div className="mt-10 text-center">
            <p className="text-muted-foreground text-sm font-medium">
              Don&apos;t have an account?{' '}
              <Link href="/register" className="text-foreground font-bold hover:underline underline-offset-4 font-display uppercase text-[11px] tracking-wider">
                Create Account
              </Link>
            </p>
          </div>
        </div>

        <div className="mt-12 text-center">
          <p className="text-[10px] font-bold text-muted-foreground uppercase tracking-[0.4em] font-display opacity-40">Ecomm Admin Enterprise v1.0</p>
        </div>
      </div>
    </div>
  );
}
