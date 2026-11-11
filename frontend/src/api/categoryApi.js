import axiosClient from './axiosClient';

const categoryApi = {
  getAll: () => axiosClient.get('/TheLoai'),
  getById: (maTheLoai) => axiosClient.get(`/TheLoai/GetTheLoaiById?MaTheLoai=${encodeURIComponent(maTheLoai)}`),
  searchByName: (keyword) => axiosClient.get(`/TheLoai/SearchTheLoaiByName?keyword=${encodeURIComponent(keyword)}`),
  add: (data) => axiosClient.post('/TheLoai/AddTheLoai', data),
  update: (data) => axiosClient.put('/TheLoai/UpdateTheLoai', data),
  delete: (maTheLoai) => axiosClient.delete(`/TheLoai/DeleteTheLoai?MaTheLoai=${encodeURIComponent(maTheLoai)}`),
};

export default categoryApi;
