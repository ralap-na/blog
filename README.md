## PostgreSQL
```sh
docker run --name blog-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=blog -p 5432:5432 -d postgres
```