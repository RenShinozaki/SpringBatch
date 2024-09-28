package com.example.SpringBatch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class FirstTasklet implements Tasklet {
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		System.out.println("Hello World 【first step】");
		
		/*FileWriter file;
		PrintWriter pw;
		
		try
		{
			file = new FileWriter("C:\\Users\\rs978\\Desktop\\Batchtest.txt");
			pw = new PrintWriter(new BufferedWriter(file));
			
			pw.println("Hello World");
			pw.close();
		}
		catch (Exception e)
		{
			
		}
		finally
		{
			
		}*/
		
		// taskletの終了コード設定
		contribution.setExitStatus(ExitStatus.COMPLETED);
		
		// taskletの繰り返し要否ステータス
		// FINISHIED：繰り返し不要（tasklet終了）
		// CONTINUABLE：もう一度同じtaskletを呼び出す
		return RepeatStatus.FINISHED;
	}

}
