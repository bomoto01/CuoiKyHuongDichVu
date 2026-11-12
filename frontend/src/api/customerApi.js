import axiosClient from './axiosClient';

const customerApi = {
  getAll: () => axiosClient.get('/KhachHang'),
  getById: (maKH) => axiosClient.get(`/KhachHang/GetKhachHangById?MaKH=${encodeURIComponent(maKH)}`),
  searchByName: (keyword) => axiosClient.get(`/KhachHang/SearchKhachHangByName?keyword=${encodeURIComponent(keyword)}`),
  searchBySDT: (sdt) => axiosClient.get(`/KhachHang/SearchKhachHangBySDT?sdt=${encodeURIComponent(sdt)}`),
  add: (data) => axiosClient.post('/KhachHang/AddKhachHang', data),
  update: (data) => axiosClient.put('/KhachHang/UpdateKhachHang', data),
  delete: (maKH) => axiosClient.delete(`/KhachHang/DeleteKhachHang?MaKH=${encodeURIComponent(maKH)}`),
};

export default customerApi;

