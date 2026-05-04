// -----------------------------------------------
// Domain Types
// -----------------------------------------------

export interface User {
  id: number;
  username: string;
  email: string;
  fullname: string;
  gender?: string;
  phone?: string;
  avatar?: string;
  roles?: string[];
}

export interface AuthResponse {
  access_token: string;
  refresh_token: string;
  user_info: User;
}

export interface Category {
  categoryId: number;
  categoryTitle: string;
  imageUrl?: string;
  subCategories?: Category[];
  parentCategory?: Category;
}

export interface Product {
  productId: number;
  productTitle: string;
  imageUrl?: string;
  sku: string;
  priceUnit: number;
  quantity: number;
  category?: Category;
}

export interface ApiError {
  status: number;
  message: string;
  timestamp: string;
}

// -----------------------------------------------
// Form Schemas (used with React Hook Form + Zod)
// -----------------------------------------------

export interface LoginFormValues {
  username: string; // backend uses username for login
  password: string;
}

export interface RegisterFormValues {
  fullname: string;
  username: string;
  email: string;
  password: string;
  gender: string;
  phone: string;
}

export interface CreateCategoryFormValues {
  categoryTitle: string;
  imageUrl?: string;
  parentCategoryId?: number;
}

export interface CreateProductFormValues {
  productTitle: string;
  imageUrl?: string;
  sku: string;
  priceUnit: number;
  quantity: number;
  categoryId: number;
}
