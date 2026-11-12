import axiosClient from './axiosClient';

const transactionApi = {
  getAll: () => axiosClient.get('/LiSuGiaoDich'),
  getByUsername: (username) =>
    axiosClient.get(`/LiSuGiaoDich/GetLSuGiaoDichByUsername?Username=${encodeURIComponent(username)}`),
  getByMaSach: (maSach) =>
    axiosClient.get(`/LiSuGiaoDich/GetLSuGiaoDichByMaSach?MaSach=${encodeURIComponent(maSach)}`),
  getByDateRange: (start, end) =>
    axiosClient.get(`/LiSuGiaoDich/GetLSuGiaoDichByNgayGD?NgayGDDau=${start || ''}&NgayGDCuoi=${end || ''}`),
  getByStatus: (trangThai) =>
    axiosClient.get(`/LiSuGiaoDich/GetLSuGiaoDichByTrangThai?TrangThai=${encodeURIComponent(trangThai)}`),
  deleteOlderThan3Years: () => axiosClient.delete('/LiSuGiaoDich'),
};

export default transactionApi;

