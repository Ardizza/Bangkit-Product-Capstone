# Gunakan image dasar node
FROM node:14

# Menetapkan direktori kerja dalam container
WORKDIR /usr/src/app

# Menyalin file package.json dan package-lock.json ke dalam container
COPY package*.json ./

# Menginstall dependencies
RUN npm install

# Menyalin seluruh kode ke dalam container
COPY . .

# Menjalankan aplikasi
CMD [ "node", "server.js" ]
