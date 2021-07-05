# Database CSV Loader

### Solution Explanation
* This is a cli application where user can pass mapping file and csv file to load data into database.
* This is fully extensible since logic for insert data is extracted out to a yml file (mapping file) so that user can decide which data should be inserted.
* In case of changing database, user can provide custom property file using `-Dspring.config.location`


####Mapping file Explanation
* _mapping_ section is having csv header value (csv-col), db-col(database column) and transformer class in order to do data transformation.
* _check-query_ is use for checking duplication
* _insert-query_ is use for inserting the data- Note: this query will be used for dynamically add data for batch insert.

```
mapping:
- csv-col: 'data'
  db-col: 'DATA'
  transformer: 'com.dulanja33.dcl.transformers.StringTransformer'
- csv-col: 'value'
  db-col: 'VALUE'
  transformer: 'com.dulanja33.dcl.transformers.IntegerTransformer'
- csv-col: 'date'
  db-col: 'DATE'
  transformer: 'com.dulanja33.dcl.transformers.StringTransformer'
check-query: 'select count(*) from TEST where DATA = ${data}'
insert-query: "insert into TEST(DATA,VALUE,DATE) values ${(${data},${value},str_to_date(${date},'%d.%m.%y'))}"
```


### How to run
``mvn clean install`` to build the jar
then run following with values


`java -jar db-csv-loader-0.0.1-SNAPSHOT.jar --mapping <absolute-path-to-mapping> --csv <absolute-path-to-csv> --chunkSize <number of data set for one insert>`

Note: better give chunkSize 1 for given sales dataset since there are errors in csv

### Assumption

* Duplicate records based on table constraints are not inserting.
* when chunkSize is given if any insertion fails then whole batch will be considered as fail.
