docker build . -t stock-app
docker run -d -p 8100:8100 stock-app
