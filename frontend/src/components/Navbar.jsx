import React from 'react';
import { useAuth } from '../context/AuthContext';
import { BookOpen, User, LogOut, ShieldCheck, Library } from 'lucide-react';

export const Navbar = ({ currentTab, onSelectTab, tabs = [] }) => {
  const { user, logout, isAdmin } = useAuth();

  return (
    <header className="bg-white border-b border-slate-200 sticky top-0 z-40 shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo & App Name */}
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-blue-700 to-blue-500 flex items-center justify-center text-white shadow-md shadow-blue-500/20">
              <BookOpen className="w-6 h-6" />
            </div>
            <div>
              <span className="text-xs font-bold tracking-wider text-blue-600 uppercase">
                Trường Đại học Công nghiệp Hà Nội
              </span>
              <h1 className="text-lg font-black text-slate-800 leading-tight">
                THƯ VIỆN HaUI
              </h1>
            </div>
          </div>

          {/* User Profile & Logout */}
          <div className="flex items-center gap-4">
            <div className="hidden sm:flex items-center gap-3 px-3 py-1.5 rounded-xl bg-slate-50 border border-slate-200/80">
              <div className="w-8 h-8 rounded-full bg-blue-100 text-blue-700 flex items-center justify-center font-bold text-sm">
                {user?.tenTK ? user.tenTK.charAt(0).toUpperCase() : 'U'}
              </div>
              <div className="text-left text-xs">
                <p className="font-bold text-slate-800 line-clamp-1">{user?.tenTK || user?.username}</p>
                <div className="flex items-center gap-1.5">
                  <span
                    className={`inline-block px-1.5 py-0.5 rounded text-[10px] font-semibold ${
                      isAdmin
                        ? 'bg-amber-100 text-amber-800 border border-amber-200'
                        : 'bg-blue-100 text-blue-800 border border-blue-200'
                    }`}
                  >
                    {isAdmin ? 'Quản Trị Viên (Admin)' : 'Thủ Thư (Manager)'}
                  </span>
                  <span className="text-slate-400">@{user?.username}</span>
                </div>
              </div>
            </div>

            <button
              onClick={logout}
              className="flex items-center gap-1.5 px-3 py-2 text-sm font-semibold text-rose-600 hover:text-rose-700 hover:bg-rose-50 rounded-xl transition duration-150"
              title="Đăng xuất"
            >
              <LogOut className="w-4 h-4" />
              <span className="hidden md:inline">Đăng xuất</span>
            </button>
          </div>
        </div>

        {/* Tab Navigation Menu */}
        {tabs.length > 0 && (
          <nav className="flex space-x-1 sm:space-x-2 border-t border-slate-100 overflow-x-auto py-2">
            {tabs.map((tab) => {
              const active = currentTab === tab.id;
              const Icon = tab.icon;
              return (
                <button
                  key={tab.id}
                  onClick={() => onSelectTab(tab.id)}
                  className={`flex items-center gap-2 px-3.5 py-2 rounded-xl text-sm font-medium whitespace-nowrap transition-all duration-150 ${
                    active
                      ? 'bg-blue-600 text-white shadow-sm shadow-blue-500/25 font-semibold'
                      : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100'
                  }`}
                >
                  {Icon && <Icon className="w-4 h-4 shrink-0" />}
                  <span>{tab.label}</span>
                </button>
              );
            })}
          </nav>
        )}
      </div>
    </header>
  );
};
