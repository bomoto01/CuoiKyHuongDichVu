import axiosClient from './axiosClient';

const bookApi = {
  getAll: () => axiosClient.get('/Sach'),
  getById: (maSach) => axiosClient.get(`/Sach/GetSachById?MaSach=${encodeURIComponent(maSach)}`),
  getByCategory: (maTheLoai) => axiosClient.get(`/Sach/GetSachByTheLoai?MaTheLoai=${encodeURIComponent(maTheLoai)}`),
  searchByName: (keyword) => axiosClient.get(`/Sach/SearchSachByName?keyword=${encodeURIComponent(keyword)}`),
  add: (data) => axiosClient.post('/Sach/AddSach', data),
  update: (data) => axiosClient.put('/Sach/UpdateSach', data),
  addMore: (maSach, soLuongThem) =>
    axiosClient.put(`/Sach/AddMoreSach?MaSach=${encodeURIComponent(maSach)}&SoLuongThem=${soLuongThem}`),
  removeSome: (maSach, soLuongXoa, username) =>
    axiosClient.put(
      `/Sach/RemoveSomeSach?MaSach=${encodeURIComponent(maSach)}&SoLuongXoa=${soLuongXoa}&username=${encodeURIComponent(username || '')}`
    ),
  delete: (maSach) => axiosClient.delete(`/Sach/DeleteSach?MaSach=${encodeURIComponent(maSach)}`),
};

export default bookApi;

