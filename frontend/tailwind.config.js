/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        azzurro: '#0061FF',
        'azzurro-scuro': '#0052d9',
        'azzurro-chiaro': '#0075ff',

        'blu-notte': '#020617',

        blu: '#0F172A',

        grigio: '#F1F5F9',

        'grigio-scuro': '#828282',

        'testo-p': '#1E293B',

        'testo-s': '#64748B',     }
    },
  },
  plugins: [
    require("tailwindcss-animate"),
    require('tailwindcss-animated')
  ],
}
