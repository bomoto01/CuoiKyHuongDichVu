import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { Navbar } from '../components/Navbar';
import { Toast } from '../components/Toast';
import { Modal } from '../components/Modal';
import bookApi from '../api/bookApi';
import categoryApi from '../api/categoryApi';
import accountApi from '../api/accountApi';
import transactionApi from '../api/transactionApi';
import {
  BookOpen,
  FolderTree,
  Users,
  History,
  Shield,
  Plus,
  Edit2,
  Trash2,
  Search,
  PlusCircle,
  MinusCircle,
  Calendar,
  Filter,
} from 'lucide-react';

export const AdminDashboard = () => {
  const { user, logout } = useAuth();
  const [currentTab, setCurrentTab] = useState('sach');
  const [toast, setToast] = useState({ message: '', type: 'success' });

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast({ message: '', type: 'success' }), 4000);
  };

  const tabs = [
    { id: 'sach', label: '1. Quản Lý Sách', icon: BookOpen },
    { id: 'the-loai', label: '2. Quản Lý Thể Loại', icon: FolderTree },
    { id: 'tai-khoan', label: '3. Quản Lý Tài Khoản', icon: Users },
    { id: 'truy-van', label: '4. Lịch Sử Giao Dịch', icon: History },
    { id: 'profile', label: '5. Thông Tin Admin', icon: Shield },
  ];

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <Navbar currentTab={currentTab} onSelectTab={setCurrentTab} tabs={tabs} />

      <main className="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 lg:p-8">
        {currentTab === 'sach' && <TabQuanLySach showToast={showToast} user={user} />}
        {currentTab === 'the-loai' && <TabQuanLyTheLoai showToast={showToast} />}
        {currentTab === 'tai-khoan' && <TabQuanLyTaiKhoan showToast={showToast} />}
        {currentTab === 'truy-van' && <TabLichSuGiaoDich showToast={showToast} />}
        {currentTab === 'profile' && <TabThongTinAdmin user={user} logout={logout} />}
      </main>

      <Toast message={toast.message} type={toast.type} onClose={() => setToast({ message: '', type: 'success' })} />
    </div>
  );
};

