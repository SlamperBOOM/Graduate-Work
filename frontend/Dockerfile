FROM node:17-alpine as builder
WORKDIR /src/frontend

COPY public/ /src/frontend/public/
COPY src/ /src/frontend/src/
COPY package.json /src/frontend/

RUN npm install

COPY . .
RUN npm run build

EXPOSE 3000
ENTRYPOINT ["npm", "start"]