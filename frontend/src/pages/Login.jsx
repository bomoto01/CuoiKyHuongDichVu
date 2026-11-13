import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { BookOpen, Lock, User, ArrowRight, ShieldCheck, UserCheck } from 'lucide-react';

export const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e?.preventDefault();
    if (!username.trim() || !password) {
      setError('Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu');
      return;
    }

    try {
      setError('');
      setLoading(true);
      const user = await login(username.trim(), password);
      if (user.role === 'ADMIN') {
        navigate('/admin');
      } else {
        navigate('/manager');
      }
    } catch (err) {
      setError(err.message || 'Tên đăng nhập hoặc mật khẩu không chính xác');
    } finally {
      setLoading(false);
    }
  };

  const handleQuickLogin = (u, p) => {
    setUsername(u);
    setPassword(p);
    setError('');
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-950 to-slate-900 flex items-center justify-center p-4">
      <div className="max-w-md w-full">
        {/* HaUI Library Header */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-tr from-blue-600 to-indigo-500 text-white shadow-xl shadow-blue-500/30 mb-4">
            <BookOpen className="w-8 h-8" />
          </div>
          <p className="text-blue-400 font-bold text-xs uppercase tracking-widest mb-1">
            ĐẠI HỌC CÔNG NGHIỆP HÀ NỘI
          </p>
          <h2 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">
            Hệ Thống Thư Viện HaUI
          </h2>
          <p className="text-slate-400 text-sm mt-1">
            Cổng quản lý và điều hành thư viện điện tử
          </p>
        </div>

        {/* Card Form */}
        <div className="bg-white/95 backdrop-blur-md rounded-3xl p-6 sm:p-8 shadow-2xl border border-white/20">
          <h3 className="text-xl font-bold text-slate-800 mb-6 text-center">
            Đăng nhập hệ thống
          </h3>

          {error && (
            <div className="mb-5 p-3.5 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-sm flex items-center gap-2">
              <span className="font-medium">{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-slate-600 mb-1.5">
                Tên tài khoản
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <User className="w-5 h-5" />
                </div>
                <input
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Nhập tên đăng nhập..."
                  required
                  className="w-full pl-11 pr-4 py-3 rounded-xl border border-slate-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 outline-none text-slate-800 transition"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-slate-600 mb-1.5">
                Mật khẩu
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <Lock className="w-5 h-5" />
                </div>
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Nhập mật khẩu..."
                  required
                  className="w-full pl-11 pr-4 py-3 rounded-xl border border-slate-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 outline-none text-slate-800 transition"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full mt-2 py-3.5 px-4 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold rounded-xl shadow-lg shadow-blue-500/25 transition flex items-center justify-center gap-2 disabled:opacity-50"
            >
              {loading ? (
                <span>Đang xác thực...</span>
              ) : (
                <>
                  <span>Đăng nhập</span>
                  <ArrowRight className="w-4 h-4" />
                </>
              )}
            </button>
          </form>

          {/* Quick Login test buttons for grading/testing */}
          <div className="mt-6 pt-5 border-t border-slate-100">
            <p className="text-xs font-semibold text-slate-500 mb-2.5 text-center">
              Chọn nhanh tài khoản mẫu để đăng nhập:
            </p>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
              <button
                type="button"
                onClick={() => handleQuickLogin('phamquoctoan', '123456')}
                className="flex items-center gap-2 p-2.5 rounded-xl border border-blue-200 bg-blue-50/70 hover:bg-blue-100/70 text-blue-900 text-xs font-semibold transition text-left"
              >
                <UserCheck className="w-4 h-4 text-blue-600 shrink-0" />
                <div>
                  <p className="font-bold">Thủ Thư (Manager)</p>
                  <p className="text-[11px] text-blue-700 font-normal">phamquoctoan</p>
                </div>
              </button>

              <button
                type="button"
                onClick={() => handleQuickLogin('admin', '123456')}
                className="flex items-center gap-2 p-2.5 rounded-xl border border-amber-200 bg-amber-50/70 hover:bg-amber-100/70 text-amber-900 text-xs font-semibold transition text-left"
              >
                <ShieldCheck className="w-4 h-4 text-amber-600 shrink-0" />
                <div>
                  <p className="font-bold">Quản Trị Viên (Admin)</p>
                  <p className="text-[11px] text-amber-700 font-normal">admin</p>
                </div>
              </button>
            </div>
          </div>
        </div>

        <p className="text-center text-xs text-slate-500 mt-6">
          Học phần: Phát triển phần mềm hướng dịch vụ (SOA) - Nhóm 18 - HaUI
        </p>
      </div>
    </div>
  );
};

