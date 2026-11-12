import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { Navbar } from '../components/Navbar';
import { Toast } from '../components/Toast';
import { Modal } from '../components/Modal';
import customerApi from '../api/customerApi';
import bookApi from '../api/bookApi';
import borrowApi from '../api/borrowApi';
import categoryApi from '../api/categoryApi';
import {
  Search,
  BookPlus,
  RotateCcw,
  FileText,
  Users,
  UserCheck,
  AlertTriangle,
  Calendar,
  CheckCircle,
  Plus,
  Edit2,
  Trash2,
  RefreshCw,
  Clock,
  BookOpen,
} from 'lucide-react';

export const ManagerDashboard = () => {
  const { user, logout } = useAuth();
  const [currentTab, setCurrentTab] = useState('tim-kiem');
  const [toast, setToast] = useState({ message: '', type: 'success' });

  // Shared state: Currently selected customer for borrow/return operations
  const [selectedCustomer, setSelectedCustomer] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast({ message: '', type: 'success' }), 4000);
  };

  const tabs = [
    { id: 'tim-kiem', label: '1. Tìm Kiếm Bạn Đọc', icon: Search },
    { id: 'muon-sach', label: '2. Mượn Sách', icon: BookPlus },
    { id: 'tra-sach', label: '3. Trả / Mất Sách', icon: RotateCcw },
    { id: 'truy-van', label: '4. Truy Vấn Đơn Mượn', icon: FileText },
    { id: 'ban-doc', label: '5. Quản Lý Bạn Đọc', icon: Users },
    { id: 'tai-khoan', label: '6. Thông Tin Tài Khoản', icon: UserCheck },
  ];

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <Navbar currentTab={currentTab} onSelectTab={setCurrentTab} tabs={tabs} />

      <main className="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 lg:p-8">
        {currentTab === 'tim-kiem' && (
          <TabTimKiem
            onSelectCustomer={(cust, nextTab) => {
              setSelectedCustomer(cust);
              setCurrentTab(nextTab || 'muon-sach');
              showToast(`Đã chọn bạn đọc: ${cust.tenKH} (${cust.maKH})`);
            }}
            showToast={showToast}
          />
        )}

        {currentTab === 'muon-sach' && (
          <TabMuonSach
            selectedCustomer={selectedCustomer}
            setSelectedCustomer={setSelectedCustomer}
            showToast={showToast}
            user={user}
          />
        )}

        {currentTab === 'tra-sach' && (
          <TabTraSach
            selectedCustomer={selectedCustomer}
            setSelectedCustomer={setSelectedCustomer}
            showToast={showToast}
            user={user}
          />
        )}

        {currentTab === 'truy-van' && <TabTruyVanDonMuon showToast={showToast} />}

        {currentTab === 'ban-doc' && <TabQuanLyBanDoc showToast={showToast} />}

        {currentTab === 'tai-khoan' && <TabThongTinTaiKhoan user={user} logout={logout} />}
      </main>

      <Toast message={toast.message} type={toast.type} onClose={() => setToast({ message: '', type: 'success' })} />
    </div>
  );
};

