# Record-Processing Exercise

## Running Tests

`$ clj -X:test`

## Parsing a file

`$ < sample-records.csv clj -X exercise.cli/run`

To combine multiple files:

`$ cat sample-records.psv sample-records.csv sample-records.ssv | clj -X exercise.cli/run`

To specify a sort order:

`$ < sample-records.csv clj -X exercise.cli/run --output 3`

The argument to the `--output` option should be one of 1, 2, or 3, specifying one of the following output styles:

- Output 1 – sorted by email (descending), then by last name (ascending)
- Output 2 – sorted by birth date (ascending)
- Output 3 – sorted by last name (descending)

If unspecified, this program will default to output style 1.

## Starting the REST service

`$ clj -X exercise.rest/run`

The service runs on port 3000 by default (this can be overridden with
`--port`).

## Interacting with the REST service

Retrieving records from an empty database:
```
$ curl -X GET localhost:3000/records/birthdate
[]
```

Adding a record to the database:
```
$ curl -s -X POST -H "Context-Type: text/plain" localhost:3000/records \
       --data 'Aardvark, Zebulon, zebulon@example.com, red, 1/1/2001' | jq
{
  "last-name": "Aardvark",
  "first-name": "Zebulon",
  "email": "zebulon@example.com",
  "favorite-color": "red",
  "date-of-birth": "1/1/2001"
}
```

Retrieving records from a non-empty database:
```
$ curl -s -X GET localhost:3000/records/birthdate | jq
[
  {
    "last-name": "Aardvark",
    "first-name": "Zebulon",
    "email": "zebulon@example.com",
    "favorite-color": "red",
    "date-of-birth": "1/1/2001"
  }
]
```
