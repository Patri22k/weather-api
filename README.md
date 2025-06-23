# Weather API

This project is simple API built in Spring Boot. It uses Redis to cache
information from 3rd party API. More info can be found in
[this website](https://roadmap.sh/projects/weather-api-wrapper-service).

## Set-up
To use this application, simply clone the repository and move to the
root of the project.
```
git clone https://github.com/Patri22k/weather-api
cd weather-api
```
There are 2 branches:
- **master:** created by me
- **zortik:** created by my dear friend and mentor

*Note: you can see the differences, general thinking, architecture
and readable/more advanced code by looking at these branches.*

### Redis set-up
As mentioned above, this project uses redis. To build a redis image
run the command:
```
docker run --name redis-weather-api -p 6379:6379 -d redis
```

To view what is inside the redis image, open Redis CLI via:
```
docker exec -it redis-weather-api redis-cli
```
You can visit a [official redis documentation](https://redis.io/docs/latest/develop/tools/cli/)
to learn more.

## How to use
You can use [Postman](https://www.postman.com/downloads/) to
create HTTP requests to use my weather API. My endpoint(s) is/are:
- {{BASE_URL}}/weather?location=Bratislava

*Note: default `{{BASE_URL}}` is `http://localhost:8080`

Both branches have default rate limiter set to 5req/min per IP address. I was
inspired by a [geeksforgeeks website](https://www.geeksforgeeks.org/advance-java/implementing-rate-limiting-in-a-spring-boot-application/).