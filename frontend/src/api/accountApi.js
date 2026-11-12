import axiosClient from './axiosClient';

const accountApi = {
  getAll: () => axiosClient.get('/TaiKhoan'),
  getByUsername: (username) =>
    axiosClient.get(`/TaiKhoan/GetTaiKhoanByUsername?username=${encodeURIComponent(username)}`),
  search: (keyword) => axiosClient.get(`/TaiKhoan/SearchTaiKhoan?keyword=${encodeURIComponent(keyword)}`),
  add: (data) => axiosClient.post('/TaiKhoan/AddTaiKhoan', data),
  update: (data) => axiosClient.put('/TaiKhoan/UpdateTaiKhoan', data),
  delete: (username) => axiosClient.delete(`/TaiKhoan/DeleteTaiKhoan?Username=${encodeURIComponent(username)}`),
};

export default accountApi;

