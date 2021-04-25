package br.com.project.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class MultiTickerModel {
	@JsonProperty("stream")
	@Getter
	@Setter
	private String stream; 
	@JsonProperty("data")
	@Getter
	@Setter
	private TickerStreamModel ticker;
}
