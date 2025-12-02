package restore;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class ReStoreDriver {

  public static int runWordCount(Configuration conf, Path input, Path output) throws Exception {
      Job job = Job.getInstance(conf, "wordcount");
      job.setJarByClass(ReStoreDriver.class);
      job.setMapperClass(WordCount.WCMapper.class);
      job.setReducerClass(WordCount.WCReducer.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(IntWritable.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);
      TextInputFormat.addInputPath(job, input);
      TextOutputFormat.setOutputPath(job, output);
      return job.waitForCompletion(true) ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
      if (args.length != 3) {
          System.err.println("Usage: ReStoreDriver <input> <job1_out> <job2_out>");
          System.exit(2);
      }

      Configuration conf = new Configuration();
      FileSystem fs = FileSystem.get(conf);

      Path input = new Path(args[0]);
      Path job1Out = new Path(args[1]);
      Path job2Out = new Path(args[2]);

      boolean needRunJob1 = !fs.exists(job1Out);

      if (needRunJob1) {
          System.out.println("Running Job1 ...");
          if (fs.exists(job1Out)) fs.delete(job1Out, true);
          int result1 = runWordCount(conf, input, job1Out);
          if (result1 != 0) System.exit(result1);
      } else {
          System.out.println("Reusing job1 output - skipping Job1.");
      }
   
      System.out.println("Running Job2 ...");
      if (fs.exists(job2Out)) fs.delete(job2Out, true);
      int result2 = runWordCount(conf, job1Out, job2Out);
      System.out.println("Job2 finished");
      System.exit(result2);
   }
}
