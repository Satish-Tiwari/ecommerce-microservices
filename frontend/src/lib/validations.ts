import { z } from 'zod';

export const loginSchema = z.object({
  username: z
    .string()
    .min(1, 'Username is required')
    .min(6, 'Username must be at least 6 characters'),
  password: z
    .string()
    .min(1, 'Password is required')
    .min(8, 'Password must be at least 8 characters'),
});

export const registerSchema = z.object({
  fullname: z
    .string()
    .min(6, 'Full name must be at least 6 characters')
    .max(50, 'Full name must be less than 50 characters'),
  username: z
    .string()
    .min(6, 'Username must be at least 6 characters')
    .max(50, 'Username must be less than 50 characters'),
  email: z
    .string()
    .email('Invalid email address')
    .max(50, 'Email must be less than 50 characters'),
  password: z
    .string()
    .min(8, 'Password must be at least 8 characters')
    .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/, 'Password must contain uppercase, lowercase and numbers'),
  gender: z.string().min(1, 'Gender is required'),
  phone: z
    .string()
    .regex(/^(\+84[0-9]{9,10}|0[0-9]{9,10})$/, 'Invalid phone number format'),
  avatar: z.string().url('Invalid avatar URL').optional().or(z.literal('')),
});

export type LoginSchema = z.infer<typeof loginSchema>;
export type RegisterSchema = z.infer<typeof registerSchema>;
