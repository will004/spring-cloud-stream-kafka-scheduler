# Scheduler Using Kafka Stream with Spring Cloud Stream

Using two state store in Kafka to save the accepted job and save the scheduled job.

## Job Data Structure
```json
{
  "createdAt": "",
  "processedAt": "",
  "jobType": "",
  "identifier": ""
}
```
