'use client';

import { 
  Tags, 
  Plus, 
  MoreVertical, 
  Edit2, 
  Trash2, 
  ChevronRight,
  FolderTree,
  Image as ImageIcon
} from 'lucide-react';
import { cn } from '@/lib/utils';

const categories = [
  { id: 1, title: 'Electronics', count: 156, parent: null, image: 'https://images.unsplash.com/photo-1498049794561-7780e7231661?w=800&q=80' },
  { id: 2, title: 'Laptops', count: 42, parent: 'Electronics', image: null },
  { id: 3, title: 'Smartphones', count: 89, parent: 'Electronics', image: null },
  { id: 4, title: 'Furniture', count: 84, parent: null, image: 'https://images.unsplash.com/photo-1524758631624-e2822e304c36?w=800&q=80' },
  { id: 5, title: 'Living Room', count: 32, parent: 'Furniture', image: null },
  { id: 6, title: 'Kitchen', count: 12, parent: 'Furniture', image: null },
];

export default function CategoriesPage() {
  return (
    <div className="space-y-8 animate-fade-in">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight uppercase italic">Categories</h1>
          <p className="text-gray-400 mt-1">Organize your products into logical groups.</p>
        </div>
        <button className="btn-primary self-start sm:self-center">
          <Plus className="w-4 h-4" />
          Add Category
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Quick Add Form */}
        <div className="p-8 rounded-2xl bg-white/[0.01] border border-white/[0.06] backdrop-blur-xl h-fit">
          <h2 className="text-lg font-bold mb-6 flex items-center gap-2 uppercase tracking-tight">
            <Plus className="w-4 h-4 text-white" />
            Quick Add
          </h2>
          <form className="space-y-5">
            <div>
              <label className="block text-[10px] font-bold text-gray-500 uppercase tracking-widest mb-2">Name</label>
              <input 
                type="text" 
                placeholder="e.g. Smart Home"
                className="input-field py-2.5"
              />
            </div>
            <div>
              <label className="block text-[10px] font-bold text-gray-500 uppercase tracking-widest mb-2">Parent Category</label>
              <select className="w-full px-4 py-2.5 bg-white/[0.03] border border-white/[0.06] rounded-xl outline-none focus:border-white/30 text-gray-400 appearance-none">
                <option className="bg-surface">None</option>
                <option className="bg-surface">Electronics</option>
                <option className="bg-surface">Furniture</option>
              </select>
            </div>
            <div>
              <label className="block text-[10px] font-bold text-gray-500 uppercase tracking-widest mb-2">Image URL</label>
              <div className="flex gap-2">
                <input 
                  type="text" 
                  placeholder="https://..."
                  className="flex-1 px-4 py-2.5 bg-white/[0.03] border border-white/[0.06] rounded-xl outline-none focus:border-white/30"
                />
                <button type="button" className="p-2.5 bg-white/[0.03] border border-white/[0.06] rounded-xl hover:bg-white/[0.05] transition-colors">
                  <ImageIcon className="w-4 h-4 text-gray-400" />
                </button>
              </div>
            </div>
            <button type="submit" className="btn-primary w-full py-3 shadow-none">
              Create Category
            </button>
          </form>
        </div>

        {/* Categories List */}
        <div className="lg:col-span-2 space-y-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-bold flex items-center gap-2 uppercase tracking-tight">
              <FolderTree className="w-4 h-4 text-white" />
              Hierarchy
            </h2>
            <span className="text-[10px] font-bold text-gray-600 uppercase tracking-widest">{categories.length} Total</span>
          </div>

          <div className="space-y-4">
            {categories.map((cat) => (
              <div 
                key={cat.id}
                className={cn(
                  "flex items-center justify-between p-5 rounded-2xl border transition-all duration-500 group",
                  cat.parent 
                    ? "ml-10 bg-white/[0.01] border-white/[0.04] hover:border-white/10" 
                    : "bg-white/[0.02] border-white/[0.06] hover:border-white/20"
                )}
              >
                <div className="flex items-center gap-5">
                  {cat.image ? (
                    <div className="w-12 h-12 rounded-xl overflow-hidden border border-white/[0.08] bg-black">
                      <img src={cat.image} alt={cat.title} className="w-full h-full object-cover grayscale opacity-50 group-hover:opacity-100 group-hover:grayscale-0 transition-all duration-500" />
                    </div>
                  ) : (
                    <div className="w-12 h-12 rounded-xl bg-white/[0.03] border border-white/[0.06] flex items-center justify-center group-hover:bg-white/[0.05] transition-colors">
                      <Tags className="w-5 h-5 text-gray-600 group-hover:text-white transition-colors" />
                    </div>
                  )}
                  <div>
                    <h3 className="font-bold text-gray-200 group-hover:text-white transition-colors text-base">
                      {cat.title}
                    </h3>
                    <p className="text-[10px] uppercase tracking-widest text-gray-500 font-medium">{cat.count} products</p>
                  </div>
                </div>
                
                <div className="flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-all duration-300 transform translate-x-2 group-hover:translate-x-0">
                  <button className="p-2.5 hover:bg-white text-gray-500 hover:text-black rounded-xl transition-all duration-300">
                    <Edit2 className="w-4 h-4" />
                  </button>
                  <button className="p-2.5 hover:bg-red-500 text-gray-500 hover:text-white rounded-xl transition-all duration-300">
                    <Trash2 className="w-4 h-4" />
                  </button>
                  {!cat.parent && <ChevronRight className="w-4 h-4 text-gray-700" />}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
