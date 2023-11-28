package com.nicourrrn.testprojectmobile.repo.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CounterResponse (
    @JsonProperty("counter") val counter: Int
)