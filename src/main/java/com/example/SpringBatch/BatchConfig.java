package com.example.SpringBatch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;

// @EnableBatchProcessing ← Spring Boot V3.0以降では不要の宣言
// https://spring.pleiades.io/guides/gs/batch-processing
@Configuration
public class BatchConfig implements JobExecutionListener {
    
    // Job Start Method From JobExecutionListner
    @Override
    public void beforeJob(JobExecution jobExecution)
    {
    	System.out.println("Job Start");
    }
    
    // Job End Method From JobExecutionListner
    @Override
    public void afterJob(JobExecution jobExecution)
    {
    	if (jobExecution.getStatus() == BatchStatus.COMPLETED)
    	{
    		System.out.println("Job End");
    	}
    	else
    	{
    		System.out.println("Job Abnormal End");
    	}
    	
    }
    
    // jobの定義
	@Bean
	Job job(JobRepository jobRepository, JobDecider jobDecider, Step step, Step firstStep) {
		
		// startで指定したstepを実行した後に、JobDeciderを起動して状態に応じて
		// next以下の処理を実行する
		
		return 
				new JobBuilder("job", jobRepository)
				.start(step)
				.next(jobDecider).on("COMPLETED").to(firstStep).on("STOPPED").to(step)
				.from(jobDecider).on("STOPPED").end()
				.end()
				.build();
		
		/*
		 *  ～～「on」で条件を指定した際のパターン～～
		 *  on("任意の文字列").to(step)
		 *  on("任意の文字列").end()
		 *  on("任意の文字列").fail()
		 */
		
		/*  パターン①
		 *  stepAが失敗→stepC
		 *  stepAが失敗以外→stepB
		 * 
		 * .start(stepA)
		 * 		.on("*").to(stepB)
		 * 	.from(stepA)
		 * 		.on("FAILED").to(stepC)
		 * .end() 
		 * .build();
		 */
		
		/*  パターン②
		 *  stepA→stepBが失敗→正常終了
		 *  stepA→stepBが失敗以外→stepC→正常終了
		 *  
		 *  .start(stepA)
		 *  .next(stepB)
		 *  	.on("FAILED").end()
		 *  .from(stepB)
		 *  	.on("*").to(stepC)
		 *  .end()
		 *  .build();
		 */
		
		/*  パターン③
		 *  stepA→stepBが失敗→異常終了
		 *  stepA→stepBが失敗以外→stepC→正常終了
		 *  
		 *  .start(stepA)
		 *  .next(stepB)
		 *  	.on("FAILED").fail()
		 *  .from(stepB)
		 *  	.on("*").to(stepC)
		 *  .end()
		 *  .build();
		 */
	}
   
	// jobExecutionDeciderの定義
	@Bean
	JobExecutionDecider decider(JobDecider jobDecider)
	{
		return jobDecider;
	}
	
	// stepの定義
    @Bean
	Step step(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
		return new StepBuilder("step", jobRepository).tasklet((contribution, chunkContext) -> {
			System.out.println("Hello world【step】");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}
    
    // tasklet definition for firstStep
    /*protected Tasklet HelloTasklet()
    {
    	return (contribution, chunkContext) -> { System.out.println("Hello World sss"); return RepeatStatus.FINISHED; };
    	//return new FirstTasklet();
    }*/
    
    // firststepの定義
    @Bean
    Step firstStep(JobRepository jobRepository, JdbcTransactionManager transactionManager, Tasklet firstTasklet) {
    	return new StepBuilder("firstStep", jobRepository).tasklet(firstTasklet, transactionManager).build();
    }
    

    
    /*
    //@Autowired
    public BatchConfig(
    				   	JobBuilderFactory jobBuilder,
    				   	StepBuilderFactory stepBuilder
                       )
    {
        this.jobBuilderFactory = jobBuilder;
        this.stepBuilderFactory = stepBuilder;
        //this.firstTasklet = firstTasklet;
    }
	*/
    /*@Bean
    JobRepository jobRepository() throws Exception {
    	JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    	// factory.setDataSource();
    	factory.setDatabaseType("h2");
    	//factory.setTransactionManager(transactionManager);
    	
    	return factory.getObject();
    		
    }*/
    
    /*
    // jobの定義
    @Bean
    //JobRepository jobRepository, PlatformTransactionManager transactionManager
    Job firstJob() {
    	// prevantRestartを不可して異常発生時に再起動を禁止
    	return this.jobBuilderFactory.get("firstJob").incrementer(new RunIdIncrementer()).start(firstStep()).build();
		//return new JobBuilder("firstJob", jobRepository).preventRestart().start(firstStep(jobRepository, transactionManager)).build();
    	// return new JobBuilder("firstJob", jobRepository).start(step1).build();
    }
    */
    
    // joblauncher（カスタマイズしたい時に作る）
    /*@Bean
    JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
    	TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    	jobLauncher.setJobRepository(jobRepository);
    	jobLauncher.afterPropertiesSet();
    	
    	return jobLauncher;
    }*/
    
    // jobregistry（作成は任意：デフォルトでは存在しない）
    /*@Bean
    JobRegistry jobRegistry() throws Exception
    {
    	MapJobRegistry jobRegistry = new MapJobRegistry();
    	return jobRegistry;
    }*/
    
}

