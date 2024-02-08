# Scheduler Using Kafka Stream with Spring Cloud Stream

Using two state store in Kafka to save the accepted job and save the scheduled job.

The main idea of this scheduler is to make changes from an entity in the future.

For example, entity `Order` with status `PENDING` needs to be cancelled in 6 hours after it is created. Instead of querying the main database every single second to get the data, we can move the load from the database and use this scheduler to the changes happened on time.

## Job Data Structure
```json
{
  "createdAt": "",
  "processedAt": "",
  "jobType": "",
  "identifier": ""
}
```
## System Design
<p align="center">
  <img src="https://github.com/will004/spring-cloud-stream-kafka-scheduler/assets/29310003/06affe3c-d5dd-4dc2-b9ac-9ae871d9e2af"/>
</p>

The main idea is:
1. An application push to a Kafka topic. Let's say its name is `TopicA` with `identifier` as the key and the value is `JobData`.
2. The Kafka Stream application immediately consumes the data from `TopicA` and save it to `Job Data State Store`.
   - Before saving the data, the app validates the `createdAt` from existing data. We can attempt to get the data from `identifier`.
     - If the data is new i.e. doesn't exist, we save the data.
     - If the data is already exist and the incoming data's `createdAt` is newer, then save the data.
3. In the Kafka Stream application, there is a `Punctuator` that runs every 1 second to query the job with criteria: `processedAt >= now`. If the job fulfills the criteria, we saves the data into `Scheduled State Store` and delete the data in the `Job Data State Store`.
4. The `Punctuator` push the scheduled data to a certain topic according to its job type to another application.
   - Note: in this case, the Kafka Stream push back the job to the same application. But it can push to another application.

## Kafka Stream Topology
Using https://zz85.github.io/kafka-streams-viz/ the Kafka Topology is looked like this:
<p align="center">
  <img src="https://github.com/will004/spring-cloud-stream-kafka-scheduler/assets/29310003/932a5e75-731d-41cc-a2b4-e2d081799b52"/>
</p>

From the topology above, the branches represented two new topics that will be pushed according to the job type in `Job Data`.
