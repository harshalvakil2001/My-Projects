var json = {"tables":[{"tableName":"EXAM_CENTER", "columns":{"EXAM_CENTER_ID":"INTEGER","EXAM_CENTER_NAME":"VARCHAR_IGNORECASE"},"relations":[]},{"tableName":"MARKS", "columns":{"MATHS":"INTEGER","EXAM_DATE":"VARCHAR_IGNORECASE","ENGLISH":"INTEGER","EXAM_CENTER_ID":"INTEGER","HINDI":"INTEGER","STUDENT_ID":"INTEGER"},"relations":[{"fkTableName":"MARKS","fkColumnName":"EXAM_CENTER_ID","pkTableName":"EXAM_CENTER","pkColumnName":"EXAM_CENTER_ID"},{"fkTableName":"MARKS","fkColumnName":"STUDENT_ID","pkTableName":"STUDENTS","pkColumnName":"STUDENT_ID"}]},{"tableName":"STUDENTS", "columns":{"STUDENT_ID":"INTEGER","STUDENT_NAME":"VARCHAR_IGNORECASE"},"relations":[]}]};