package com.data.shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface IData {
	
	public void parseFromInput(DataInput in) throws IOException;
	
	public void writeToOutput(DataOutput out) throws IOException;
	
}
