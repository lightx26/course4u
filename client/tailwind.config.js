/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        btnLogin: "#c3c3c3",
        btnLoginHover: "#111111",
        purple: "#861fa2",
      },
    },
  },
  plugins: [
    function ({ addComponents }) {
      addComponents({
        ".custom-option": {
          "@apply text-gray-700 bg-white hover:bg-gray-200": {},
        },
      });
    },
  ],
};
