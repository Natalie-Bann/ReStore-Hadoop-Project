How to Run the Project
----------------------

1. Create a working directory in HDFS
   hdfs dfs -mkdir -p /user/<username>/restore_project

2. Upload the sample input file
   hdfs dfs -put input.txt /user/<username>/restore_project/input

3. First run (normal execution)
   time yarn jar target/restore-1.0-SNAPSHOT.jar restore.ReStoreDriver \
       /user/<username>/restore_project/input \
       /user/<username>/restore_project/job1_out \
       /user/<username>/restore_project/job2_out

4. Second run (reuse demonstration)
   time yarn jar target/restore-1.0-SNAPSHOT.jar restore.ReStoreDriver \
       /user/<username>/restore_project/input \
       /user/<username>/restore_project/job1_out \
       /user/<username>/restore_project/job2_out

   - In the second run, Job1 should be skipped automatically
   - Job2 will run using Job1’s saved output

5. Check the outputs

   Job1 Output:
      hdfs dfs -cat /user/<username>/restore_project/job1_out/part-*

   Job2 Output:
      hdfs dfs -cat /user/<username>/restore_project/job2_out/part-*
