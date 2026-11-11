import axiosClient from './axiosClient';

const borrowApi = {
  getAll: () => axiosClient.get('/Muon'),
  getByMaKH: (maKH) => axiosClient.get(`/Muon/GetMuonByMaKH?MaKH=${encodeURIComponent(maKH)}`),
  getByMaMuon: (maMuon) => axiosClient.get(`/Muon/GetMuonByMaMuon?MaMuon=${encodeURIComponent(maMuon)}`),
  getByMaSach: (maSach) => axiosClient.get(`/Muon/GetMuonByMaSach?MaSach=${encodeURIComponent(maSach)}`),
  getByNgayMuon: (date) => axiosClient.get(`/Muon/GetMuonByNgayMuon?NgayMuon=${encodeURIComponent(date)}`),
  getOverdue: () => axiosClient.get('/Muon/GetOverdue'),
  add: (data) => axiosClient.post('/Muon/AddMuon', data),
  traSach: (data) => axiosClient.put('/Muon/TraSach', data),
  matSach: (data) => axiosClient.put('/Muon/MatSach', data),
};

export default borrowApi;
