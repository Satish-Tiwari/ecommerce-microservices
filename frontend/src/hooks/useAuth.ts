import { useMutation, useQuery } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import toast from 'react-hot-toast';
import { api, getErrorMessage } from '@/lib/api';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { setCredentials, logout as logoutAction } from '@/store/slices/authSlice';
import type { LoginFormValues, RegisterFormValues, User } from '@/types';

export function useLogin() {
  const dispatch = useAppDispatch();
  const router = useRouter();

  return useMutation({
    mutationFn: (data: LoginFormValues) => api.login(data),
    onSuccess: (data) => {
      dispatch(setCredentials({ 
        user: data.user_info, 
        token: data.access_token 
      }));
      toast.success(`Welcome back, ${data.user_info.fullname}! 👋`);
      router.push('/dashboard');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

export function useRegister() {
  const router = useRouter();

  return useMutation({
    mutationFn: (data: RegisterFormValues) => api.register(data),
    onSuccess: (data) => {
      toast.success(data.message || 'Account created! Please sign in. 🎉');
      router.push('/login');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

export function useProfile() {
  const dispatch = useAppDispatch();
  const { isAuthenticated, token } = useAppSelector((s) => s.auth);

  return useQuery({
    queryKey: ['profile'],
    queryFn: () => api.getProfile(),
    enabled: isAuthenticated && !!token,
    onSuccess: (user) => {
      // Keep user info in sync
      dispatch(setCredentials({ user, token: token! }));
    },
  });
}

export function useLogout() {
  const dispatch = useAppDispatch();
  const router = useRouter();

  const logoutMutation = useMutation({
    mutationFn: () => api.logout(),
    onSettled: () => {
      dispatch(logoutAction());
      router.push('/login');
      toast.success('Logged out successfully');
    },
  });

  return () => logoutMutation.mutate();
}
