package com.example.SpringBatch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class JobDecider implements JobExecutionDecider {
	
	private String beforeStepName = "step";
	
	@Override
	public FlowExecutionStatus decide(
										JobExecution jobExecution,
										StepExecution stepExecution
									)
	{
		
		/*
		 * FlowExecutionStatus.FAILDE
		 * FlowExecutionStatus.STOPPED
		 * FlowExecutionStatus.UNKNOWN
		 * new FlowExecutionStatus("任意の文字列")
		 */
		
		//System.out.println(stepExecution.getStepName());
		
		return FlowExecutionStatus.COMPLETED;
		
		// step名が"step"
		/*if (this.beforeStepName.equals(stepExecution.getStepName()) == true)
		{
			return FlowExecutionStatus.COMPLETED;
		}
		else
		{
			return FlowExecutionStatus.STOPPED;
		}*/
		
	}
}
