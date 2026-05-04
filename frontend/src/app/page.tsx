import Link from 'next/link';
import { Navbar } from '@/components/layout/Navbar';
import {
  Package, 
  Zap, 
  Shield, 
  Layers,
  BarChart3,
  Globe,
  ArrowRight, 
  ChevronRight,
  ShoppingBag,
  Cpu,
  Monitor,
  Database
} from 'lucide-react';

const FEATURES = [
  {
    icon: Layers,
    title: 'Microservices Architecture',
    desc: 'Powered by Spring Boot and Spring Cloud for massive scalability.',
  },
  {
    icon: Shield,
    title: 'Stateless Security',
    desc: 'Stateless JWT authentication shared across the entire ecosystem.',
  },
  {
    icon: Database,
    title: 'Reactive Catalog',
    desc: 'Manage millions of products with a high-performance reactive engine.',
  }
];

export default function HomePage() {
  return (
    <div className="min-h-screen flex flex-col bg-background text-foreground selection:bg-foreground selection:text-background">
      <Navbar />

      {/* Hero */}
      <section className="relative flex-1 flex flex-col items-center justify-center pt-32 pb-20 px-6 overflow-hidden">
        {/* Subtle Background Elements */}
        <div className="absolute top-1/4 left-1/2 -translate-x-1/2 w-[800px] h-[400px] bg-foreground/[0.03] rounded-full blur-[120px] pointer-events-none" />
        
        <div className="relative z-10 max-w-5xl mx-auto text-center animate-fade-in">
          <div className="inline-flex items-center gap-2 px-5 py-2 rounded-full
                          bg-secondary text-muted-foreground text-[10px] font-bold uppercase tracking-[0.3em] mb-10
                          border border-card-border font-display">
            <Globe className="w-3.5 h-3.5" />
            Distributed Enterprise Core
          </div>

          <h1 className="text-6xl md:text-8xl font-bold mb-8 leading-[0.85] tracking-tighter uppercase font-display text-foreground">
            Ecomm<span className="text-muted-foreground font-light">Admin</span>
          </h1>

          <p className="text-xl md:text-2xl text-muted-foreground max-w-2xl mx-auto mb-12 font-medium leading-relaxed tracking-tight">
            The definitive management ecosystem for high-scale microservices. 
            Built for the modern web, designed for absolute performance.
          </p>

          <div className="flex flex-col sm:flex-row gap-6 justify-center">
            <Link href="/register" className="btn-primary !py-5 !px-12 !text-lg">
              Launch Dashboard <ArrowRight className="ml-2 w-5 h-5" />
            </Link>
            <Link href="/login" className="btn-secondary !py-5 !px-12 !text-lg">
              Sign In
            </Link>
          </div>
        </div>

        {/* System Preview Mockup */}
        <div className="mt-24 w-full max-w-4xl mx-auto px-6 animate-float">
          <div className="rounded-3xl border border-card-border bg-card p-2 backdrop-blur-3xl shadow-2xl">
            <div className="rounded-2xl border border-card-border bg-background p-6">
              <div className="flex items-center gap-2 mb-8">
                <div className="w-3 h-3 rounded-full bg-secondary" />
                <div className="w-3 h-3 rounded-full bg-secondary" />
                <div className="w-3 h-3 rounded-full bg-secondary" />
                <div className="ml-4 h-4 w-40 bg-secondary/50 rounded-full" />
              </div>
              <div className="grid grid-cols-3 gap-4">
                <div className="h-32 rounded-xl bg-secondary/30 border border-card-border" />
                <div className="h-32 rounded-xl bg-secondary/30 border border-card-border" />
                <div className="h-32 rounded-xl bg-secondary/30 border border-card-border" />
              </div>
              <div className="mt-4 h-40 rounded-xl bg-secondary/20 border border-card-border" />
            </div>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="py-32 px-6 border-t border-card-border bg-secondary/10">
        <div className="max-w-6xl mx-auto">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-12">
            {FEATURES.map((f, i) => (
              <div key={i} className="group">
                <div className="w-12 h-12 rounded-2xl bg-secondary flex items-center justify-center
                                mb-8 text-foreground group-hover:bg-foreground group-hover:text-background transition-all duration-500">
                  <f.icon className="w-6 h-6" />
                </div>
                <h3 className="text-xl font-bold uppercase tracking-tight mb-4 text-foreground font-display">{f.title}</h3>
                <p className="text-muted-foreground font-medium leading-relaxed">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-card-border py-20 px-6">
        <div className="max-w-6xl mx-auto flex flex-col md:flex-row justify-between items-center gap-10">
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded bg-foreground flex items-center justify-center">
              <ShoppingBag className="w-4 h-4 text-background" />
            </div>
            <span className="font-bold text-xl uppercase tracking-tighter text-foreground font-display">Ecomm</span>
          </div>
          <p className="text-xs font-bold text-muted-foreground uppercase tracking-[0.4em]">
            © 2024 Microservices E-commerce Platform.
          </p>
          <div className="flex gap-10">
            {['Docs', 'GitHub', 'System Status'].map(link => (
              <a key={link} href="#" className="text-xs font-bold uppercase tracking-widest text-muted-foreground hover:text-foreground transition-colors font-display">
                {link}
              </a>
            ))}
          </div>
        </div>
      </footer>
    </div>
  );
}
