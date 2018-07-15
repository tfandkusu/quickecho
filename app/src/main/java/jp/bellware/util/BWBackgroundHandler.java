package jp.bellware.util;

import android.os.AsyncTask;

/**
 * ワーキングスレッドで実行させる
 */
public class BWBackgroundHandler {
	public interface Task{
		/**
		 * ワーキングスレッドで実行させたいタスク
		 * @throws Exception
		 */
		void run() throws Exception;
		
		/**
		 * 成功時タスク
		 */
		void onSuccess();
		
		/**
		 * 失敗時タスク
		 * @param e
		 */
		void onException(Exception e);
	}


    /***
     * バックグラウンドで実行させる
     * @param task 実行させるタスク
     */
	public void doBack(final Task task){
		AsyncTask<Void,Void,Void> at = new AsyncTask<Void,Void,Void>(){
			private Exception e = null;
			@Override
			protected Void doInBackground(Void... params) {
				try {
					task.run();
				} catch (Exception e) {
					this.e = e;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if(e == null){
					task.onSuccess();
				}else{
					task.onException(e);
				}
			}
		};
		at.execute();
	}
	
}
