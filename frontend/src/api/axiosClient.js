import axios from 'axios';

const axiosClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

axiosClient.interceptors.response.use(
  (response) => {
    // If backend returns ApiResponse<T>, response.data has { code, message, result }
    if (response.data && response.data.result !== undefined) {
      return response.data.result;
    }
    return response.data;
  },
  (error) => {
    const errorData = error.response?.data;
    const message = errorData?.message || error.message || 'Có lỗi xảy ra, vui lòng thử lại';
    
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    
    return Promise.reject(new Error(message));
  }
);

export default axiosClient;

