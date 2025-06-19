# Weather API using Spring Boot and Redis

Currently, the application is in development.

## Redis set-up
To build a redis image run the command:
```
docker run --name redis-weather-api -p 6379:6379 -d redis
```

To view what is inside the redis image, open Redis CLI via:
```
docker exec -it redis-weather-api redis-cli
```
Example what can be stored inside at the moment:
```
127.0.0.1:6379> keys *
1) "weather:bratislava"
127.0.0.1:6379> get weather:bratislava
"Sunny in bratislava, 25\xc2\xb0C"
```