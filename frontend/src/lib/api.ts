import axios, { AxiosError, type AxiosInstance } from 'axios';
import type {
  AuthResponse,
  User,
  Category,
  Product,
  LoginFormValues,
  RegisterFormValues,
  CreateCategoryFormValues,
  CreateProductFormValues,
} from '@/types';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

class ApiClient {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_URL,
      headers: { 'Content-Type': 'application/json' },
    });

    // Request interceptor - attach JWT
    this.client.interceptors.request.use((config) => {
      if (typeof window !== 'undefined') {
        const token = localStorage.getItem('ecommerce_token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
      }
      return config;
    });

    // Response interceptor - handle 401
    this.client.interceptors.response.use(
      (response) => response,
      (error: AxiosError) => {
        if (error.response?.status === 401 && typeof window !== 'undefined') {
          localStorage.removeItem('ecommerce_token');
          localStorage.removeItem('ecommerce_user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // --- Auth ---
  async register(data: RegisterFormValues): Promise<{ message: string }> {
    const res = await this.client.post<{ message: string }>('/api/auth/register', data);
    return res.data;
  }

  async login(data: LoginFormValues): Promise<AuthResponse> {
    const res = await this.client.post<AuthResponse>('/api/auth/login', data);
    return res.data;
  }

  async getProfile(): Promise<User> {
    const res = await this.client.get<User>('/api/auth/profile');
    return res.data;
  }

  async logout(): Promise<void> {
    await this.client.post('/api/auth/logout');
  }

  // --- Categories ---
  async getCategories(): Promise<Category[]> {
    const res = await this.client.get<Category[]>('/api/categories');
    return res.data;
  }

  async getCategory(id: number): Promise<Category> {
    const res = await this.client.get<Category>(`/api/categories/${id}`);
    return res.data;
  }

  async createCategory(data: CreateCategoryFormValues): Promise<Category> {
    const res = await this.client.post<Category>('/api/categories', data);
    return res.data;
  }

  async updateCategory(id: number, data: Partial<Category>): Promise<Category> {
    const res = await this.client.put<Category>(`/api/categories/${id}`, data);
    return res.data;
  }

  async deleteCategory(id: number): Promise<void> {
    await this.client.delete(`/api/categories/${id}`);
  }

  // --- Products ---
  async getProducts(): Promise<Product[]> {
    const res = await this.client.get<Product[]>('/api/products');
    return res.data;
  }

  async getProduct(id: number): Promise<Product> {
    const res = await this.client.get<Product>(`/api/products/${id}`);
    return res.data;
  }

  async createProduct(data: CreateProductFormValues): Promise<Product> {
    const res = await this.client.post<Product>('/api/products', data);
    return res.data;
  }

  async updateProduct(id: number, data: Partial<Product>): Promise<Product> {
    const res = await this.client.put<Product>(`/api/products/${id}`, data);
    return res.data;
  }

  async deleteProduct(id: number): Promise<void> {
    await this.client.delete(`/api/products/${id}`);
  }
}

export const api = new ApiClient();

/** Extract error message from Axios errors */
export function getErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    return error.response?.data?.message || error.message || 'An error occurred';
  }
  if (error instanceof Error) return error.message;
  return 'An unexpected error occurred';
}
