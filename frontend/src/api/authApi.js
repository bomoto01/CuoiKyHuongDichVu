import axiosClient from './axiosClient';

const authApi = {
  login: (data) => axiosClient.post('/TaiKhoan/Login', data),
  getMyInfo: () => axiosClient.get('/TaiKhoan/my-info'),
};

export default authApi;
