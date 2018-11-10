### Sample code for

* **Room**

## Room rx plugin logic
### ```SELECT COUNT(id) FROM Loc```
* **`Single<Integer>`** Emits 0 when there are no rows, row count otherwise. No errors.
* **`Maybe<Integer>`** Emits 0 when there are no rows, row count otherwise. No error or complete.
* **`Flowable<Integer>`** Emits row count (0 when there are no rows) on subscribing. Emits count as db keeps changing. No error or complete

### ```SELECT id FROM Loc```
* **`Single<List<Integer>>`** Emits empty list when there are no rows, list with rows otherwise. No errors.
* **`Maybe<List<Integer>>`** Emits empty list when there are no rows, list with rows otherwise. No error or complete.
* **`Flowable<List<Integer>>`** Emits list with rows (empty list when there are no rows) on subscribing. Emits lists with rows as db keeps changing. No error or complete
 
### ```SELECT * FROM Loc WHERE id=:id```
* **`Single<Loc>`** Emits Loc object if query finds a row, error otherwise.
* **`Maybe<Loc>`** Emits Loc object if query finds a row, completes otherwise. No error.
* **`Flowable<Loc>`** Emits Loc object if it exists on subscribing or is added later on. Doesn't emit when the queried row is deleted.
