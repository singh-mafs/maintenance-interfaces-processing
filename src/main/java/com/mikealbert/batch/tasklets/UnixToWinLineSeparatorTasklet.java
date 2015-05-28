package com.mikealbert.batch.tasklets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

public class UnixToWinLineSeparatorTasklet implements Tasklet {
	private String inputResource;
	
	@Value("#{jobParameters[inputResource]}")
	public void setInputResource(String inputResource) {
		this.inputResource = inputResource;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		String fileName = inputResource.replace("file:", "");
	    File input = new File(fileName);
	    File output = new File(fileName + ".tmp");
	    Scanner scanner = new Scanner(new FileInputStream(input));
	    String linuxSeparator = "\n";
	    String lineSeparator = System.getProperty("line.separator");
	    scanner.useDelimiter(linuxSeparator);
	    FileOutputStream out = new FileOutputStream(output);
	    
	    while (scanner.hasNext()) { // looks up to the next delimiter
	        String line = scanner.next();
	        out.write(line.getBytes());
	        out.write(lineSeparator.getBytes());
	    }
	    // the OutputStream now contains the content with new lines CR+LF instead of LF
	    out.close();
	    scanner.close();
	    output.renameTo(new File(fileName));
	    
	    return RepeatStatus.FINISHED;
	}



}
