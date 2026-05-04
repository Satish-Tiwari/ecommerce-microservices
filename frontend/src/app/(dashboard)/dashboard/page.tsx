'use client';

import { 
  ShoppingBag, 
  Users, 
  TrendingUp, 
  CreditCard,
  ArrowUpRight,
  ArrowDownRight,
  Clock,
  Package,
  CheckCircle2,
  AlertCircle,
  MoreVertical
} from 'lucide-react';
import { cn } from '@/lib/utils';

const stats = [
  {
    title: 'Total Revenue',
    value: '$45,231.89',
    change: '+20.1%',
    trend: 'up',
    icon: CreditCard,
  },
  {
    title: 'Active Orders',
    value: '356',
    change: '+12.5%',
    trend: 'up',
    icon: ShoppingBag,
  },
  {
    title: 'New Customers',
    value: '2,345',
    change: '-3.2%',
    trend: 'down',
    icon: Users,
  },
  {
    title: 'Growth Rate',
    value: '18.4%',
    change: '+4.3%',
    trend: 'up',
    icon: TrendingUp,
  }
];

const recentOrders = [
  { id: '#ORD-7234', customer: 'John Doe', product: 'Premium Headphones', status: 'delivered', amount: '$299.00', date: '2 mins ago' },
  { id: '#ORD-7233', customer: 'Sarah Smith', product: 'Wireless Mouse', status: 'processing', amount: '$49.99', date: '15 mins ago' },
  { id: '#ORD-7232', customer: 'Mike Johnson', product: 'Mechanical Keyboard', status: 'shipped', amount: '$159.00', date: '1 hour ago' },
  { id: '#ORD-7231', customer: 'Emily Brown', product: 'Gaming Monitor', status: 'pending', amount: '$449.00', date: '3 hours ago' },
];

const statusStyles = {
  delivered: 'text-foreground bg-secondary border-card-border',
  processing: 'text-muted-foreground bg-secondary/50 border-card-border',
  shipped: 'text-foreground bg-secondary border-card-border',
  pending: 'text-muted-foreground bg-secondary/30 border-card-border',
};

export default function DashboardPage() {
  return (
    <div className="space-y-10 animate-fade-in">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div>
          <h1 className="text-3xl font-bold tracking-tight font-display text-foreground uppercase">Overview</h1>
          <p className="text-muted-foreground mt-2 font-medium tracking-wide">
            Performance analytics and system health for your store.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <button className="btn-secondary !py-2.5">Download Report</button>
          <button className="btn-primary !py-2.5">Create Product</button>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, i) => (
          <div 
            key={stat.title}
            className="p-8 rounded-3xl bg-card border border-card-border
                       hover:bg-secondary transition-all duration-500 group shadow-sm"
            style={{ animationDelay: `${i * 100}ms` }}
          >
            <div className="flex items-center justify-between mb-6">
              <div className="w-12 h-12 rounded-2xl bg-foreground flex items-center justify-center shadow-lg shadow-foreground/5">
                <stat.icon className="w-5 h-5 text-background" />
              </div>
              <div className={cn(
                "flex items-center gap-1 text-[11px] font-black px-2 py-1 rounded-full uppercase tracking-tighter",
                stat.trend === 'up' ? "text-foreground bg-secondary" : "text-muted-foreground bg-secondary/50"
              )}>
                {stat.trend === 'up' ? <ArrowUpRight className="w-3 h-3" /> : <ArrowDownRight className="w-3 h-3" />}
                {stat.change}
              </div>
            </div>
            <p className="text-xs font-bold text-muted-foreground uppercase tracking-widest">{stat.title}</p>
            <h3 className="text-3xl font-black mt-2 tracking-tighter text-foreground">
              {stat.value}
            </h3>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Recent Orders */}
        <div className="lg:col-span-2 p-8 rounded-3xl bg-card border border-card-border shadow-sm">
          <div className="flex items-center justify-between mb-8">
            <h2 className="text-lg font-bold uppercase font-display tracking-tight text-foreground">Recent Sales</h2>
            <MoreVertical className="w-5 h-5 text-muted-foreground cursor-pointer" />
          </div>
          
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead>
                <tr className="text-[10px] uppercase tracking-[0.2em] text-muted-foreground border-b border-card-border">
                  <th className="pb-4 font-black">ID</th>
                  <th className="pb-4 font-black">Client</th>
                  <th className="pb-4 font-black">Product</th>
                  <th className="pb-4 font-black">Value</th>
                  <th className="pb-4 font-black">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-card-border">
                {recentOrders.map((order) => (
                  <tr key={order.id} className="group hover:bg-secondary/50 transition-colors">
                    <td className="py-5 text-sm font-bold text-foreground tracking-tighter">{order.id}</td>
                    <td className="py-5 text-sm font-medium text-muted-foreground">{order.customer}</td>
                    <td className="py-5 text-sm text-muted-foreground font-medium">{order.product}</td>
                    <td className="py-5 text-sm font-black text-foreground">{order.amount}</td>
                    <td className="py-5">
                      <span className={cn(
                        "inline-flex items-center px-3 py-1 rounded-lg text-[10px] font-black uppercase tracking-tighter border",
                        statusStyles[order.status as keyof typeof statusStyles]
                      )}>
                        {order.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* System & Health */}
        <div className="space-y-8">
          <div className="p-8 rounded-3xl bg-card border border-card-border shadow-sm">
            <h2 className="text-lg font-bold uppercase font-display tracking-tight mb-8 text-foreground">Network Status</h2>
            <div className="space-y-6">
              {[
                { name: 'Product Engine', status: 'Optimal', icon: CheckCircle2, color: 'text-foreground' },
                { name: 'Auth Gateway', status: 'Optimal', icon: CheckCircle2, color: 'text-foreground' },
                { name: 'Order Processing', status: 'Queueing', icon: Clock, color: 'text-muted-foreground' },
              ].map((s) => (
                <div key={s.name} className="flex items-center justify-between p-4 rounded-2xl bg-secondary/50 border border-card-border">
                  <div className="flex items-center gap-3">
                    <s.icon className={cn("w-4 h-4", s.color)} />
                    <span className="text-sm font-bold text-muted-foreground">{s.name}</span>
                  </div>
                  <span className={cn("text-[10px] font-black uppercase tracking-widest", s.color)}>
                    {s.status}
                  </span>
                </div>
              ))}
            </div>
          </div>

          <div className="p-8 rounded-3xl bg-foreground text-background shadow-lg shadow-foreground/5">
            <h2 className="text-lg font-bold uppercase font-display tracking-tight mb-4">Storage Capacity</h2>
            <p className="text-sm font-bold leading-relaxed mb-6">
              Your store is currently running at 84% capacity. Consider upgrading your tier.
            </p>
            <button className="w-full py-4 bg-background text-foreground rounded-2xl font-black uppercase tracking-widest text-xs hover:brightness-110 transition-colors">
              Upgrade Tier
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
