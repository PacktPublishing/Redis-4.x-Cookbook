import com.redislabs.provider.redis._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

object SumBalance extends App{
  val conf = new SparkConf()
    .setMaster("local")
    .setAppName("Spark Redis Demo")
    .set("redis.host", "192.168.1.7")

  //Initialize the sparksession
  val sparkSession = SparkSession.builder.
    master("local")
    .config(conf)
    .appName("spark session example")
    .getOrCreate()

  //Fetch the sparkcontext for spark-redis library
  val sc = sparkSession.sparkContext

  // Read Hash data from Redis
  val userHashRDD= sc.fromRedisKeyPattern("user*").getHash()

  import sparkSession.implicits._
  val ds = sparkSession.createDataset(userHashRDD)

  //Preparing the schema of the JSON data
  val schema = StructType(Seq(
    StructField("name", StringType, true),
    StructField("sex", StringType, true),
    StructField("rtime", LongType, true),
    StructField("nation", StringType, true),
    StructField("balance", DoubleType,true)
  ))

  // Parse the json data and rename the columns
  val namedDS = ds
    .withColumnRenamed("_1","id")
    .withColumnRenamed("_2","jsondata")
    .withColumn("jsondata",from_json($"jsondata", schema))

  //Generating the result [String, String] RDD
  val totalBalanceRDD = namedDS.agg(sum($"jsondata.balance")).rdd.map(total =>("totalBalance",total.get(0).toString()))


  //Write the result back to Redis`
  sc.toRedisKV(totalBalanceRDD)

}