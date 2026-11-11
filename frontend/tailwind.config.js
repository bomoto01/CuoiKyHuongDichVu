/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        haui: {
          blue: '#0054a6',
          dark: '#003b75',
          gold: '#e69500',
          light: '#f0f6ff',
        }
      }
    },
  },
  plugins: [],
}
