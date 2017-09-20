package oj.beans

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString
class Work extends Entity {
    String project
    String company
    Double hours
    Date date
    String description
    String nickname
    String fromTime
    String toTime
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) Date fromDateTime
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) Date toDateTime
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) String duration
}
