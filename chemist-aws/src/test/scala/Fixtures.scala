package funnel
package chemist
package aws

import com.amazonaws.services.ec2.model.{Instance => EC2Instance}
import com.amazonaws.services.ec2.model.{Placement,Tag}

object Fixtures {

  def instance(
    id: String,
    datacenter: String = "us-east-1b",
    privateDns: String = "foo.internal",
    publicDns: String = "foo.ext.amazonaws.com",
    tags: Seq[(String,String)] = Seq("baz" -> "v1","bar" -> "v2")
  ): EC2Instance =
    (new EC2Instance).withInstanceId(id)
    .withPlacement(new Placement(datacenter))
    .withPrivateDnsName(privateDns)
    .withPublicDnsName(publicDns)
    .withTags(tags.map { case (k,v) => new Tag(k,v) }:_*)

  val instances: Seq[EC2Instance] =
    instance("i-dx947af7") ::
    instance("i-15807647") ::
    instance("i-flaskAAA", tags = Seq("type" -> "flask")) :: Nil

  val localhost: Location =
    Location(
      host = "127.0.0.1",
      port = 5775,
      datacenter = "local",
      protocol = "http",
      intent = LocationIntent.Supervision)

  val telemetryLocalhost: Location =
    Location(
      host = "127.0.0.1",
      port = 7390,
      datacenter = "local",
      protocol = "tcp",
      intent = LocationIntent.Supervision)

  def asgEvent(
    kind: AutoScalingEventKind,
    name: String = "test-group",
    instanceId: String = "instance-id-goes-here"
  ): String = s"""
    |{
    |  "StatusCode": "InProgress",
    |  "Service": "AWS Auto Scaling",
    |  "AutoScalingGroupName": "${name}",
    |  "Description": "test",
    |  "ActivityId": "926c4ae3-8181-4668-bcd1-6febc7668d18",
    |  "Event": "${kind.notification}",
    |  "Details": {
    |    "Availability Zone": "us-east-1b"
    |  },
    |  "AutoScalingGroupARN": "...",
    |  "Progress": 50,
    |  "Time": "2014-07-31T18:30:41.244Z",
    |  "AccountId": "465404450664",
    |  "RequestId": "926c4ae3-8181-4668-bcd1-6febc7668d18",
    |  "StatusMessage": "",
    |  "EndTime": "2014-07-31T18:30:41.244Z",
    |  "EC2InstanceId": "${instanceId}",
    |  "StartTime": "2014-07-31T18:30:35.406Z",
    |  "Cause": "At 2014-07-31T18:30:35Z ..."
    |}
    """.stripMargin
}
