FROM node:17-alpine as builder
WORKDIR /usr/src/dd-frontend

COPY package*.json ./

RUN npm install

COPY . .
RUN npm run build

EXPOSE 3000
CMD ["npm", "start"]