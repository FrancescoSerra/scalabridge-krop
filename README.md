# Scalabridge-krop

Within sbt run `backend / run` to start the server at `http://127.0.0.1:8080`

## Sample requests

### Retrieve all the todos

`GET http://127.0.0.1:8080/todos`

### Create a new todo

`POST http://127.0.0.1:8080/todos`

with body 

```
{
    "title": "Test1",
    "description": "my description",
    "author": "Francesco"
}
```

### Get a single todo

`GET http://127.0.0.1:8080/todos/<todo id>`

### Delete single todo

`DELETE http://127.0.0.1:8080/todos/<todo id>`