// ==========================================
// 1. TAB QUẢN LÝ SÁCH (Mục 2.2.1 & 3.7.3.1)
// ==========================================
const TabQuanLySach = ({ showToast, user }) => {
  const [books, setBooks] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [searchCategory, setSearchCategory] = useState('');

  // Form State
  const [formData, setFormData] = useState({
    maSach: '',
    tenSach: '',
    tacGia: '',
    soLuong: 1,
    maTheLoai: '',
  });
  const [isEditing, setIsEditing] = useState(false);

  // Modals for stock adjustment (AddMoreSach & RemoveSomeSach)
  const [adjustModal, setAdjustModal] = useState({ open: false, type: 'add', book: null, qty: 1 });

  const loadData = async () => {
    try {
      setLoading(true);
      const [bkList, catList] = await Promise.allSettled([bookApi.getAll(), categoryApi.getAll()]);
      if (bkList.status === 'fulfilled') {
        setBooks(bkList.value || []);
      } else {
        showToast('Lỗi tải danh sách sách: ' + (bkList.reason?.message || 'Không thể lấy dữ liệu'), 'error');
      }
      if (catList.status === 'fulfilled') {
        setCategories(catList.value || []);
      } else {
        showToast('Lỗi tải thể loại: ' + (catList.reason?.message || 'Không thể lấy dữ liệu'), 'error');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleReset = () => {
    setFormData({ maSach: '', tenSach: '', tacGia: '', soLuong: 1, maTheLoai: '' });
    setIsEditing(false);
  };

  const handleSelectRow = (b) => {
    setFormData({
      maSach: b.maSach,
      tenSach: b.tenSach,
      tacGia: b.tacGia || '',
      soLuong: b.soLuong,
      maTheLoai: b.maTheLoai || '',
    });
    setIsEditing(true);
  };

  const handleSearch = async () => {
    if (!keyword.trim() && !searchCategory) {
      loadData();
      return;
    }
    try {
      setLoading(true);
      let list = [];
      if (searchCategory) {
        list = await bookApi.getByCategory(searchCategory);
      } else {
        list = await bookApi.searchByName(keyword.trim());
      }
      setBooks(list || []);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async (e) => {
    e?.preventDefault();
    if (!formData.maSach.trim() || !formData.tenSach.trim() || !formData.maTheLoai) {
      showToast('Vui lòng nhập Mã sách, Tên sách và Thể loại', 'error');
      return;
    }

    try {
      const payload = {
        ...formData,
        soLuong: parseInt(formData.soLuong, 10),
      };

      if (isEditing) {
        await bookApi.update(payload);
        showToast('Cập nhật sách thành công!');
      } else {
        await bookApi.add(payload);
        showToast('Thêm sách mới thành công!');
      }
      handleReset();
      loadData();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  const handleDelete = async () => {
    if (!formData.maSach) {
      showToast('Vui lòng chọn 1 cuốn sách để xóa', 'error');
      return;
    }
    if (!window.confirm(`Xác nhận xóa hoàn toàn sách "${formData.tenSach}" (${formData.maSach})?`)) return;

    try {
      await bookApi.delete(formData.maSach);
      showToast('Xóa đầu sách thành công!');
      handleReset();
      loadData();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  const handleAdjustStock = async () => {
    const { type, book, qty } = adjustModal;
    if (!book || qty <= 0) return;

    try {
      if (type === 'add') {
        await bookApi.addMore(book.maSach, qty);
        showToast(`Đã nhập thêm ${qty} cuốn cho sách "${book.tenSach}"!`);
      } else {
        await bookApi.removeSome(book.maSach, qty, user?.username || 'admin');
        showToast(`Đã hủy/loại bỏ ${qty} cuốn sách "${book.tenSach}"!`);
      }
      setAdjustModal({ open: false, type: 'add', book: null, qty: 1 });
      loadData();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6 space-y-6">
      <div className="pb-4 border-b border-slate-100 flex justify-between items-center">
        <div>
          <h2 className="text-xl font-bold text-slate-800">Quản Lý Danh Mục Sách</h2>
          <p className="text-sm text-slate-500">Thêm, sửa, xóa, tìm kiếm và điều chỉnh số lượng tồn kho sách</p>
        </div>
      </div>

      {/* Form Input (3.7.3.1) */}
      <form onSubmit={handleSave} className="bg-slate-50 p-5 rounded-2xl border border-slate-200 space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3">
          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Mã Sách (*)</label>
            <input
              type="text"
              value={formData.maSach}
              disabled={isEditing}
              onChange={(e) => setFormData({ ...formData, maSach: e.target.value })}
              placeholder="VD: S001"
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-mono font-bold disabled:bg-slate-100"
            />
          </div>

          <div className="lg:col-span-2">
            <label className="block text-xs font-bold text-slate-600 mb-1">Tên Sách (*)</label>
            <input
              type="text"
              value={formData.tenSach}
              onChange={(e) => setFormData({ ...formData, tenSach: e.target.value })}
              placeholder="Tên đầu sách..."
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Tác Giả</label>
            <input
              type="text"
              value={formData.tacGia}
              onChange={(e) => setFormData({ ...formData, tacGia: e.target.value })}
              placeholder="Tên tác giả..."
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Thể Loại (*)</label>
            <select
              value={formData.maTheLoai}
              onChange={(e) => setFormData({ ...formData, maTheLoai: e.target.value })}
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            >
              <option value="">-- Chọn thể loại --</option>
              {categories.map((c) => (
                <option key={c.maTheLoai} value={c.maTheLoai}>
                  {c.tenTheLoai}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div>
          <label className="block text-xs font-bold text-slate-600 mb-1">Số Lượng Trong Kho</label>
          <input
            type="number"
            min="0"
            value={formData.soLuong}
            onChange={(e) => setFormData({ ...formData, soLuong: parseInt(e.target.value, 10) || 0 })}
            className="w-full sm:w-48 px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-bold"
          />
        </div>

        {/* Buttons */}
        <div className="flex flex-wrap items-center gap-2 pt-2 border-t border-slate-200/80">
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-bold shadow transition flex items-center gap-1.5"
          >
            {isEditing ? <Edit2 className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
            <span>{isEditing ? 'Sửa Sách' : 'Thêm Sách'}</span>
          </button>

          {isEditing && (
            <button
              type="button"
              onClick={handleDelete}
              className="px-4 py-2 bg-rose-600 hover:bg-rose-700 text-white rounded-xl text-xs font-bold shadow transition flex items-center gap-1.5"
            >
              <Trash2 className="w-4 h-4" />
              <span>Xóa Sách</span>
            </button>
          )}

          <button
            type="button"
            onClick={handleReset}
            className="px-4 py-2 bg-white border border-slate-300 hover:bg-slate-100 text-slate-700 rounded-xl text-xs font-semibold transition"
          >
            Làm Mới
          </button>
        </div>
      </form>

      {/* Filter / Search Bar */}
      <div className="flex flex-col sm:flex-row items-center gap-3">
        <div className="w-full sm:w-48">
          <select
            value={searchCategory}
            onChange={(e) => setSearchCategory(e.target.value)}
            className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-xs font-medium"
          >
            <option value="">Lọc theo thể loại...</option>
            {categories.map((c) => (
              <option key={c.maTheLoai} value={c.maTheLoai}>
                {c.tenTheLoai}
              </option>
            ))}
          </select>
        </div>

        <div className="relative flex-1 w-full">
          <input
            type="text"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
            placeholder="Tìm theo tên sách..."
            className="w-full pl-9 pr-4 py-2 border border-slate-300 rounded-lg text-xs"
          />
          <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
        </div>

        <button
          onClick={handleSearch}
          className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-xs font-bold shrink-0 shadow"
        >
          Tìm Kiếm
        </button>
        <button
          onClick={() => {
            setKeyword('');
            setSearchCategory('');
            loadData();
          }}
          className="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-lg text-xs font-semibold shrink-0"
        >
          Hiển thị tất cả
        </button>
      </div>

      {/* Books Table */}
      <div className="overflow-x-auto border border-slate-200 rounded-xl">
        <table className="w-full text-left text-xs">
          <thead className="bg-slate-100 text-slate-700 font-bold uppercase tracking-wider">
            <tr>
              <th className="py-3 px-3">Mã Sách</th>
              <th className="py-3 px-3">Tên Sách</th>
              <th className="py-3 px-3">Tác giả</th>
              <th className="py-3 px-3">Thể loại</th>
              <th className="py-3 px-3 text-center">Số lượng</th>
              <th className="py-3 px-3 text-center">Điều chỉnh kho</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {loading ? (
              <tr>
                <td colSpan="6" className="text-center py-6 text-slate-400">
                  Đang tải...
                </td>
              </tr>
            ) : books.length === 0 ? (
              <tr>
                <td colSpan="6" className="text-center py-6 text-slate-400">
                  Không có đầu sách nào
                </td>
              </tr>
            ) : (
              books.map((b) => (
                <tr
                  key={b.maSach}
                  onClick={() => handleSelectRow(b)}
                  className="cursor-pointer hover:bg-slate-50 transition"
                >
                  <td className="py-3 px-3 font-mono font-bold text-blue-700">{b.maSach}</td>
                  <td className="py-3 px-3 font-medium text-slate-900">{b.tenSach}</td>
                  <td className="py-3 px-3 text-slate-600">{b.tacGia || '—'}</td>
                  <td className="py-3 px-3 text-slate-600">{b.tenTheLoai}</td>
                  <td className="py-3 px-3 text-center">
                    <span className="px-2.5 py-0.5 rounded font-bold bg-blue-100 text-blue-800">
                      {b.soLuong}
                    </span>
                  </td>
                  <td className="py-3 px-3 text-center">
                    <div className="flex items-center justify-center gap-1.5" onClick={(e) => e.stopPropagation()}>
                      <button
                        title="Bổ sung thêm số lượng sách"
                        onClick={() => setAdjustModal({ open: true, type: 'add', book: b, qty: 5 })}
                        className="p-1.5 rounded-lg bg-emerald-50 hover:bg-emerald-100 text-emerald-700 border border-emerald-200 transition"
                      >
                        <PlusCircle className="w-4 h-4" />
                      </button>
                      <button
                        title="Hủy bớt sách hỏng / loại bỏ sách"
                        onClick={() => setAdjustModal({ open: true, type: 'remove', book: b, qty: 1 })}
                        className="p-1.5 rounded-lg bg-amber-50 hover:bg-amber-100 text-amber-700 border border-amber-200 transition"
                      >
                        <MinusCircle className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Adjust Stock Modal */}
      <Modal
        isOpen={adjustModal.open}
        onClose={() => setAdjustModal({ ...adjustModal, open: false })}
        title={adjustModal.type === 'add' ? 'Nhập thêm số lượng sách' : 'Hủy bớt số lượng sách'}
      >
        {adjustModal.book && (
          <div className="space-y-4">
            <div className="p-3 bg-slate-50 rounded-xl border border-slate-200 text-xs">
              <p className="font-bold text-slate-800">{adjustModal.book.tenSach}</p>
              <p className="text-slate-500">Mã: {adjustModal.book.maSach} — Hiện còn: {adjustModal.book.soLuong} cuốn</p>
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1">
                {adjustModal.type === 'add' ? 'Số lượng nhập thêm:' : 'Số lượng hủy bớt:'}
              </label>
              <input
                type="number"
                min="1"
                max={adjustModal.type === 'remove' ? adjustModal.book.soLuong : 9999}
                value={adjustModal.qty}
                onChange={(e) =>
                  setAdjustModal({ ...adjustModal, qty: parseInt(e.target.value, 10) || 1 })
                }
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm font-bold"
              />
            </div>

            <button
              onClick={handleAdjustStock}
              className={`w-full py-2.5 rounded-xl text-white font-bold text-sm shadow transition ${
                adjustModal.type === 'add' ? 'bg-emerald-600 hover:bg-emerald-700' : 'bg-amber-600 hover:bg-amber-700'
              }`}
            >
              Xác nhận thực hiện
            </button>
          </div>
        )}
      </Modal>
    </div>
  );
};

// ==========================================
// 2. TAB QUẢN LÝ THỂ LOẠI (Mục 2.2.2 & 3.7.3.2)
// ==========================================
const TabQuanLyTheLoai = ({ showToast }) => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [formData, setFormData] = useState({ maTheLoai: '', tenTheLoai: '' });
  const [isEditing, setIsEditing] = useState(false);

  const fetchAll = async () => {
    try {
      setLoading(true);
      const data = await categoryApi.getAll();
      setCategories(data || []);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  const handleReset = () => {
    setFormData({ maTheLoai: '', tenTheLoai: '' });
    setIsEditing(false);
  };

  const handleSelectRow = (c) => {
    setFormData({ maTheLoai: c.maTheLoai, tenTheLoai: c.tenTheLoai });
    setIsEditing(true);
  };

  const handleSearch = async () => {
    if (!keyword.trim()) {
      fetchAll();
      return;
    }
    try {
      setLoading(true);
      const data = await categoryApi.searchByName(keyword.trim());
      setCategories(data || []);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async (e) => {
    e?.preventDefault();
    if (!formData.maTheLoai.trim() || !formData.tenTheLoai.trim()) {
      showToast('Vui lòng nhập đầy đủ mã và tên thể loại', 'error');
      return;
    }

    try {
      if (isEditing) {
        await categoryApi.update(formData);
        showToast('Cập nhật thể loại thành công!');
      } else {
        await categoryApi.add(formData);
        showToast('Thêm thể loại mới thành công!');
      }
      handleReset();
      fetchAll();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  const handleDelete = async () => {
    if (!formData.maTheLoai) {
      showToast('Vui lòng chọn 1 thể loại để xóa', 'error');
      return;
    }
    if (!window.confirm(`Xác nhận xóa thể loại "${formData.tenTheLoai}"?`)) return;

    try {
      await categoryApi.delete(formData.maTheLoai);
      showToast('Xóa thể loại thành công!');
      handleReset();
      fetchAll();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6 space-y-6">
      <div className="pb-4 border-b border-slate-100">
        <h2 className="text-xl font-bold text-slate-800">Quản Lý Thể Loại Sách</h2>
        <p className="text-sm text-slate-500">Phân loại các đầu sách theo chuyên ngành và chủ đề</p>
      </div>

      {/* Form */}
      <form onSubmit={handleSave} className="bg-slate-50 p-5 rounded-2xl border border-slate-200 space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Mã Thể Loại (*)</label>
            <input
              type="text"
              value={formData.maTheLoai}
              disabled={isEditing}
              onChange={(e) => setFormData({ ...formData, maTheLoai: e.target.value })}
              placeholder="VD: TL001"
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-mono font-bold disabled:bg-slate-100"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Tên Thể Loại (*)</label>
            <input
              type="text"
              value={formData.tenTheLoai}
              onChange={(e) => setFormData({ ...formData, tenTheLoai: e.target.value })}
              placeholder="VD: Khoa học, Văn học, Công nghệ..."
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>
        </div>

        <div className="flex flex-wrap items-center gap-2 pt-2 border-t border-slate-200/80">
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-bold shadow transition flex items-center gap-1.5"
          >
            {isEditing ? <Edit2 className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
            <span>{isEditing ? 'Sửa Thể Loại' : 'Thêm Thể Loại'}</span>
          </button>

          {isEditing && (
            <button
              type="button"
              onClick={handleDelete}
              className="px-4 py-2 bg-rose-600 hover:bg-rose-700 text-white rounded-xl text-xs font-bold shadow transition flex items-center gap-1.5"
            >
              <Trash2 className="w-4 h-4" />
              <span>Xóa Thể Loại</span>
            </button>
          )}

          <button
            type="button"
            onClick={handleReset}
            className="px-4 py-2 bg-white border border-slate-300 hover:bg-slate-100 text-slate-700 rounded-xl text-xs font-semibold transition"
          >
            Làm Mới
          </button>
        </div>
      </form>

      {/* Search */}
      <div className="flex items-center gap-2 max-w-md">
        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
          placeholder="Tìm tên thể loại..."
          className="w-full px-3 py-2 border border-slate-300 rounded-lg text-xs"
        />
        <button
          onClick={handleSearch}
          className="px-3.5 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-xs font-bold shrink-0"
        >
          Tìm
        </button>
        <button
          onClick={() => {
            setKeyword('');
            fetchAll();
          }}
          className="px-3.5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-lg text-xs font-semibold shrink-0"
        >
          Hiển thị
        </button>
      </div>

      {/* Table */}
      <div className="overflow-x-auto border border-slate-200 rounded-xl">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-100 text-slate-700 text-xs font-bold uppercase tracking-wider">
            <tr>
              <th className="py-3 px-4">Mã thể loại</th>
              <th className="py-3 px-4">Tên thể loại</th>
              <th className="py-3 px-4 text-center">Thao tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {loading ? (
              <tr>
                <td colSpan="3" className="text-center py-6 text-slate-400">
                  Đang tải...
                </td>
              </tr>
            ) : categories.length === 0 ? (
              <tr>
                <td colSpan="3" className="text-center py-6 text-slate-400">
                  Chưa có thể loại nào
                </td>
              </tr>
            ) : (
              categories.map((c) => (
                <tr
                  key={c.maTheLoai}
                  onClick={() => handleSelectRow(c)}
                  className="cursor-pointer hover:bg-slate-50 transition"
                >
                  <td className="py-3 px-4 font-mono font-bold text-blue-700">{c.maTheLoai}</td>
                  <td className="py-3 px-4 text-slate-900 font-medium">{c.tenTheLoai}</td>
                  <td className="py-3 px-4 text-center">
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleSelectRow(c);
                      }}
                      className="px-2.5 py-1 rounded text-xs font-semibold bg-slate-100 hover:bg-blue-100 text-slate-700 hover:text-blue-700 border border-slate-300 transition"
                    >
                      Sửa
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

// ==========================================
// 3. TAB QUẢN LÝ TÀI KHOẢN (Mục 2.2.3 & 3.7.3.3)
// ==========================================
const TabQuanLyTaiKhoan = ({ showToast }) => {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [formData, setFormData] = useState({
    username: '',
    pass: '',
    tenTK: '',
    sdtTK: '',
    role: 'MANAGER',
  });
  const [isEditing, setIsEditing] = useState(false);

  const fetchAll = async () => {
    try {
      setLoading(true);
      const data = await accountApi.getAll();
      setAccounts(data || []);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  const handleReset = () => {
    setFormData({ username: '', pass: '', tenTK: '', sdtTK: '', role: 'MANAGER' });
    setIsEditing(false);
  };

  const handleSelectRow = (acc) => {
    setFormData({
      username: acc.username,
      pass: '',
      tenTK: acc.tenTK || '',
      sdtTK: acc.sdtTK || '',
      role: acc.role,
    });
    setIsEditing(true);
  };

  const handleSearch = async () => {
    if (!keyword.trim()) {
      fetchAll();
      return;
    }
    try {
      setLoading(true);
      const data = await accountApi.search(keyword.trim());
      setAccounts(data || []);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async (e) => {
    e?.preventDefault();
    if (!formData.username.trim()) {
      showToast('Vui lòng nhập tên tài khoản', 'error');
      return;
    }
    if (!isEditing && !formData.pass) {
      showToast('Vui lòng nhập mật khẩu cho tài khoản mới', 'error');
      return;
    }

    try {
      if (isEditing) {
        await accountApi.update(formData);
        showToast('Cập nhật tài khoản thành công!');
      } else {
        await accountApi.add(formData);
        showToast('Thêm tài khoản mới thành công!');
      }
      handleReset();
      fetchAll();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  const handleDelete = async () => {
    if (!formData.username) {
      showToast('Vui lòng chọn 1 tài khoản để xóa', 'error');
      return;
    }
    if (!window.confirm(`Xác nhận xóa tài khoản "${formData.username}"?`)) return;

    try {
      await accountApi.delete(formData.username);
      showToast('Xóa tài khoản thành công!');
      handleReset();
      fetchAll();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6 space-y-6">
      <div className="pb-4 border-b border-slate-100">
        <h2 className="text-xl font-bold text-slate-800">Quản Lý Tài Khoản Nhân Sự</h2>
        <p className="text-sm text-slate-500">Quản lý và phân quyền các tài khoản Quản trị viên (Admin) và Thủ thư (Manager)</p>
      </div>

      {/* Form */}
      <form onSubmit={handleSave} className="bg-slate-50 p-5 rounded-2xl border border-slate-200 space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3">
          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Tên tài khoản (*)</label>
            <input
              type="text"
              value={formData.username}
              disabled={isEditing}
              onChange={(e) => setFormData({ ...formData, username: e.target.value })}
              placeholder="Username..."
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-bold disabled:bg-slate-100"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">
              {isEditing ? 'Đổi mật khẩu mới (nếu có)' : 'Mật khẩu (*)'}
            </label>
            <input
              type="password"
              value={formData.pass}
              onChange={(e) => setFormData({ ...formData, pass: e.target.value })}
              placeholder={isEditing ? 'Để trống nếu giữ nguyên' : 'Nhập mật khẩu...'}
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Họ và Tên</label>
            <input
              type="text"
              value={formData.tenTK}
              onChange={(e) => setFormData({ ...formData, tenTK: e.target.value })}
              placeholder="Tên nhân sự..."
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Số điện thoại</label>
            <input
              type="text"
              value={formData.sdtTK}
              onChange={(e) => setFormData({ ...formData, sdtTK: e.target.value })}
              placeholder="SĐT..."
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Vai trò</label>
            <select
              value={formData.role}
              onChange={(e) => setFormData({ ...formData, role: e.target.value })}
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-semibold"
            >
              <option value="MANAGER">Thủ thư (Manager)</option>
              <option value="ADMIN">Quản trị viên (Admin)</option>
            </select>
          </div>
        </div>

        <div className="flex flex-wrap items-center gap-2 pt-2 border-t border-slate-200/80">
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-bold shadow transition flex items-center gap-1.5"
          >
            {isEditing ? <Edit2 className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
            <span>{isEditing ? 'Cập Nhật Tài Khoản' : 'Thêm Tài Khoản'}</span>
          </button>

          {isEditing && (
            <button
              type="button"
              onClick={handleDelete}
              className="px-4 py-2 bg-rose-600 hover:bg-rose-700 text-white rounded-xl text-xs font-bold shadow transition flex items-center gap-1.5"
            >
              <Trash2 className="w-4 h-4" />
              <span>Xóa Tài Khoản</span>
            </button>
          )}

          <button
            type="button"
            onClick={handleReset}
            className="px-4 py-2 bg-white border border-slate-300 hover:bg-slate-100 text-slate-700 rounded-xl text-xs font-semibold transition"
          >
            Làm Mới
          </button>
        </div>
      </form>

      {/* Search */}
      <div className="flex items-center gap-2 max-w-md">
        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
          placeholder="Tìm theo tên hoặc username..."
          className="w-full px-3 py-2 border border-slate-300 rounded-lg text-xs"
        />
        <button
          onClick={handleSearch}
          className="px-3.5 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-xs font-bold shrink-0"
        >
          Tìm
        </button>
        <button
          onClick={() => {
            setKeyword('');
            fetchAll();
          }}
          className="px-3.5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-lg text-xs font-semibold shrink-0"
        >
          Hiển thị
        </button>
      </div>

      {/* Table */}
      <div className="overflow-x-auto border border-slate-200 rounded-xl">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-100 text-slate-700 text-xs font-bold uppercase tracking-wider">
            <tr>
              <th className="py-3 px-4">Tên tài khoản (Username)</th>
              <th className="py-3 px-4">Họ và Tên</th>
              <th className="py-3 px-4">Số điện thoại</th>
              <th className="py-3 px-4 text-center">Vai trò</th>
              <th className="py-3 px-4 text-center">Thao tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {loading ? (
              <tr>
                <td colSpan="5" className="text-center py-6 text-slate-400">
                  Đang tải...
                </td>
              </tr>
            ) : accounts.length === 0 ? (
              <tr>
                <td colSpan="5" className="text-center py-6 text-slate-400">
                  Chưa có tài khoản nào
                </td>
              </tr>
            ) : (
              accounts.map((acc) => (
                <tr
                  key={acc.username}
                  onClick={() => handleSelectRow(acc)}
                  className="cursor-pointer hover:bg-slate-50 transition"
                >
                  <td className="py-3 px-4 font-mono font-bold text-blue-700">{acc.username}</td>
                  <td className="py-3 px-4 text-slate-900 font-medium">{acc.tenTK || '—'}</td>
                  <td className="py-3 px-4 text-slate-600">{acc.sdtTK || '—'}</td>
                  <td className="py-3 px-4 text-center">
                    <span
                      className={`px-2.5 py-1 rounded-full text-xs font-bold ${
                        acc.role === 'ADMIN'
                          ? 'bg-amber-100 text-amber-800 border border-amber-200'
                          : 'bg-blue-100 text-blue-800 border border-blue-200'
                      }`}
                    >
                      {acc.role === 'ADMIN' ? 'Quản Trị Viên' : 'Thủ Thư'}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center">
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleSelectRow(acc);
                      }}
                      className="px-2.5 py-1 rounded text-xs font-semibold bg-slate-100 hover:bg-blue-100 text-slate-700 hover:text-blue-700 border border-slate-300 transition"
                    >
                      Sửa
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

// ==========================================
// 4. TAB TRUY VẤN LỊCH SỬ GIAO DỊCH (Mục 2.2.4 & 3.7.3.4)
// ==========================================
const TabLichSuGiaoDich = ({ showToast }) => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedTx, setSelectedTx] = useState(null);

  // Filters
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [statusFilter, setStatusFilter] = useState('');

  const fetchAll = async () => {
    try {
      setLoading(true);
      const data = await transactionApi.getAll();
      setTransactions(data || []);
      setSelectedTx(null);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  const handleQuery = async () => {
    try {
      setLoading(true);
      let data = [];
      if (startDate || endDate) {
        data = await transactionApi.getByDateRange(startDate, endDate);
      } else if (statusFilter) {
        data = await transactionApi.getByStatus(statusFilter);
      } else {
        data = await transactionApi.getAll();
      }
      setTransactions(data || []);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteOldRecords = async () => {
    if (
      !window.confirm(
        'Bạn có chắc chắn muốn xóa toàn bộ lịch sử các giao dịch đã hoàn thành quá 3 năm trước?'
      )
    ) {
      return;
    }

    try {
      await transactionApi.deleteOlderThan3Years();
      showToast('Đã xóa sạch các bản ghi giao dịch cũ hơn 3 năm thành công!');
      fetchAll();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b border-slate-100">
        <div>
          <h2 className="text-xl font-bold text-slate-800">Lịch Sử Giao Dịch Hệ Thống</h2>
          <p className="text-sm text-slate-500">Truy vấn toàn bộ vết giao dịch mượn, trả, báo mất và xuất nhập kho</p>
        </div>

        <button
          onClick={handleDeleteOldRecords}
          className="px-4 py-2 bg-rose-50 hover:bg-rose-100 text-rose-700 border border-rose-200 rounded-xl text-xs font-bold transition flex items-center gap-1.5 self-start sm:self-auto"
        >
          <Trash2 className="w-4 h-4 text-rose-600" />
          <span>Xóa giao dịch &gt; 3 năm</span>
        </button>
      </div>

      {/* Query Bar (3.7.3.4: Từ ngày ... đến ngày ... Truy vấn) */}
      <div className="grid grid-cols-1 md:grid-cols-12 gap-3 bg-slate-50 p-4 rounded-xl border border-slate-200">
        <div className="md:col-span-3">
          <label className="block text-xs font-bold text-slate-600 mb-1">Từ ngày:</label>
          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-xs"
          />
        </div>

        <div className="md:col-span-3">
          <label className="block text-xs font-bold text-slate-600 mb-1">Đến ngày:</label>
          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-xs"
          />
        </div>

        <div className="md:col-span-3">
          <label className="block text-xs font-bold text-slate-600 mb-1">Trạng thái giao dịch:</label>
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-xs"
          >
            <option value="">Tất cả trạng thái</option>
            <option value="Mượn sách">Mượn sách</option>
            <option value="Trả sách">Trả sách</option>
            <option value="Mất sách">Mất sách</option>
            <option value="Hủy số lượng sách">Hủy số lượng sách</option>
          </select>
        </div>

        <div className="md:col-span-3 flex items-end gap-2">
          <button
            onClick={handleQuery}
            className="flex-1 py-2 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-xs font-bold shadow transition"
          >
            Truy Vấn
          </button>
          <button
            onClick={() => {
              setStartDate('');
              setEndDate('');
              setStatusFilter('');
              fetchAll();
            }}
            className="py-2 px-3 bg-white border border-slate-300 hover:bg-slate-100 text-slate-700 rounded-lg text-xs font-semibold transition"
          >
            Hiển thị tất cả
          </button>
        </div>
      </div>

      {/* Selected Transaction detail */}
      {selectedTx && (
        <div className="p-4 rounded-xl bg-blue-50/70 border border-blue-200 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3 animate-fade-in">
          <div>
            <span className="text-xs font-bold uppercase tracking-wider text-blue-700">Chi tiết giao dịch:</span>
            <p className="text-sm font-extrabold text-blue-900">
              Mã: {selectedTx.maGD} — {selectedTx.trangThai} — Sách: {selectedTx.tenSach || selectedTx.maSach} ({selectedTx.soLuong} cuốn)
            </p>
            <p className="text-xs text-slate-600 mt-0.5">
              Thời gian: {selectedTx.ngayGD?.replace('T', ' ')} — Người thao tác: @{selectedTx.username} ({selectedTx.tenTK || 'Nội bộ'})
              {selectedTx.tenKH && ` — Bạn đọc: ${selectedTx.tenKH} (${selectedTx.maKH})`}
            </p>
          </div>
          <button
            onClick={() => setSelectedTx(null)}
            className="text-xs px-2.5 py-1 bg-white border border-blue-200 text-blue-700 rounded-lg font-semibold hover:bg-blue-50"
          >
            Đóng
          </button>
        </div>
      )}

      {/* Table (3.7.3.4) */}
      <div className="overflow-x-auto border border-slate-200 rounded-xl">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-100 text-slate-700 text-xs font-bold uppercase tracking-wider">
            <tr>
              <th className="py-3 px-3">Mã giao dịch</th>
              <th className="py-3 px-3">Tài khoản thao tác</th>
              <th className="py-3 px-3">Khách hàng</th>
              <th className="py-3 px-3">Đầu Sách</th>
              <th className="py-3 px-3">Ngày giao dịch</th>
              <th className="py-3 px-3 text-center">Số lượng</th>
              <th className="py-3 px-3 text-center">Trạng thái</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 text-xs">
            {loading ? (
              <tr>
                <td colSpan="7" className="text-center py-6 text-slate-400">
                  Đang truy xuất dữ liệu...
                </td>
              </tr>
            ) : transactions.length === 0 ? (
              <tr>
                <td colSpan="7" className="text-center py-6 text-slate-400">
                  Không tìm thấy giao dịch phù hợp
                </td>
              </tr>
            ) : (
              transactions.map((tx) => {
                const isSelected = selectedTx?.maGD === tx.maGD;
                return (
                  <tr
                    key={tx.maGD}
                    onClick={() => setSelectedTx(tx)}
                    className={`cursor-pointer transition ${
                      isSelected ? 'bg-blue-50 font-semibold' : 'hover:bg-slate-50'
                    }`}
                  >
                    <td className="py-3 px-3 font-mono font-bold text-blue-700">{tx.maGD}</td>
                    <td className="py-3 px-3 text-slate-800 font-medium">{tx.username}</td>
                    <td className="py-3 px-3 text-slate-700">{tx.tenKH || '—'}</td>
                    <td className="py-3 px-3 text-slate-900 font-medium">{tx.tenSach || tx.maSach}</td>
                    <td className="py-3 px-3 text-slate-600">{tx.ngayGD?.slice(0, 16).replace('T', ' ')}</td>
                    <td className="py-3 px-3 text-center font-bold text-slate-900">{tx.soLuong}</td>
                    <td className="py-3 px-3 text-center">
                      <span
                        className={`px-2 py-0.5 rounded text-[11px] font-bold ${
                          tx.trangThai === 'Mượn sách'
                            ? 'bg-blue-100 text-blue-800'
                            : tx.trangThai === 'Trả sách'
                            ? 'bg-emerald-100 text-emerald-800'
                            : tx.trangThai === 'Mất sách'
                            ? 'bg-rose-100 text-rose-800'
                            : 'bg-amber-100 text-amber-800'
                        }`}
                      >
                        {tx.trangThai}
                      </span>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

// ==========================================
// 5. TAB THÔNG TIN ADMIN (Mục 3.7.3.5)
// ==========================================
const TabThongTinAdmin = ({ user, logout }) => {
  return (
    <div className="max-w-xl mx-auto bg-white rounded-3xl shadow-sm border border-slate-200/80 p-8 text-center space-y-6">
      <div className="w-20 h-20 mx-auto rounded-3xl bg-gradient-to-tr from-amber-500 to-amber-600 text-white flex items-center justify-center font-black text-3xl shadow-xl shadow-amber-500/20">
        <Shield className="w-10 h-10" />
      </div>

      <div>
        <h2 className="text-2xl font-black text-slate-900">{user?.tenTK || 'Administrator'}</h2>
        <span className="inline-block mt-1 px-3 py-1 rounded-full text-xs font-bold bg-amber-100 text-amber-800 border border-amber-200">
          Quản Trị Viên (Admin) — Toàn Quyền Quản Trị
        </span>
      </div>

      <div className="bg-slate-50 p-5 rounded-2xl border border-slate-200 text-left space-y-3 text-sm">
        <div className="flex justify-between py-1 border-b border-slate-200/80">
          <span className="text-slate-500">Tên tài khoản:</span>
          <span className="font-mono font-bold text-slate-800">{user?.username}</span>
        </div>
        <div className="flex justify-between py-1 border-b border-slate-200/80">
          <span className="text-slate-500">Họ và tên:</span>
          <span className="font-semibold text-slate-800">{user?.tenTK || 'Administrator'}</span>
        </div>
        <div className="flex justify-between py-1 border-b border-slate-200/80">
          <span className="text-slate-500">Số điện thoại:</span>
          <span className="font-semibold text-slate-800">{user?.sdtTK || '0347162755'}</span>
        </div>
        <div className="flex justify-between py-1">
          <span className="text-slate-500">Vai trò bảo mật:</span>
          <span className="font-bold text-amber-700">{user?.role}</span>
        </div>
      </div>

      <button
        onClick={logout}
        className="w-full py-3 px-4 bg-rose-600 hover:bg-rose-700 text-white font-bold rounded-2xl shadow transition"
      >
        Đăng Xuất Khỏi Hệ Thống
      </button>
    </div>
  );
};