// ==========================================
// 1. TAB TÌM KIẾM BẠN ĐỌC (Mục 2.2.5 & 3.7.2.1)
// ==========================================
const TabTimKiem = ({ onSelectCustomer, showToast }) => {
  const [searchType, setSearchType] = useState('ma'); // 'ma', 'ten', 'sdt'
  const [keyword, setKeyword] = useState('');
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedRow, setSelectedRow] = useState(null);

  const fetchAll = async () => {
    try {
      setLoading(true);
      const data = await customerApi.getAll();
      setCustomers(data || []);
      setSelectedRow(null);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  const handleSearch = async () => {
    if (!keyword.trim()) {
      fetchAll();
      return;
    }
    try {
      setLoading(true);
      let results = [];
      if (searchType === 'ma') {
        try {
          const item = await customerApi.getById(keyword.trim());
          if (item) results = [item];
        } catch {
          results = [];
        }
      } else if (searchType === 'ten') {
        results = await customerApi.searchByName(keyword.trim());
      } else if (searchType === 'sdt') {
        results = await customerApi.searchBySDT(keyword.trim());
      }
      setCustomers(results || []);
      if (!results || results.length === 0) {
        showToast('Không tìm thấy bạn đọc phù hợp', 'info');
      }
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6">
      <div className="mb-6 pb-4 border-b border-slate-100 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-slate-800">Tra cứu & Tìm kiếm Bạn đọc</h2>
          <p className="text-sm text-slate-500">Tìm kiếm độc giả để thực hiện mượn hoặc trả sách nhanh chóng</p>
        </div>
      </div>

      {/* Filter Controls */}
      <div className="grid grid-cols-1 md:grid-cols-12 gap-3 mb-6 bg-slate-50 p-4 rounded-xl border border-slate-200">
        <div className="md:col-span-3">
          <label className="block text-xs font-bold text-slate-600 mb-1">Tìm kiếm theo</label>
          <select
            value={searchType}
            onChange={(e) => setSearchType(e.target.value)}
            className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm focus:ring-2 focus:ring-blue-500/20"
          >
            <option value="ma">Mã Khách Hàng (Mã bạn đọc)</option>
            <option value="ten">Tên Bạn Đọc</option>
            <option value="sdt">Số Điện Thoại</option>
          </select>
        </div>

        <div className="md:col-span-6">
          <label className="block text-xs font-bold text-slate-600 mb-1">Từ khóa tìm kiếm</label>
          <div className="relative">
            <input
              type="text"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              placeholder="Nhập mã, tên hoặc số điện thoại..."
              className="w-full pl-9 pr-4 py-2 bg-white rounded-lg border border-slate-300 text-sm focus:ring-2 focus:ring-blue-500/20"
            />
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
          </div>
        </div>

        <div className="md:col-span-3 flex items-end gap-2">
          <button
            onClick={handleSearch}
            className="flex-1 py-2 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-sm font-semibold shadow-sm transition"
          >
            Tìm Kiếm
          </button>
          <button
            onClick={() => {
              setKeyword('');
              fetchAll();
            }}
            className="py-2 px-3 bg-white hover:bg-slate-100 text-slate-700 rounded-lg border border-slate-300 text-sm font-medium transition"
            title="Làm mới / Hiển thị tất cả"
          >
            Hiển thị tất cả
          </button>
        </div>
      </div>

      {/* Selected Action Bar */}
      {selectedRow && (
        <div className="mb-4 p-4 rounded-xl bg-blue-50/80 border border-blue-200 flex flex-col sm:flex-row items-center justify-between gap-3 animate-fade-in">
          <div>
            <span className="text-xs font-bold uppercase tracking-wider text-blue-700">Đang chọn bạn đọc:</span>
            <p className="text-sm font-extrabold text-blue-900">
              {selectedRow.tenKH} — Mã: {selectedRow.maKH} — SĐT: {selectedRow.sdtKH || 'Chưa có'}
            </p>
          </div>
          <div className="flex items-center gap-2">
            <button
              onClick={() => onSelectCustomer(selectedRow, 'muon-sach')}
              className="py-2 px-3.5 bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold rounded-lg shadow transition flex items-center gap-1.5"
            >
              <BookPlus className="w-4 h-4" />
              <span>Chuyển sang Lập Đơn Mượn</span>
            </button>
            <button
              onClick={() => onSelectCustomer(selectedRow, 'tra-sach')}
              className="py-2 px-3.5 bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-bold rounded-lg shadow transition flex items-center gap-1.5"
            >
              <RotateCcw className="w-4 h-4" />
              <span>Chuyển sang Trả Sách</span>
            </button>
          </div>
        </div>
      )}

      {/* Customers Table */}
      <div className="overflow-x-auto border border-slate-200 rounded-xl">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-100/80 text-slate-700 text-xs font-bold uppercase tracking-wider">
            <tr>
              <th className="py-3 px-4">Mã Khách Hàng</th>
              <th className="py-3 px-4">Tên Khách Hàng (Bạn đọc)</th>
              <th className="py-3 px-4">Số Điện Thoại</th>
              <th className="py-3 px-4 text-center">Thao tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {loading ? (
              <tr>
                <td colSpan="4" className="text-center py-8 text-slate-400">
                  Đang tải dữ liệu...
                </td>
              </tr>
            ) : customers.length === 0 ? (
              <tr>
                <td colSpan="4" className="text-center py-8 text-slate-400">
                  Không tìm thấy bạn đọc nào
                </td>
              </tr>
            ) : (
              customers.map((c) => {
                const isSelected = selectedRow?.maKH === c.maKH;
                return (
                  <tr
                    key={c.maKH}
                    onClick={() => setSelectedRow(c)}
                    className={`cursor-pointer transition ${
                      isSelected ? 'bg-blue-50/80 font-semibold' : 'hover:bg-slate-50'
                    }`}
                  >
                    <td className="py-3 px-4 font-mono text-blue-700 font-bold">{c.maKH}</td>
                    <td className="py-3 px-4 text-slate-900">{c.tenKH}</td>
                    <td className="py-3 px-4 text-slate-600">{c.sdtKH || '—'}</td>
                    <td className="py-3 px-4 text-center">
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          setSelectedRow(c);
                        }}
                        className={`px-3 py-1 rounded-lg text-xs font-medium border transition ${
                          isSelected
                            ? 'bg-blue-600 text-white border-blue-600'
                            : 'bg-white text-slate-700 border-slate-300 hover:bg-slate-100'
                        }`}
                      >
                        {isSelected ? 'Đang chọn' : 'Lấy thông tin'}
                      </button>
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
// 2. TAB MƯỢN SÁCH (Mục 2.2.6 & 3.7.2.2)
// ==========================================
const TabMuonSach = ({ selectedCustomer, setSelectedCustomer, showToast, user }) => {
  const [customerCode, setCustomerCode] = useState(selectedCustomer?.maKH || '');
  const [customerName, setCustomerName] = useState(selectedCustomer?.tenKH || '');

  // Book warehouse search
  const [books, setBooks] = useState([]);
  const [bookKeyword, setBookKeyword] = useState('');
  const [selectedBook, setSelectedBook] = useState(null);
  const [borrowQuantity, setBorrowQuantity] = useState(1);
  const [borrowDate, setBorrowDate] = useState(() => new Date().toISOString().slice(0, 10));
  const [returnDate, setReturnDate] = useState(() => {
    const d = new Date();
    d.setDate(d.getDate() + 14);
    return d.toISOString().slice(0, 10);
  });

  const [recentBorrows, setRecentBorrows] = useState([]);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (selectedCustomer) {
      setCustomerCode(selectedCustomer.maKH);
      setCustomerName(selectedCustomer.tenKH);
    }
  }, [selectedCustomer]);

  const loadBooks = async () => {
    try {
      const data = await bookApi.getAll();
      setBooks(data || []);
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  useEffect(() => {
    loadBooks();
  }, []);

  const handleSearchBook = async () => {
    if (!bookKeyword.trim()) {
      loadBooks();
      return;
    }
    try {
      const data = await bookApi.searchByName(bookKeyword.trim());
      setBooks(data || []);
      if (!data || data.length === 0) {
        showToast('Không tìm thấy sách phù hợp', 'info');
      }
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  const handleLookupCustomer = async () => {
    if (!customerCode.trim()) return;
    try {
      const cust = await customerApi.getById(customerCode.trim());
      if (cust) {
        setSelectedCustomer(cust);
        setCustomerName(cust.tenKH);
        showToast(`Đã tìm thấy bạn đọc: ${cust.tenKH}`);
      }
    } catch (err) {
      showToast('Mã bạn đọc không tồn tại', 'error');
    }
  };

  const handleCreateBorrow = async (e) => {
    e.preventDefault();
    if (!customerCode.trim()) {
      showToast('Vui lòng chọn hoặc nhập mã bạn đọc', 'error');
      return;
    }
    if (!selectedBook) {
      showToast('Vui lòng chọn 1 cuốn sách từ kho sách', 'error');
      return;
    }
    if (borrowQuantity <= 0) {
      showToast('Số lượng mượn phải lớn hơn 0', 'error');
      return;
    }
    if (borrowQuantity > selectedBook.soLuong) {
      showToast(`Số lượng trong kho chỉ còn ${selectedBook.soLuong} cuốn, không đủ để mượn`, 'error');
      return;
    }

    try {
      setSubmitting(true);
      const maMuon = 'PM' + Math.floor(100000 + Math.random() * 900000);
      const payload = {
        maMuon,
        maKH: customerCode.trim(),
        maSach: selectedBook.maSach,
        soLuong: parseInt(borrowQuantity, 10),
        ngayMuon: borrowDate ? `${borrowDate}T09:00:00` : null,
        ngayTra: returnDate ? `${returnDate}T17:00:00` : null,
        username: user?.username || 'hoannhunguyen',
      };

      const res = await borrowApi.add(payload);
      showToast(`Lập phiếu mượn ${maMuon} thành công!`, 'success');

      setRecentBorrows((prev) => [res, ...prev]);
      setSelectedBook(null);
      setBorrowQuantity(1);
      loadBooks(); // refresh stock count
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Top Customer Banner */}
      <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6">
        <h2 className="text-xl font-bold text-slate-800 mb-4">Lập Phiếu Mượn Sách</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 bg-slate-50 p-4 rounded-xl border border-slate-200">
          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Mã Bạn Đọc (Khách hàng)</label>
            <div className="flex gap-2">
              <input
                type="text"
                value={customerCode}
                onChange={(e) => setCustomerCode(e.target.value)}
                onBlur={handleLookupCustomer}
                placeholder="VD: KH252642"
                className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-mono font-bold"
              />
              <button
                type="button"
                onClick={handleLookupCustomer}
                className="px-3 py-2 bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold rounded-lg shrink-0"
              >
                Kiểm tra
              </button>
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Tên Bạn Đọc</label>
            <input
              type="text"
              value={customerName}
              readOnly
              placeholder="Chưa chọn bạn đọc"
              className="w-full px-3 py-2 bg-slate-100 rounded-lg border border-slate-200 text-sm font-semibold text-slate-800"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Ngày Hẹn Trả</label>
            <input
              type="date"
              value={returnDate}
              onChange={(e) => setReturnDate(e.target.value)}
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>
        </div>
      </div>

      {/* Grid: Left selected borrow item & Recent, Right Book Warehouse */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Left: Action Card & Created borrow slips */}
        <div className="lg:col-span-5 space-y-4">
          <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-5">
            <h3 className="text-base font-bold text-slate-800 mb-3 flex items-center gap-2">
              <BookPlus className="w-5 h-5 text-blue-600" />
              <span>Xác nhận sách muốn mượn</span>
            </h3>

            {selectedBook ? (
              <div className="space-y-3 bg-blue-50/70 p-4 rounded-xl border border-blue-200">
                <div className="flex justify-between items-start">
                  <div>
                    <span className="text-xs font-bold text-blue-700 font-mono">{selectedBook.maSach}</span>
                    <h4 className="font-extrabold text-slate-900 text-base">{selectedBook.tenSach}</h4>
                    <p className="text-xs text-slate-600">Tác giả: {selectedBook.tacGia || 'Chưa rõ'}</p>
                    <p className="text-xs text-slate-600">Thể loại: {selectedBook.tenTheLoai}</p>
                  </div>
                  <span className="text-xs px-2 py-1 bg-emerald-100 text-emerald-800 rounded font-bold">
                    Còn {selectedBook.soLuong} cuốn
                  </span>
                </div>

                <div className="pt-2 border-t border-blue-200/80">
                  <label className="block text-xs font-bold text-slate-700 mb-1">Số lượng mượn:</label>
                  <input
                    type="number"
                    min="1"
                    max={selectedBook.soLuong}
                    value={borrowQuantity}
                    onChange={(e) => setBorrowQuantity(parseInt(e.target.value, 10) || 1)}
                    className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-bold"
                  />
                </div>

                <button
                  type="button"
                  disabled={submitting}
                  onClick={handleCreateBorrow}
                  className="w-full py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl shadow transition"
                >
                  {submitting ? 'Đang lưu...' : 'Xác nhận Lập Phiếu Mượn'}
                </button>
              </div>
            ) : (
              <div className="text-center py-8 px-4 bg-slate-50 border border-dashed border-slate-300 rounded-xl text-slate-500 text-sm">
                Hãy click chọn 1 cuốn sách từ bảng "Kho Sách" bên phải để mượn
              </div>
            )}
          </div>

          {/* Recent borrow list in this session */}
          {recentBorrows.length > 0 && (
            <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-5">
              <h3 className="text-sm font-bold text-slate-800 mb-3">Phiếu mượn vừa tạo thành công</h3>
              <div className="space-y-2">
                {recentBorrows.map((b) => (
                  <div key={b.maMuon} className="p-3 rounded-lg border border-emerald-200 bg-emerald-50 text-xs">
                    <div className="flex justify-between font-bold text-emerald-900">
                      <span>Mã phiếu: {b.maMuon}</span>
                      <span>SL: {b.soLuong} cuốn</span>
                    </div>
                    <p className="text-slate-700 mt-1">Sách: {b.tenSach}</p>
                    <p className="text-slate-500">Người mượn: {b.tenKH} ({b.maKH})</p>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Right: Book Warehouse Table */}
        <div className="lg:col-span-7 bg-white rounded-2xl shadow-sm border border-slate-200/80 p-5">
          <div className="flex flex-col sm:flex-row items-center justify-between gap-3 mb-4">
            <h3 className="text-base font-bold text-slate-800">Kho Sách Thư Viện</h3>
            <div className="flex items-center gap-2 w-full sm:w-auto">
              <input
                type="text"
                placeholder="Tìm tên sách..."
                value={bookKeyword}
                onChange={(e) => setBookKeyword(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSearchBook()}
                className="px-3 py-1.5 border border-slate-300 rounded-lg text-xs w-full sm:w-48"
              />
              <button
                onClick={handleSearchBook}
                className="px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-xs font-semibold shrink-0"
              >
                Tìm
              </button>
            </div>
          </div>

          <div className="overflow-x-auto border border-slate-200 rounded-xl">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-100 text-slate-700 font-bold uppercase">
                <tr>
                  <th className="py-2.5 px-3">Mã</th>
                  <th className="py-2.5 px-3">Tên Sách</th>
                  <th className="py-2.5 px-3">Tác giả</th>
                  <th className="py-2.5 px-3">Thể loại</th>
                  <th className="py-2.5 px-3 text-center">Tồn kho</th>
                  <th className="py-2.5 px-3 text-center">Chọn</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {books.map((bk) => {
                  const isSelected = selectedBook?.maSach === bk.maSach;
                  const outOfStock = bk.soLuong <= 0;
                  return (
                    <tr
                      key={bk.maSach}
                      onClick={() => !outOfStock && setSelectedBook(bk)}
                      className={`cursor-pointer transition ${
                        isSelected ? 'bg-blue-50 font-semibold' : outOfStock ? 'opacity-50 bg-slate-50' : 'hover:bg-slate-50'
                      }`}
                    >
                      <td className="py-2.5 px-3 font-mono font-bold text-blue-700">{bk.maSach}</td>
                      <td className="py-2.5 px-3 text-slate-900 font-medium">{bk.tenSach}</td>
                      <td className="py-2.5 px-3 text-slate-600">{bk.tacGia || '—'}</td>
                      <td className="py-2.5 px-3 text-slate-600">{bk.tenTheLoai}</td>
                      <td className="py-2.5 px-3 text-center">
                        <span
                          className={`px-2 py-0.5 rounded font-bold ${
                            outOfStock ? 'bg-rose-100 text-rose-800' : 'bg-emerald-100 text-emerald-800'
                          }`}
                        >
                          {bk.soLuong}
                        </span>
                      </td>
                      <td className="py-2.5 px-3 text-center">
                        <button
                          disabled={outOfStock}
                          onClick={(e) => {
                            e.stopPropagation();
                            setSelectedBook(bk);
                          }}
                          className={`px-2.5 py-1 rounded text-[11px] font-semibold border ${
                            isSelected
                              ? 'bg-blue-600 text-white border-blue-600'
                              : 'bg-white text-slate-700 border-slate-300 hover:bg-slate-100'
                          }`}
                        >
                          {outOfStock ? 'Hết sách' : isSelected ? 'Đã chọn' : 'Mượn sách'}
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

// ==========================================
// 3. TAB TRẢ SÁCH / BÁO MẤT (Mục 2.2.7 & 3.7.2.3)
// ==========================================
const TabTraSach = ({ selectedCustomer, setSelectedCustomer, showToast, user }) => {
  const [customerCode, setCustomerCode] = useState(selectedCustomer?.maKH || '');
  const [customerName, setCustomerName] = useState(selectedCustomer?.tenKH || '');
  const [borrowList, setBorrowList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedBorrow, setSelectedBorrow] = useState(null);
  const [returnQuantity, setReturnQuantity] = useState(1);
  const [submitting, setSubmitting] = useState(false);

  const fetchBorrowList = async (makh) => {
    if (!makh) return;
    try {
      setLoading(true);
      const data = await borrowApi.getByMaKH(makh);
      setBorrowList(data || []);
      setSelectedBorrow(null);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (selectedCustomer) {
      setCustomerCode(selectedCustomer.maKH);
      setCustomerName(selectedCustomer.tenKH);
      fetchBorrowList(selectedCustomer.maKH);
    }
  }, [selectedCustomer]);

  const handleLookup = async () => {
    if (!customerCode.trim()) return;
    try {
      const cust = await customerApi.getById(customerCode.trim());
      if (cust) {
        setSelectedCustomer(cust);
        setCustomerName(cust.tenKH);
        fetchBorrowList(cust.maKH);
        showToast(`Đã tìm thấy bạn đọc: ${cust.tenKH}`);
      }
    } catch {
      showToast('Không tìm thấy bạn đọc này', 'error');
    }
  };

  const handleReturnBook = async () => {
    if (!selectedBorrow) {
      showToast('Vui lòng chọn 1 mục sách đang mượn', 'error');
      return;
    }
    if (returnQuantity <= 0 || returnQuantity > selectedBorrow.soLuong) {
      showToast('Số lượng trả không hợp lệ', 'error');
      return;
    }

    try {
      setSubmitting(true);
      await borrowApi.traSach({
        maMuon: selectedBorrow.maMuon,
        soLuong: parseInt(returnQuantity, 10),
        username: user?.username || 'hoannhunguyen',
      });
      showToast(`Trả thành công ${returnQuantity} cuốn sách "${selectedBorrow.tenSach}"`, 'success');
      fetchBorrowList(customerCode);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setSubmitting(false);
    }
  };

  const handleLostBook = async () => {
    if (!selectedBorrow) {
      showToast('Vui lòng chọn 1 mục sách', 'error');
      return;
    }
    if (
      !window.confirm(
        `Xác nhận báo mất ${returnQuantity} cuốn sách "${selectedBorrow.tenSach}"? Thao tác này sẽ ghi nhận vào lịch sử giao dịch và không hoàn sách vào kho.`
      )
    ) {
      return;
    }

    try {
      setSubmitting(true);
      await borrowApi.matSach({
        maMuon: selectedBorrow.maMuon,
        soLuongMat: parseInt(returnQuantity, 10),
        username: user?.username || 'hoannhunguyen',
      });
      showToast(`Đã ghi nhận báo mất ${returnQuantity} cuốn sách!`, 'info');
      fetchBorrowList(customerCode);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header & Customer search */}
      <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6">
        <h2 className="text-xl font-bold text-slate-800 mb-4">Quản Lý Trả Sách & Báo Mất Sách</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 bg-slate-50 p-4 rounded-xl border border-slate-200">
          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Mã Bạn Đọc (Khách hàng)</label>
            <div className="flex gap-2">
              <input
                type="text"
                value={customerCode}
                onChange={(e) => setCustomerCode(e.target.value)}
                onBlur={handleLookup}
                placeholder="VD: KH252642"
                className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-mono font-bold"
              />
              <button
                type="button"
                onClick={handleLookup}
                className="px-3 py-2 bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold rounded-lg shrink-0"
              >
                Tra cứu
              </button>
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Tên Bạn Đọc</label>
            <input
              type="text"
              value={customerName}
              readOnly
              placeholder="Chưa chọn bạn đọc"
              className="w-full px-3 py-2 bg-slate-100 rounded-lg border border-slate-200 text-sm font-semibold text-slate-800"
            />
          </div>

          <div className="flex items-end">
            <button
              onClick={() => fetchBorrowList(customerCode)}
              className="py-2 px-4 bg-slate-200 hover:bg-slate-300 text-slate-800 rounded-lg text-sm font-semibold transition"
            >
              Làm mới danh sách đang mượn
            </button>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Left: Action return panel */}
        <div className="lg:col-span-5 bg-white rounded-2xl shadow-sm border border-slate-200/80 p-5">
          <h3 className="text-base font-bold text-slate-800 mb-3 flex items-center gap-2">
            <RotateCcw className="w-5 h-5 text-emerald-600" />
            <span>Xác nhận thông tin Trả sách</span>
          </h3>

          {selectedBorrow ? (
            <div className="space-y-4 bg-emerald-50/70 p-4 rounded-xl border border-emerald-200">
              <div>
                <span className="text-xs font-bold text-emerald-800 font-mono">Phiếu: {selectedBorrow.maMuon}</span>
                <h4 className="font-extrabold text-slate-900 text-base">{selectedBorrow.tenSach}</h4>
                <p className="text-xs text-slate-600">Đang mượn: <b>{selectedBorrow.soLuong} cuốn</b></p>
                <p className="text-xs text-slate-600">
                  Ngày mượn: {selectedBorrow.ngayMuon?.slice(0, 10)} — Hạn trả: {selectedBorrow.ngayTra?.slice(0, 10)}
                </p>
                {selectedBorrow.daQuaHan && (
                  <span className="inline-block mt-1 text-[11px] font-bold text-rose-700 bg-rose-100 px-2 py-0.5 rounded">
                    ⚠️ ĐÃ QUÁ HẠN TRẢ
                  </span>
                )}
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">Số lượng trả:</label>
                <input
                  type="number"
                  min="1"
                  max={selectedBorrow.soLuong}
                  value={returnQuantity}
                  onChange={(e) => setReturnQuantity(parseInt(e.target.value, 10) || 1)}
                  className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-bold"
                />
              </div>

              <div className="grid grid-cols-2 gap-2 pt-2 border-t border-emerald-200">
                <button
                  type="button"
                  disabled={submitting}
                  onClick={handleReturnBook}
                  className="py-2.5 px-3 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl shadow transition text-xs flex items-center justify-center gap-1"
                >
                  <CheckCircle className="w-4 h-4" />
                  <span>Xác Nhận Trả</span>
                </button>

                <button
                  type="button"
                  disabled={submitting}
                  onClick={handleLostBook}
                  className="py-2.5 px-3 bg-rose-600 hover:bg-rose-700 text-white font-bold rounded-xl shadow transition text-xs flex items-center justify-center gap-1"
                >
                  <AlertTriangle className="w-4 h-4" />
                  <span>Báo Mất Sách</span>
                </button>
              </div>
            </div>
          ) : (
            <div className="text-center py-10 px-4 bg-slate-50 border border-dashed border-slate-300 rounded-xl text-slate-500 text-sm">
              Chọn một dòng sách từ "Danh sách đang mượn" bên phải để tiến hành trả hoặc báo mất
            </div>
          )}
        </div>

        {/* Right: Borrowed books table */}
        <div className="lg:col-span-7 bg-white rounded-2xl shadow-sm border border-slate-200/80 p-5">
          <h3 className="text-base font-bold text-slate-800 mb-3">
            Danh sách sách đang mượn của bạn đọc ({borrowList.length})
          </h3>

          <div className="overflow-x-auto border border-slate-200 rounded-xl">
            <table className="w-full text-left text-xs">
              <thead className="bg-slate-100 text-slate-700 font-bold uppercase">
                <tr>
                  <th className="py-2.5 px-3">Mã mượn</th>
                  <th className="py-2.5 px-3">Tên Sách</th>
                  <th className="py-2.5 px-3 text-center">Số lượng</th>
                  <th className="py-2.5 px-3">Ngày mượn</th>
                  <th className="py-2.5 px-3">Hạn trả</th>
                  <th className="py-2.5 px-3 text-center">Chọn</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {loading ? (
                  <tr>
                    <td colSpan="6" className="text-center py-6 text-slate-400">
                      Đang tải danh sách mượn...
                    </td>
                  </tr>
                ) : borrowList.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="text-center py-6 text-slate-400">
                      Bạn đọc này hiện không nợ cuốn sách nào
                    </td>
                  </tr>
                ) : (
                  borrowList.map((bm) => {
                    const isSelected = selectedBorrow?.maMuon === bm.maMuon;
                    return (
                      <tr
                        key={bm.maMuon}
                        onClick={() => {
                          setSelectedBorrow(bm);
                          setReturnQuantity(bm.soLuong);
                        }}
                        className={`cursor-pointer transition ${
                          isSelected ? 'bg-emerald-50 font-semibold' : 'hover:bg-slate-50'
                        }`}
                      >
                        <td className="py-2.5 px-3 font-mono font-bold text-blue-700">{bm.maMuon}</td>
                        <td className="py-2.5 px-3 text-slate-900 font-medium">{bm.tenSach}</td>
                        <td className="py-2.5 px-3 text-center font-bold text-slate-800">{bm.soLuong}</td>
                        <td className="py-2.5 px-3 text-slate-600">{bm.ngayMuon?.slice(0, 10)}</td>
                        <td className="py-2.5 px-3 text-slate-600">
                          {bm.ngayTra?.slice(0, 10)}
                          {bm.daQuaHan && (
                            <span className="ml-1 text-[10px] text-rose-600 font-bold bg-rose-50 px-1 py-0.5 rounded">
                              Quá hạn
                            </span>
                          )}
                        </td>
                        <td className="py-2.5 px-3 text-center">
                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              setSelectedBorrow(bm);
                              setReturnQuantity(bm.soLuong);
                            }}
                            className={`px-2 py-1 rounded text-[11px] font-semibold border ${
                              isSelected
                                ? 'bg-emerald-600 text-white border-emerald-600'
                                : 'bg-white text-slate-700 border-slate-300 hover:bg-slate-100'
                            }`}
                          >
                            {isSelected ? 'Đang chọn' : 'Chọn'}
                          </button>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

// ==========================================
// 4. TAB TRUY VẤN ĐƠN MƯỢN (Mục 2.2.8 & 3.7.2.4)
// ==========================================
const TabTruyVanDonMuon = ({ showToast }) => {
  const [borrowList, setBorrowList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filterOverdueOnly, setFilterOverdueOnly] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);

  const fetchAll = async () => {
    try {
      setLoading(true);
      const data = await borrowApi.getAll();
      setBorrowList(data || []);
      setFilterOverdueOnly(false);
      setSelectedItem(null);
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const fetchOverdue = async () => {
    try {
      setLoading(true);
      const data = await borrowApi.getOverdue();
      setBorrowList(data || []);
      setFilterOverdueOnly(true);
      setSelectedItem(null);
      showToast(`Đã lọc ra ${data?.length || 0} đơn mượn quá hạn trả!`, 'info');
    } catch (err) {
      showToast(err.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b border-slate-100">
        <div>
          <h2 className="text-xl font-bold text-slate-800">Truy Vấn Lịch Sử & Đơn Mượn Sách</h2>
          <p className="text-sm text-slate-500">Tra cứu đơn mượn theo thời gian hoặc kiểm tra các đơn đã quá hạn trả</p>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={fetchAll}
            className={`px-4 py-2 text-sm font-semibold rounded-xl border transition ${
              !filterOverdueOnly
                ? 'bg-blue-600 text-white border-blue-600 shadow'
                : 'bg-white text-slate-700 border-slate-300 hover:bg-slate-100'
            }`}
          >
            Tất Cả Đơn Mượn
          </button>
          <button
            onClick={fetchOverdue}
            className={`px-4 py-2 text-sm font-bold rounded-xl border transition flex items-center gap-1.5 ${
              filterOverdueOnly
                ? 'bg-rose-600 text-white border-rose-600 shadow'
                : 'bg-rose-50 text-rose-700 border-rose-200 hover:bg-rose-100'
            }`}
          >
            <Clock className="w-4 h-4" />
            <span>Quá Hạn Trả</span>
          </button>
        </div>
      </div>

      {/* Detail card if selected */}
      {selectedItem && (
        <div className="p-4 rounded-xl bg-slate-50 border border-slate-200 flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
          <div>
            <span className="text-xs font-bold text-blue-600 uppercase">Chi tiết đơn mượn: {selectedItem.maMuon}</span>
            <h4 className="text-base font-bold text-slate-900 mt-0.5">
              Sách: {selectedItem.tenSach} ({selectedItem.soLuong} cuốn)
            </h4>
            <p className="text-xs text-slate-600 mt-1">
              Độc giả: <b>{selectedItem.tenKH}</b> (Mã: {selectedItem.maKH} - SĐT: {selectedItem.sdtKH || '—'})
            </p>
            <p className="text-xs text-slate-500 mt-0.5">
              Ngày mượn: {selectedItem.ngayMuon?.replace('T', ' ')} — Hạn trả: {selectedItem.ngayTra?.replace('T', ' ')}
            </p>
          </div>
          {selectedItem.daQuaHan && (
            <div className="px-3 py-1.5 rounded-lg bg-rose-100 text-rose-800 text-xs font-bold flex items-center gap-1.5">
              <AlertTriangle className="w-4 h-4 text-rose-600" />
              <span>Đơn này đã quá hạn hoàn trả!</span>
            </div>
          )}
        </div>
      )}

      {/* Table */}
      <div className="overflow-x-auto border border-slate-200 rounded-xl">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-100 text-slate-700 text-xs font-bold uppercase tracking-wider">
            <tr>
              <th className="py-3 px-4">Mã Mượn</th>
              <th className="py-3 px-4">Tên Khách Hàng</th>
              <th className="py-3 px-4">Tên Sách</th>
              <th className="py-3 px-4 text-center">Số lượng</th>
              <th className="py-3 px-4">Ngày mượn</th>
              <th className="py-3 px-4">Ngày trả</th>
              <th className="py-3 px-4 text-center">Trạng thái</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {loading ? (
              <tr>
                <td colSpan="7" className="text-center py-8 text-slate-400">
                  Đang tải đơn mượn...
                </td>
              </tr>
            ) : borrowList.length === 0 ? (
              <tr>
                <td colSpan="7" className="text-center py-8 text-slate-400">
                  {filterOverdueOnly ? 'Không có đơn mượn nào bị quá hạn' : 'Không có dữ liệu đơn mượn'}
                </td>
              </tr>
            ) : (
              borrowList.map((item) => {
                const isSelected = selectedItem?.maMuon === item.maMuon;
                return (
                  <tr
                    key={item.maMuon}
                    onClick={() => setSelectedItem(item)}
                    className={`cursor-pointer transition ${
                      isSelected ? 'bg-blue-50 font-semibold' : 'hover:bg-slate-50'
                    }`}
                  >
                    <td className="py-3 px-4 font-mono font-bold text-blue-700">{item.maMuon}</td>
                    <td className="py-3 px-4 text-slate-900">{item.tenKH}</td>
                    <td className="py-3 px-4 text-slate-800 font-medium">{item.tenSach}</td>
                    <td className="py-3 px-4 text-center font-bold">{item.soLuong}</td>
                    <td className="py-3 px-4 text-slate-600">{item.ngayMuon?.slice(0, 10)}</td>
                    <td className="py-3 px-4 text-slate-600">{item.ngayTra?.slice(0, 10)}</td>
                    <td className="py-3 px-4 text-center">
                      {item.daQuaHan ? (
                        <span className="px-2.5 py-1 bg-rose-100 text-rose-800 rounded-full text-xs font-bold">
                          Quá Hạn Trả
                        </span>
                      ) : (
                        <span className="px-2.5 py-1 bg-emerald-100 text-emerald-800 rounded-full text-xs font-semibold">
                          Đang Mượn
                        </span>
                      )}
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
// 5. TAB QUẢN LÝ BẠN ĐỌC (Mục 2.2.9 & 3.7.2.5)
// ==========================================
const TabQuanLyBanDoc = ({ showToast }) => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({ maKH: '', tenKH: '', sdtKH: '' });
  const [isEditing, setIsEditing] = useState(false);

  const fetchAll = async () => {
    try {
      setLoading(true);
      const data = await customerApi.getAll();
      setCustomers(data || []);
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
    setFormData({ maKH: '', tenKH: '', sdtKH: '' });
    setIsEditing(false);
  };

  const handleSelectRow = (c) => {
    setFormData({ maKH: c.maKH, tenKH: c.tenKH, sdtKH: c.sdtKH || '' });
    setIsEditing(true);
  };

  const handleSave = async (e) => {
    e?.preventDefault();
    if (!formData.maKH.trim() || !formData.tenKH.trim()) {
      showToast('Vui lòng điền mã và tên bạn đọc', 'error');
      return;
    }

    try {
      if (isEditing) {
        await customerApi.update(formData);
        showToast('Cập nhật thông tin bạn đọc thành công!');
      } else {
        await customerApi.add(formData);
        showToast('Thêm bạn đọc mới thành công!');
      }
      handleReset();
      fetchAll();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  const handleDelete = async () => {
    if (!formData.maKH) {
      showToast('Vui lòng chọn 1 bạn đọc để xóa', 'error');
      return;
    }
    if (!window.confirm(`Bạn có chắc chắn muốn xóa bạn đọc ${formData.tenKH} (${formData.maKH})?`)) return;

    try {
      await customerApi.delete(formData.maKH);
      showToast('Xóa bạn đọc thành công!');
      handleReset();
      fetchAll();
    } catch (err) {
      showToast(err.message, 'error');
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200/80 p-6 space-y-6">
      <div className="pb-4 border-b border-slate-100">
        <h2 className="text-xl font-bold text-slate-800">Quản Lý Bạn Đọc (Khách Hàng)</h2>
        <p className="text-sm text-slate-500">Thêm mới, chỉnh sửa và quản lý danh sách độc giả thư viện</p>
      </div>

      {/* Form Input */}
      <form onSubmit={handleSave} className="bg-slate-50 p-5 rounded-2xl border border-slate-200 space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Mã Bạn Đọc (*)</label>
            <input
              type="text"
              value={formData.maKH}
              disabled={isEditing}
              onChange={(e) => setFormData({ ...formData, maKH: e.target.value })}
              placeholder="VD: KH999999"
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm font-mono font-bold disabled:bg-slate-100"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Tên Bạn Đọc (*)</label>
            <input
              type="text"
              value={formData.tenKH}
              onChange={(e) => setFormData({ ...formData, tenKH: e.target.value })}
              placeholder="Họ và tên..."
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-600 mb-1">Số Điện Thoại</label>
            <input
              type="text"
              value={formData.sdtKH}
              onChange={(e) => setFormData({ ...formData, sdtKH: e.target.value })}
              placeholder="Số điện thoại liên lạc..."
              className="w-full px-3 py-2 bg-white rounded-lg border border-slate-300 text-sm"
            />
          </div>
        </div>

        {/* Buttons */}
        <div className="flex flex-wrap items-center gap-2 pt-2">
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-bold shadow transition flex items-center gap-1.5"
          >
            {isEditing ? <Edit2 className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
            <span>{isEditing ? 'Cập Nhật Thông Tin' : 'Thêm Bạn Đọc'}</span>
          </button>

          {isEditing && (
            <button
              type="button"
              onClick={handleDelete}
              className="px-4 py-2 bg-rose-600 hover:bg-rose-700 text-white rounded-xl text-xs font-bold shadow transition flex items-center gap-1.5"
            >
              <Trash2 className="w-4 h-4" />
              <span>Xóa Bạn Đọc</span>
            </button>
          )}

          <button
            type="button"
            onClick={handleReset}
            className="px-4 py-2 bg-white border border-slate-300 hover:bg-slate-100 text-slate-700 rounded-xl text-xs font-semibold transition"
          >
            Làm Mới Form
          </button>
        </div>
      </form>

      {/* Table */}
      <div className="overflow-x-auto border border-slate-200 rounded-xl">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-100 text-slate-700 text-xs font-bold uppercase tracking-wider">
            <tr>
              <th className="py-3 px-4">Mã bạn đọc</th>
              <th className="py-3 px-4">Tên bạn đọc</th>
              <th className="py-3 px-4">Số điện thoại</th>
              <th className="py-3 px-4 text-center">Chọn sửa</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {loading ? (
              <tr>
                <td colSpan="4" className="text-center py-6 text-slate-400">
                  Đang tải danh sách...
                </td>
              </tr>
            ) : customers.length === 0 ? (
              <tr>
                <td colSpan="4" className="text-center py-6 text-slate-400">
                  Chưa có bạn đọc nào
                </td>
              </tr>
            ) : (
              customers.map((c) => (
                <tr
                  key={c.maKH}
                  onClick={() => handleSelectRow(c)}
                  className="cursor-pointer hover:bg-slate-50 transition"
                >
                  <td className="py-3 px-4 font-mono font-bold text-blue-700">{c.maKH}</td>
                  <td className="py-3 px-4 text-slate-900 font-medium">{c.tenKH}</td>
                  <td className="py-3 px-4 text-slate-600">{c.sdtKH || '—'}</td>
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
// 6. TAB THÔNG TIN TÀI KHOẢN (Mục 3.7.2.6)
// ==========================================
const TabThongTinTaiKhoan = ({ user, logout }) => {
  return (
    <div className="max-w-xl mx-auto bg-white rounded-3xl shadow-sm border border-slate-200/80 p-8 text-center space-y-6">
      <div className="w-20 h-20 mx-auto rounded-3xl bg-gradient-to-tr from-blue-600 to-indigo-600 text-white flex items-center justify-center font-black text-3xl shadow-xl shadow-blue-500/20">
        {user?.tenTK ? user.tenTK.charAt(0).toUpperCase() : 'U'}
      </div>

      <div>
        <h2 className="text-2xl font-black text-slate-900">{user?.tenTK || user?.username}</h2>
        <span className="inline-block mt-1 px-3 py-1 rounded-full text-xs font-bold bg-blue-100 text-blue-800 border border-blue-200">
          Thủ Thư (Manager) — Thư Viện HaUI
        </span>
      </div>

      <div className="bg-slate-50 p-5 rounded-2xl border border-slate-200 text-left space-y-3 text-sm">
        <div className="flex justify-between py-1 border-b border-slate-200/80">
          <span className="text-slate-500">Tên tài khoản:</span>
          <span className="font-mono font-bold text-slate-800">{user?.username}</span>
        </div>
        <div className="flex justify-between py-1 border-b border-slate-200/80">
          <span className="text-slate-500">Họ và tên:</span>
          <span className="font-semibold text-slate-800">{user?.tenTK || 'Chưa cập nhật'}</span>
        </div>
        <div className="flex justify-between py-1 border-b border-slate-200/80">
          <span className="text-slate-500">Số điện thoại:</span>
          <span className="font-semibold text-slate-800">{user?.sdtTK || 'Chưa cập nhật'}</span>
        </div>
        <div className="flex justify-between py-1">
          <span className="text-slate-500">Vai trò hệ thống:</span>
          <span className="font-bold text-blue-700">{user?.role}</span>
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

