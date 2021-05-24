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
