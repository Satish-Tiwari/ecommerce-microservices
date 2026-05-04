'use client';

import { useState } from 'react';
import {
  Package,
  Plus,
  Search,
  Filter,
  MoreVertical,
  Edit2,
  Trash2,
  ExternalLink,
  Loader2
} from 'lucide-react';
import { cn } from '@/lib/utils';

// Mock data for initial UI
const products = [
  {
    id: 1,
    title: 'Premium Wireless Headphones',
    sku: 'WHP-001',
    price: 299.00,
    stock: 45,
    category: 'Electronics',
    status: 'In Stock',
    image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80'
  },
  {
    id: 2,
    title: 'Ergonomic Office Chair',
    sku: 'CHR-042',
    price: 189.50,
    stock: 12,
    category: 'Furniture',
    status: 'Low Stock',
    image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80'
  },
  {
    id: 3,
    title: 'Mechanical Gaming Keyboard',
    sku: 'KBD-099',
    price: 129.99,
    stock: 0,
    category: 'Electronics',
    status: 'Out of Stock',
    image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80'
  }
];

export default function ProductsPage() {
  const [isLoading, setIsLoading] = useState(false);

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight uppercase italic">Products</h1>
          <p className="text-gray-400 mt-1">Manage your store inventory and catalog.</p>
        </div>
        <button className="btn-primary self-start sm:self-center">
          <Plus className="w-4 h-4" />
          Add Product
        </button>
      </div>

      {/* Filters & Search */}
      <div className="flex flex-col md:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
          <input
            type="text"
            placeholder="Search products..."
            className="w-full pl-10 pr-4 py-2.5 bg-white/[0.02] border border-white/[0.08] rounded-xl 
                       focus:border-white/30 focus:ring-1 focus:ring-white/10 transition-all outline-none"
          />
        </div>
        <div className="flex gap-2">
          <button className="btn-secondary !px-4 !py-2.5">
            <Filter className="w-4 h-4" />
            Filters
          </button>
          <select className="bg-white/[0.02] border border-white/[0.08] rounded-xl px-4 py-2.5 text-sm outline-none focus:border-white/30 text-gray-300">
            <option className="bg-surface">All Categories</option>
            <option className="bg-surface">Electronics</option>
            <option className="bg-surface">Furniture</option>
          </select>
        </div>
      </div>

      {/* Products Table */}
      <div className="rounded-2xl bg-white/[0.01] border border-white/[0.06] backdrop-blur-xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="text-[10px] uppercase tracking-widest text-gray-500 border-b border-white/[0.06] bg-white/[0.02]">
                <th className="px-6 py-5 font-bold">Product</th>
                <th className="px-6 py-5 font-bold">SKU</th>
                <th className="px-6 py-5 font-bold">Price</th>
                <th className="px-6 py-5 font-bold">Stock</th>
                <th className="px-6 py-5 font-bold">Status</th>
                <th className="px-6 py-5 font-bold text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-white/[0.04]">
              {isLoading ? (
                <tr>
                  <td colSpan={6} className="py-20 text-center">
                    <Loader2 className="w-8 h-8 text-white animate-spin mx-auto opacity-20" />
                  </td>
                </tr>
              ) : products.map((product) => (
                <tr key={product.id} className="group hover:bg-white/[0.02] transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 rounded-lg bg-black overflow-hidden border border-white/[0.08] group-hover:border-white/20 transition-colors">
                        <img src={product.image} alt={product.title} className="w-full h-full object-cover grayscale opacity-60 group-hover:opacity-100 group-hover:grayscale-0 transition-all duration-500" />
                      </div>
                      <div>
                        <p className="text-sm font-bold text-gray-200 group-hover:text-white transition-colors">
                          {product.title}
                        </p>
                        <p className="text-[10px] uppercase tracking-wider text-gray-500">{product.category}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-xs font-mono text-gray-400">{product.sku}</td>
                  <td className="px-6 py-4 text-sm font-bold text-white">${product.price.toFixed(2)}</td>
                  <td className="px-6 py-4 text-sm text-gray-400">{product.stock} <span className="text-[10px] text-gray-600 uppercase">units</span></td>
                  <td className="px-6 py-4">
                    <span className={cn(
                      "inline-flex items-center px-2.5 py-1 rounded-full text-[9px] font-black uppercase border tracking-tighter",
                      product.status === 'In Stock' ? "text-white bg-white/[0.05] border-white/20" :
                        product.status === 'Low Stock' ? "text-gray-400 bg-white/[0.02] border-white/[0.05]" :
                          "text-red-400 bg-red-950/20 border-red-900/30"
                    )}>
                      {product.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-1">
                      <button className="p-2 hover:bg-white text-gray-500 hover:text-black rounded-lg transition-all duration-300">
                        <Edit2 className="w-4 h-4" />
                      </button>
                      <button className="p-2 hover:bg-red-500 text-gray-500 hover:text-white rounded-lg transition-all duration-300">
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        <div className="px-6 py-5 border-t border-white/[0.06] flex items-center justify-between text-xs text-gray-500">
          <p>Showing <span className="text-gray-300 font-bold">1</span> to <span className="text-gray-300 font-bold">{products.length}</span> of <span className="text-gray-300 font-bold">120</span> products</p>
          <div className="flex gap-1.5">
            <button className="px-4 py-2 rounded-lg border border-white/[0.06] hover:bg-white/[0.03] disabled:opacity-30 transition-colors" disabled>Previous</button>
            <button className="px-4 py-2 rounded-lg bg-white text-black font-bold">1</button>
            <button className="px-4 py-2 rounded-lg border border-white/[0.06] hover:bg-white/[0.03] transition-colors">2</button>
            <button className="px-4 py-2 rounded-lg border border-white/[0.06] hover:bg-white/[0.03] transition-colors">Next</button>
          </div>
        </div>
      </div>
    </div>
  );
}
