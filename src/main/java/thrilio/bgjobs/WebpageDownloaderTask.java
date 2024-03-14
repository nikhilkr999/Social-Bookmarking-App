package thrilio.bgjobs;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import thrilio.dao.BookmarkDao;
import thrilio.entities.Weblink;
import thrilio.util.HttpConnect;
import thrilio.util.IOUtil;

public class WebpageDownloaderTask implements Runnable{
	
	private static BookmarkDao dao = new BookmarkDao();
	private static final long TIME_FRAME = 3000000000L;
	private boolean downloadAll = false;
	ExecutorService downloadExecutor = Executors.newFixedThreadPool(5);
	
	private static class Downloader<T extends Weblink> implements Callable<T>{
		private T weblink;
		public Downloader(T weblink) {
			this.weblink = weblink;
		}
		public T call() {
			try {
				if (!weblink.getUrl().endsWith(".pdf")) {
				
					weblink.setDownloadStatus(Weblink.DownloadStatus.FAILED);
					String htmlPage = HttpConnect.download(weblink.getUrl());
					weblink.setHtmlPage(htmlPage);
				
				}else {
					weblink.setDownloadStatus(Weblink.DownloadStatus.NOT_ELIGIBLE);
				}
			}catch(MalformedURLException e) {
				e.printStackTrace();
			}catch(URISyntaxException e) {
				e.printStackTrace();
			}
			return weblink;
		}
	}
	private void download(List<Weblink> weblinks) {
		// TODO Auto-generated method stub
		List<Downloader<Weblink>> tasks = getTasks(weblinks);
		List<Future<Weblink>> futures = new ArrayList<>();
		
		try {
			futures = downloadExecutor.invokeAll(tasks, TIME_FRAME, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Future<Weblink> future : futures) {
			try {
				if (!future.isCancelled()) {
					Weblink weblink = future.get();
					String webPage = weblink.getHtmlPage();
					if(webPage != null) {
						IOUtil.write(webPage, weblink.getId());
						weblink.setDownloadStatus(Weblink.DownloadStatus.SUCCESS);
						System.out.println("Download success: " + weblink.getUrl());
					}else {
						System.out.println("Webpage Not downloaded: " + weblink.getUrl());
					}
				} else {
					System.out.println("\n\nTask is cancelled --> " + Thread.currentThread());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
	}
	private List<Downloader<Weblink>> getTasks(List<Weblink> weblinks) {
		
		List<Downloader<Weblink>> tasks = new ArrayList<>();
		
		for(Weblink weblink : weblinks) {
			tasks.add(new Downloader<Weblink>(weblink));
		}
		return tasks;
	}
	
	public WebpageDownloaderTask(boolean downloadAll) {
		this.downloadAll = downloadAll;
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			// Get Weblinks
			List<Weblink> weblinks = getWeblinks();
			
			//Download Webpages cuncurrently
			if(weblinks.size() >0) {
				download(weblinks);
			}else {
				System.out.println("No new web links to download");
			}
			
			// wait
			try {
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		downloadExecutor.shutdown();
		
	}

	private List<Weblink> getWeblinks() {
		// TODO Auto-generated method stub
		List<Weblink> weblinks = null;
		
		if(downloadAll) {
			weblinks = dao.getAllWeblinks();
			downloadAll = false;
		}else {
			weblinks = dao.getWeblinks(Weblink.DownloadStatus.NOT_ATTEMPTED);
		}
		return weblinks;
	}
}
