'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useState, useEffect } from 'react';
import { 
  LayoutDashboard, 
  Package, 
  Tags, 
  ShoppingCart, 
  Users, 
  Settings,
  ChevronDown,
  ChevronRight,
  BarChart3,
  Megaphone,
  CreditCard,
  Truck,
  Plus
} from 'lucide-react';
import { cn } from '@/lib/utils';

interface SubMenuItem {
  title: string;
  href: string;
}

interface MenuItem {
  title: string;
  href?: string;
  icon: any;
  subItems?: SubMenuItem[];
}

const menuGroups: { group?: string; items: MenuItem[] }[] = [
  {
    items: [
      {
        title: 'Overview',
        href: '/dashboard',
        icon: LayoutDashboard,
      },
    ]
  },
  {
    group: 'Catalog',
    items: [
      {
        title: 'Products',
        icon: Package,
        subItems: [
          { title: 'All Products', href: '/dashboard/products' },
          { title: 'Add New', href: '/dashboard/products/new' },
          { title: 'Categories', href: '/dashboard/categories' },
        ]
      },
      {
        title: 'Inventory',
        href: '/dashboard/inventory',
        icon: Tags,
      }
    ]
  },
  {
    group: 'Operations',
    items: [
      {
        title: 'Orders',
        href: '/dashboard/orders',
        icon: ShoppingCart,
        subItems: [
          { title: 'Processing', href: '/dashboard/orders/processing' },
          { title: 'Completed', href: '/dashboard/orders/completed' },
        ]
      },
      {
        title: 'Customers',
        href: '/dashboard/customers',
        icon: Users,
      },
    ]
  },
  {
    group: 'Insights',
    items: [
      {
        title: 'Revenue',
        icon: CreditCard,
        href: '/dashboard/analytics',
      },
      {
        title: 'Reports',
        icon: BarChart3,
        href: '/dashboard/reports',
      },
    ]
  }
];

export function Sidebar() {
  const pathname = usePathname();
  const [expandedItems, setExpandedItems] = useState<string[]>([]);

  useEffect(() => {
    const activeParent = menuGroups.flatMap(g => g.items).find(item => 
      item.subItems?.some(sub => pathname === sub.href)
    );
    if (activeParent && !expandedItems.includes(activeParent.title)) {
      setExpandedItems(prev => [...prev, activeParent.title]);
    }
  }, [pathname]);

  const toggleExpand = (title: string) => {
    setExpandedItems(prev => 
      prev.includes(title) ? prev.filter(t => t !== title) : [...prev, title]
    );
  };

  return (
    <aside className="w-64 border-r border-card-border bg-background hidden md:flex flex-col h-[calc(100vh-64px)] sticky top-16 z-40">
      <div className="flex-1 py-6 space-y-8 overflow-y-auto custom-scrollbar px-4">
        {menuGroups.map((group, gIdx) => (
          <div key={gIdx}>
            {group.group && (
              <p className="px-4 text-[10px] font-bold uppercase tracking-[0.2em] text-muted-foreground/50 mb-3 font-display">
                {group.group}
              </p>
            )}
            <div className="space-y-1">
              {group.items.map((item) => {
                const isExpanded = expandedItems.includes(item.title);
                const hasSubItems = item.subItems && item.subItems.length > 0;
                const isActive = item.href === pathname || 
                               (hasSubItems && item.subItems?.some(sub => pathname === sub.href));
                const Icon = item.icon;

                return (
                  <div key={item.title}>
                    {item.href && !hasSubItems ? (
                      <Link
                        href={item.href}
                        className={cn(
                          "flex items-center gap-3 px-4 py-2.5 rounded-xl transition-all duration-300 group",
                          isActive 
                            ? "bg-foreground text-background shadow-lg shadow-foreground/10" 
                            : "text-muted-foreground hover:text-foreground hover:bg-secondary"
                        )}
                      >
                        <Icon className={cn("w-4 h-4", isActive ? "text-background" : "text-muted-foreground group-hover:text-foreground")} />
                        <span className="text-sm font-bold tracking-tight">{item.title}</span>
                      </Link>
                    ) : (
                      <button
                        onClick={() => toggleExpand(item.title)}
                        className={cn(
                          "w-full flex items-center justify-between px-4 py-2.5 rounded-xl transition-all duration-300 group",
                          isActive ? "text-foreground" : "text-muted-foreground hover:text-foreground"
                        )}
                      >
                        <div className="flex items-center gap-3">
                          <Icon className="w-4 h-4" />
                          <span className="text-sm font-bold tracking-tight">{item.title}</span>
                        </div>
                        {isExpanded ? <ChevronDown className="w-3 h-3" /> : <ChevronRight className="w-3 h-3 opacity-30" />}
                      </button>
                    )}

                    {hasSubItems && isExpanded && (
                      <div className="mt-1 ml-6 pl-4 border-l border-card-border space-y-1">
                        {item.subItems?.map((sub) => {
                          const isSubActive = pathname === sub.href;
                          return (
                            <Link
                              key={sub.href}
                              href={sub.href}
                              className={cn(
                                "flex items-center px-4 py-2 text-[13px] rounded-lg transition-all duration-200",
                                isSubActive 
                                  ? "text-foreground font-bold bg-secondary" 
                                  : "text-muted-foreground hover:text-foreground"
                              )}
                            >
                              {sub.title}
                            </Link>
                          );
                        })}
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        ))}
      </div>

      <div className="p-6 border-t border-card-border">
        <div className="flex items-center gap-3 bg-secondary p-3 rounded-2xl border border-card-border">
          <div className="w-8 h-8 rounded-full bg-foreground flex items-center justify-center text-[10px] font-black text-background">
            E
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-xs font-bold text-foreground truncate">Ecomm Admin</p>
            <p className="text-[10px] text-muted-foreground uppercase tracking-widest font-medium">Enterprise</p>
          </div>
          <Settings className="w-4 h-4 text-muted-foreground hover:text-foreground cursor-pointer transition-colors" />
        </div>
      </div>
    </aside>
  );
}
